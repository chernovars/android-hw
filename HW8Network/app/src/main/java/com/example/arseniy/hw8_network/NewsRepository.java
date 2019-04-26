package com.example.arseniy.hw8_network;

import android.content.Context;

import com.example.arseniy.hw8_network.retrofit.Client;
import com.example.arseniy.hw8_network.retrofit.NewsListPayload;
import com.example.arseniy.hw8_network.retrofit.NewsListResponse;

import java.util.Collections;
import java.util.List;

import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private NewsDao mNewsDao;
    private FavNewsDao mFavNewsDao;
    private static int millisecondsInMinute = 1000 * 60;
    private static volatile NewsRepository instance;

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
        return mNewsDao.getAllNewsFreshFirst();
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
    }

    void removeFavorite(int id) {
        mFavNewsDao.deleteById(id);
    }

    Single<List<News>> rxDownloadNewsListPayload() {
        return Client.getInstance().getApi().getNewsListResponse()
                .map(NewsListResponse::getNewsListPayload)
                .map(unsorted -> {
                    Collections.sort(unsorted, (o1, o2) ->
                        Utils.compareMsDates(o1.getPublicationMsDate(), o2.getPublicationMsDate()));
                    return unsorted;
                    })
                .flatMapIterable(list -> list)
                .doOnNext(value -> value.setText(Utils.removeHtmlFromString(value.getText())))
                .map(this::newsListPayloadToNews)
                .toList();
    }

    private News newsListPayloadToNews(NewsListPayload newsListPayload) {
        News news = new News();
        news.title = newsListPayload.getText();
        news.date = Utils.fromMillisToDate(newsListPayload.getPublicationMsDate().getMilliseconds());
        news.id = Integer.parseInt(newsListPayload.getId());
        news.fullDesc = "";
        news.shortDesc = "";
        return news;
    }

    void rxDownloadNewsContent(int id, Consumer<String> consumer) {
        Client.getInstance().getApi().getNewsContentResponse(id)
                .map(newsContentResponse -> newsContentResponse.getNewsListPayload().getContent())
                .map(Utils::removeHtmlFromString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
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

    void rxPopulateDBFromAPI(Context context) {
        this.rxDownloadNewsListPayload()
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