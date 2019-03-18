package com.example.arseniy.hw5_storage;

import android.content.Context;

public class NewsRepository{
    private NewsDao mNewsDao;
    private FavNewsDao mFavNewsDao;

    private static volatile NewsRepository instance;

    private NewsListAdapter favoritesAdapter;

    static synchronized NewsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NewsRepository(context);
        }
        return instance;
    }

    private NewsRepository(Context context) {
        NewsDatabase db = NewsDatabase.getInstance(context);
        mNewsDao = db.getNewsDao();
        mFavNewsDao = db.getFavNewsDao();
    }

    void add(News item) {
        mNewsDao.insert(item);
    }

    void add(Iterable<News> items) {
        mNewsDao.insert(items);
    }

    void update(News item) {
        mNewsDao.insert(item);
    }

    void remove(News item) {
        mNewsDao.delete(item);
    }

    void removeAll() {
        mNewsDao.deleteAll();
    }

    News get(String title) {
        return mNewsDao.getNewsByTitle(title);
    }

    News [] getNewsWhichAreFavorite() {
        return mNewsDao.getNewsWhichAreFavorite();
    }

    boolean isFavorite(String title) {
        FavNews fav = mFavNewsDao.getFavNewsByTitle(title);
        return fav != null;
    }

    void addFavorite(String title) {
        FavNews fav = new FavNews();
        fav.title = title;
        mFavNewsDao.insert(fav);
        if (favoritesAdapter != null) {

            favoritesAdapter.updateFavorites();
        }
    }

    void removeFavorite(String title) {
        mFavNewsDao.delete(title);
        if (favoritesAdapter != null)
            favoritesAdapter.updateFavorites();
    }

    void setFavoritesAdapter(NewsListAdapter favoritesAdapter) {
        this.favoritesAdapter = favoritesAdapter;
    }
}