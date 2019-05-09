package com.example.arseniy.hw8_network.persistence;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;

import static androidx.room.OnConflictStrategy.IGNORE;


@Dao
interface NewsDao {
    @Nullable
    @Query("SELECT * FROM news WHERE id=:id")
    Maybe<News> getNewsById(int id);

    @Query("UPDATE news SET fullDesc=:text WHERE id=:id")
    void updateText(int id, String text);

    @Insert(onConflict = IGNORE)
    void insert(Iterable<News> news);

    @Query("SELECT * FROM news ORDER BY date DESC")
    Flowable<List<News>> getAllNewsFreshFirst();


    @Query("SELECT * FROM news, favnews WHERE news.id=favnews.id")
    Flowable<List<News>> getNewsWhichAreFavoriteFlowable();

    @Query("DELETE FROM news")
    void deleteAll();

    //без дополнительных "SELECT *" подчеркивалась ошибка в коде на UNION
    @Query("DELETE FROM news WHERE id NOT IN (" +
                "SELECT * FROM " +
                    "(SELECT id FROM news WHERE fullDesc != \"\" ORDER BY date DESC LIMIT :howManyNewestToPreserve ) " +
                "UNION " +
                "SELECT id FROM " +
                    "(SELECT * FROM news, favnews WHERE news.id=favnews.id)" +
            ")")
    void deleteAllExceptNewestAndFavorites(int howManyNewestToPreserve);
}



