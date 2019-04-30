package com.example.arseniy.hw8_network.persistence;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
interface NewsDao {
    @Nullable
    @Query("SELECT * FROM news WHERE title=:titleToSelect")
    Maybe<News> getNewsByTitle(String titleToSelect);

    @Nullable
    @Query("SELECT * FROM news WHERE id=:id")
    Maybe<News> getNewsById(int id);

    @Insert(onConflict = IGNORE)
    void insert(News news);

    @Query("UPDATE news SET fullDesc=:text WHERE id=:id")
    void updateText(int id, String text);

    @Insert(onConflict = IGNORE)
    void insert(Iterable<News> news);

    @Query("DELETE FROM news WHERE title=:titleToDelete")
    void delete(String titleToDelete);

    @Delete
    void delete(News news);

    @Query("SELECT * FROM news ORDER BY date DESC")
    Flowable<List<News>> getAllNewsFreshFirst();

    @Query("SELECT * FROM news, favnews WHERE news.id=favnews.id")
    Single<List<News>> getNewsWhichAreFavorite();

    @Query("DELETE FROM news")
    void deleteAll();

    @Query("DELETE FROM news WHERE id NOT IN (SELECT id FROM news ORDER BY date DESC LIMIT :howMany)")
    void deleteOldest(int howMany);
}



