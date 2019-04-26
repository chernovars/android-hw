package com.example.arseniy.hw8_network.persistence;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
interface NewsDao {
    @Nullable
    @Query("SELECT * FROM news WHERE title=:titleToSelect")
    Maybe<News> getNewsByTitle(String titleToSelect);

    @Nullable
    @Query("SELECT * FROM news WHERE id=:id")
    Maybe<News> getNewsById(int id);

    @Insert(onConflict = REPLACE)
    void insert(News news);

    @Insert
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
}



