package com.example.arseniy.hw6_async;

import android.content.Context;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Entity;
import androidx.room.Insert;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import static androidx.room.OnConflictStrategy.REPLACE;

@Database(entities = {News.class, FavNews.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class NewsDatabase extends RoomDatabase {
    private static final String DB_NAME = "news_database.db";
    private static volatile NewsDatabase instance;

    static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static NewsDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                NewsDatabase.class,
                DB_NAME).build();
    }

    public abstract NewsDao getNewsDao();
    public abstract FavNewsDao getFavNewsDao();
}

class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

@Dao
interface NewsDao {
    @Nullable
    @Query("SELECT * FROM news WHERE title=:titleToSelect")
    News getNewsByTitle(String titleToSelect);

    @Insert(onConflict = REPLACE)
    void insert(News news);

    @Insert
    void insert(Iterable<News> news);

    @Query("DELETE FROM news WHERE title=:titleToDelete")
    void delete(String titleToDelete);

    @Delete
    void delete(News news);

    @Query("SELECT * FROM news ")
    News[] getAllNews();

    @Query("SELECT * FROM news, favnews WHERE news.title=favnews.title")
    News[] getNewsWhichAreFavorite();

    @Query("DELETE FROM news")
    void deleteAll();
}

@Entity
class News {
    @PrimaryKey
    @NonNull
    String title;
    Date date;
    String shortDesc;
    String fullDesc;
}

@Entity
class FavNews {
    @PrimaryKey
    @NonNull
    String title;
}

@Dao
interface FavNewsDao {
    @Nullable
    @Query("SELECT * FROM favnews WHERE title=:titleToSelect")
    FavNews getFavNewsByTitle(String titleToSelect);

    @Insert(onConflict = REPLACE)
    void insert(FavNews favnews);

    @Query("DELETE FROM favnews WHERE title=:titleToDelete")
    void delete(String titleToDelete);

    @Delete
    void delete(FavNews favnews);

    @Query("SELECT * FROM favnews ")
    FavNews[] getAllFavNews();
}



