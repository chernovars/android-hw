package com.example.arseniy.hw8_network;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import com.example.arseniy.hw8_network.persistence.News;
import com.example.arseniy.hw8_network.persistence.NewsRepository;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class NewsActivity extends AppCompatActivity {
    private TextView mNewsContent;
    private TextView mNewsDate;
    private MenuItem mToggleFavorite;
    private boolean mIsFavorite;
    private int[] starIconsIDs = {R.drawable.btn_rating_star_off_normal, R.drawable.btn_rating_star_off_pressed};
    private CompositeDisposable mCompositeDisposable;

    static final String NEWS_ID_EXTRA = "news_id_extra";
    static final String NEWS_TITLE_EXTRA = "news_title_extra";
    static final String NEWS_IS_FAVORITE_EXTRA = "news_is_favorite_extra";

    private int mNewsId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCompositeDisposable = new CompositeDisposable();

        setContentView(R.layout.activity_news);

        TextView mNewsTitle = findViewById(R.id.news_title);
        mNewsContent = findViewById(R.id.news_content);
        mNewsContent.setMovementMethod(LinkMovementMethod.getInstance());
        mNewsDate = findViewById(R.id.news_date);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            mNewsId = startIntent.getIntExtra(NEWS_ID_EXTRA, 0);
            String mTitle = startIntent.getStringExtra(NEWS_TITLE_EXTRA);
            mNewsTitle.setText(mTitle);

            mCompositeDisposable.add(NewsRepository.getInstance(this).get(mNewsId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::pullFullDesc));

            if (savedInstanceState != null)
                mIsFavorite = savedInstanceState.getBoolean(NEWS_IS_FAVORITE_EXTRA);
            else
                mIsFavorite = false;
        }
    }

    public void pullFullDesc(News news) {
        mNewsDate.setText(Utils.customFormatDate(news.date));
        if (news.fullDesc.isEmpty() && Utils.isConnected(this))
            mCompositeDisposable.add(
                    NewsRepository.getInstance(this).rxPullNewsText(mNewsId)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(text -> setTextAsHtml(mNewsContent, text))
            );
        else
            setTextAsHtml(mNewsContent, news.fullDesc);
    }

    private void setTextAsHtml(TextView view, String text) {
        view.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //здесь реализуем добавление (удаление) в таблицу избранных по клику на звездочку
        getMenuInflater().inflate(R.menu.main, menu);
        mToggleFavorite = menu.findItem(R.id.toggle_favorite);

        mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));

        NewsActivity activity = this;
        mToggleFavorite.setOnMenuItemClickListener(item -> {
            mIsFavorite = !mIsFavorite;
            mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
            mCompositeDisposable.add(
                    rxSetIsFavorite(mNewsId, mIsFavorite, NewsRepository.getInstance(activity))
            );

            return true;
        });

        NewsRepository repo = NewsRepository.getInstance(this);

        mCompositeDisposable.add(repo.isFavorite(mNewsId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setIconOnIsFavoriteResult)
        );
        return true;
    }

    public Disposable rxSetIsFavorite(int newsId, boolean isNowFavorite, NewsRepository repository) {
        if (isNowFavorite)
            return Single.just(newsId)
                    .doOnSuccess(repository::addFavorite)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSuccess(value -> onAddFavoriteSuccess())
                    .subscribe();
        else
            return Single.just(newsId)
                    .doOnSuccess(repository::removeFavorite)
                    .subscribeOn(Schedulers.io())
                    .subscribe();
    }


    public void onAddFavoriteSuccess() {
        Toast.makeText(this, R.string.toast_added_to_favorites, Toast.LENGTH_SHORT).show();
    }

    public void setIconOnIsFavoriteResult(Boolean res) {
        if (mToggleFavorite != null) {
            mIsFavorite = res;
            mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_IS_FAVORITE_EXTRA, mIsFavorite);
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.clear();
        super.onDestroy();
    }
}



