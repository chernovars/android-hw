package com.example.arseniy.hw8_network.persistence;

import android.content.Context;

import com.example.arseniy.hw8_network.Utils;
import com.example.arseniy.hw8_network.retrofit.Client;
import com.example.arseniy.hw8_network.retrofit.NewsListPayload;
import com.example.arseniy.hw8_network.retrofit.TinkoffApiResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public class NewsRepository {
    private NewsDao mNewsDao;
    private FavNewsDao mFavNewsDao;
    private static volatile NewsRepository instance;
    public final static int TOP_NEWS_TO_PRESERVE = 3;

    public static synchronized NewsRepository getInstance(Context context) {
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

    public void add(Iterable<News> items) {
        mNewsDao.insert(items);
    }

    private void updateText(int id, String text) {
        mNewsDao.updateText(id, text);
    }

    private void deleteAllExceptNewestAndFavorites(int howManyNewestToPreserve) {
        mNewsDao.deleteAllExceptNewestAndFavorites(howManyNewestToPreserve);
    }

    public Maybe<News> get(int id) {
        return mNewsDao.getNewsById(id);
    }

    private Flowable<List<News>> getAll() {
        return mNewsDao.getAllNewsFreshFirst();
    }

    public Flowable<List<News>> getNewsWhichAreFavorite() {
        return mNewsDao.getNewsWhichAreFavoriteFlowable();
    }

    public Single<Boolean> isFavorite(int id) {
        return mFavNewsDao.getFavNewsById(id).isEmpty().map(bool -> !bool);
    }

    public void addFavorite(int id) {
        FavNews fav = new FavNews();
        fav.id = id;
        mFavNewsDao.insert(fav);
    }

    public void removeFavorite(int id) {
        mFavNewsDao.deleteById(id);
    }

    public Single<List<News>> rxDownloadNewsListPayload() {
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

    public Single<String> rxPullNewsText(int id) {
        return Client.getInstance().getApi().getNewsContentResponse(id)
                .map(tinkoffApiResponse -> tinkoffApiResponse.getPayload().getContent())
                .doOnSuccess(value -> updateText(id, value));
    }

    public Flowable<List> rxGetAllNews(boolean filterEmpty) {
        return this.getAll()
                .flatMap(list -> filterEmpty ?
                        Flowable.fromIterable(list).filter(news -> !news.fullDesc.isEmpty()).toList().toFlowable() :
                        Flowable.just(list))
                .cast(List.class);
    }

    public Completable rxDeleteAllExceptNewestAndFavorites(int howManyNewest) {
        return Completable.fromAction(() -> deleteAllExceptNewestAndFavorites(howManyNewest));
    }
}