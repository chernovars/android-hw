package com.example.arseniy.hw8_network.persistence;

import android.content.Context;

import com.example.arseniy.hw8_network.Utils;
import com.example.arseniy.hw8_network.retrofit.Client;
import com.example.arseniy.hw8_network.retrofit.NewsListPayload;
import com.example.arseniy.hw8_network.retrofit.TinkoffApiResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private NewsDao mNewsDao;
    private FavNewsDao mFavNewsDao;
    private static volatile NewsRepository instance;
    private static int mTopNewsToPreserve = 3;
    private static CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static synchronized NewsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NewsRepository(context);
            instance.rxRemoveOldest(mTopNewsToPreserve);
        }
        return instance;
    }

    private NewsRepository(Context context) {
        NewsDatabase db = NewsDatabase.getInstance(context);
        mNewsDao = db.getNewsDao();
        mFavNewsDao = db.getFavNewsDao();
    }

    private void add(Iterable<News> items) {
        mNewsDao.insert(items);
    }

    private void updateText(int id, String text) {
        mNewsDao.updateText(id, text);
    }

    private void removeOldest(int howMany) {
        mNewsDao.deleteOldest(howMany);
    }

    private Maybe<News> get(int id) {
        return mNewsDao.getNewsById(id);
    }

    private Flowable<List<News>> getAll() {
        return mNewsDao.getAllNewsFreshFirst();
    }

    private Single<List<News>> getNewsWhichAreFavorite() {
        return mNewsDao.getNewsWhichAreFavorite();
    }

    private Single<Boolean> isFavorite(int id) {
        return mFavNewsDao.getFavNewsById(id).isEmpty().map(bool -> !bool);
    }

    private void addFavorite(int id) {
        FavNews fav = new FavNews();
        fav.id = id;
        mFavNewsDao.insert(fav);
    }

    private void removeFavorite(int id) {
        mFavNewsDao.deleteById(id);
    }

    private Single<List<News>> rxDownloadNewsListPayload() {
        return Client.getInstance().getApi().getNewsListResponse()
                .map(TinkoffApiResponse::getPayload)
                .map(unsorted -> {
                    Collections.sort(unsorted, (o1, o2) ->
                        Utils.compareMsDates(o1.getPublicationMsDate(), o2.getPublicationMsDate()));
                    return unsorted;
                    })
                .map(list -> list.stream().map(this::newsListPayloadToNews).collect(Collectors.toList()));

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

    public void rxPullNewsText(int id, Consumer<String> consumer) {
        compositeDisposable.add(Client.getInstance().getApi().getNewsContentResponse(id)
                .map(tinkoffApiResponse -> tinkoffApiResponse.getPayload().getContent())
                .doOnSuccess(value -> updateText(id, value))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept));
    }

    public void rxGetNews(int id, Consumer<News> consumer) {
        compositeDisposable.add(this.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept));
    }

    public void rxGetAllNews(Consumer<List<News>> consumer, boolean filterEmpty) {
        compositeDisposable.add(this.getAll()
                .map(list -> filterEmpty ? list.stream().filter(news -> !news.fullDesc.isEmpty()).collect(Collectors.toList()) : list)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept));
    }

    public void rxGetFavorites(Consumer<List<News>> consumer) {
        compositeDisposable.add(this.getNewsWhichAreFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept));
    }

    public void rxPopulateDBFromAPI(Context context) {
        //this.removeAll();
        compositeDisposable.add(this.rxDownloadNewsListPayload()
                .observeOn(Schedulers.io())
                .subscribe(this::add));
    }

    public void rxPollIsFavorite(int newsId, Consumer<Boolean> consumeIsFavorite) {
        compositeDisposable.add(this.isFavorite(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumeIsFavorite::accept));
    }


    public void rxSetIsFavorite(int newsId, boolean isNowFavorite, Runnable ifFavoriteResult) {
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

    private void rxRemoveOldest(int howMany) {
        int dummy = 1; //сингл не работает если ничего не вернуть в onSuccess
        compositeDisposable.add(Single.create(e -> {
            removeOldest(howMany);
            e.onSuccess(dummy);
        })
                .subscribeOn(Schedulers.io())
                .subscribe());
    }

    public void disposeSubscriptions() {
        compositeDisposable.clear();
    }
}