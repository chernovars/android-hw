package com.example.arseniy.hw8_network.persistence;

import android.content.Context;

import com.example.arseniy.hw8_network.Utils;
import com.example.arseniy.hw8_network.retrofit.Client;
import com.example.arseniy.hw8_network.retrofit.NewsListPayload;
import com.example.arseniy.hw8_network.retrofit.TinkoffApiResponse;

import java.util.List;
import java.util.function.Consumer;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private NewsDao mNewsDao;
    private FavNewsDao mFavNewsDao;
    private static volatile NewsRepository instance;
    private static int mTopNewsToPreserve = 3;

    public static synchronized NewsRepository getInstance(Context context) {
        if (instance == null) {
            instance = new NewsRepository(context);
            instance.rxDeleteAllExceptNewest(mTopNewsToPreserve);
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

    private void deleteAllExceptNewest(int howMany) {
        mNewsDao.deleteAllExceptNewest(howMany);
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
                .flattenAsObservable(list -> list)
                .toSortedList((o1, o2) -> Utils.compareMsDates(o1.getPublicationMsDate(), o2.getPublicationMsDate()))
                .flattenAsObservable(list -> list)
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

    public Disposable rxPullNewsText(int id, Consumer<String> consumer) {
        return Client.getInstance().getApi().getNewsContentResponse(id)
                .map(tinkoffApiResponse -> tinkoffApiResponse.getPayload().getContent())
                .doOnSuccess(value -> updateText(id, value))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    public Disposable rxGetNews(int id, Consumer<News> consumer) {
        return this.get(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    public Disposable rxGetAllNews(Consumer<List<News>> consumer, boolean filterEmpty) {
        return this.getAll()
                .flatMap(list -> filterEmpty ?
                        Flowable.fromIterable(list).filter(news -> !news.fullDesc.isEmpty()).toList().toFlowable() :
                        Flowable.just(list))
                .cast(List.class)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }


    public Disposable rxGetFavorites(Consumer<List<News>> consumer) {
        return this.getNewsWhichAreFavorite()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer::accept);
    }

    public Disposable rxPopulateDBFromAPI() {
        //this.removeAll();
        return this.rxDownloadNewsListPayload()
                .observeOn(Schedulers.io())
                .subscribe(this::add);
    }

    public Disposable rxPollIsFavorite(int newsId, Consumer<Boolean> consumeIsFavorite) {
        return this.isFavorite(newsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumeIsFavorite::accept);
    }


    public Disposable rxSetIsFavorite(int newsId, boolean isNowFavorite, Runnable ifFavoriteResult) {
        if (isNowFavorite)
            return Single.just(newsId)
                    .doOnSuccess(this::addFavorite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(value -> ifFavoriteResult.run())
                    .subscribe();
        else
            return Single.just(newsId)
                    .doOnSuccess(this::removeFavorite)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
    }

    private Disposable rxDeleteAllExceptNewest(int howManyNewest) {
        return Completable.fromAction(() -> deleteAllExceptNewest(howManyNewest))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}