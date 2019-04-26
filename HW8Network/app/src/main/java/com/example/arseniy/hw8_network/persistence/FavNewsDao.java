package com.example.arseniy.hw8_network.persistence;

import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Single;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
interface FavNewsDao {
    @Nullable
    @Query("SELECT * FROM favnews WHERE id=:id")
    Maybe<FavNews> getFavNewsById(int id);

    @Insert(onConflict = REPLACE)
    void insert(FavNews favnews);

    @Query("DELETE FROM favnews WHERE id=:id")
    void deleteById(int id);

    @Delete
    void delete(FavNews favnews);

    @Query("SELECT * FROM favnews ")
    Single<List<FavNews>> getAllFavNews();
}
