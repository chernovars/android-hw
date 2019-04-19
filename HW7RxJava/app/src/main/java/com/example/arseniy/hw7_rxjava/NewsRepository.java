package com.example.arseniy.hw7_rxjava;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    Flowable<List<News>> getAll() {
        return mNewsDao.getAllNews();
    }

    Single<List<News>> getNewsWhichAreFavorite() {
        return mNewsDao.getNewsWhichAreFavorite();
    }

    Single<Boolean> isFavorite(int id) {
        return mFavNewsDao.getFavNewsById(id).isEmpty().map(bool -> !bool);
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

    void rxGetNews(int id, Consumer<News> consumer) {
        this.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    void rxGetAllNews(Consumer<List<News>> consumer) {
        this.getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    void rxGetFavorites(Consumer<List<News>> consumer) {
        this.getNewsWhichAreFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    void rxPopulateDB(Context context) {
        ArrayList<News> news = Utils.generateNews(NewsListFragment.MOCK_NEWS_COUNT, context);
        Single.just(news)
                .observeOn(Schedulers.io())
                .subscribe(value -> {
                    this.removeAll();
                    this.add(value);
                });
    }

    void rxPollIsFavorite(int newsId, Consumer<Boolean> consumeIsFavorite) {
        this.isFavorite(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumeIsFavorite::accept);
    }

    void rxSetIsFavorite(int newsId, boolean isNowFavorite, Runnable ifFavoriteResult) {
        if (isNowFavorite)
            Single.just(newsId)
                    .doOnSuccess(this::addFavorite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(value -> ifFavoriteResult.run())
                    .subscribe();
        else
            Single.just(newsId)
                    .doOnSuccess(this::removeFavorite)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
    }
}