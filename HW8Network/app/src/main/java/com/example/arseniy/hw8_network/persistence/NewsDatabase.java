package com.example.arseniy.hw8_network.persistence;
import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;



import static androidx.room.OnConflictStrategy.REPLACE;

@Database(entities = {News.class, FavNews.class}, version = 5)
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
                DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public abstract NewsDao getNewsDao();
    public abstract FavNewsDao getFavNewsDao();
}

