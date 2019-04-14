package com.example.arseniy.hw7_rxjava;

import android.content.Context;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

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

    Maybe<News> get(int id) {
        return mNewsDao.getNewsById(id);
    }

    Single<List<News>> getAll() {
        return mNewsDao.getAllNews();
    }

    Single<List<News>> getNewsWhichAreFavorite() {
        return mNewsDao.getNewsWhichAreFavorite();
    }

    Single<Boolean> isFavorite(int id) {
        return mFavNewsDao.getFavNewsById(id).isEmpty();
    }

    void addFavorite(int id) {
        FavNews fav = new FavNews();
        fav.id = id;
        mFavNewsDao.insert(fav);
        if (favoritesAdapter != null) {
            favoritesAdapter.updateFavorites();
        }
    }

    void removeFavorite(int id) {
        mFavNewsDao.deleteById(id);
        if (favoritesAdapter != null)
            favoritesAdapter.updateFavorites();
    }

    void setFavoritesAdapter(NewsListAdapter favoritesAdapter) {
        this.favoritesAdapter = favoritesAdapter;
    }
}