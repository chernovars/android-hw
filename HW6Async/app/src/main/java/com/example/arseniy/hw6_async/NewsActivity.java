package com.example.arseniy.hw6_async;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

interface NewsActivityOnTaskCompleted {
    void onTaskCompleted(Boolean res);
    void onTaskCompleted(News news);
    void onAddFavoriteSuccess();
}

public class NewsActivity extends AppCompatActivity implements NewsActivityOnTaskCompleted {
    TextView mNewsTitle;
    TextView mNewsDesc;
    TextView mNewsDate;
    MenuItem mToggleFavorite;
    boolean mIsFavorite;
    int starIconsIDs[] = {R.drawable.btn_rating_star_off_normal, R.drawable.btn_rating_star_off_pressed};

    static final String NEWS_TITLE_EXTRA = "news_title_extra";
    static final String NEWS_IS_FAVORITE_EXTRA = "news_is_favorite_extra";

    String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNewsTitle = findViewById(R.id.news_title);
        mNewsDesc = findViewById(R.id.news_full_desc);
        mNewsDate = findViewById(R.id.news_date);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            mTitle = startIntent.getStringExtra(NEWS_TITLE_EXTRA);
            mNewsTitle.setText(mTitle);

            new GetNewsAsyncTask(this).execute(mTitle);
            if (savedInstanceState != null)
                mIsFavorite = savedInstanceState.getBoolean(NEWS_IS_FAVORITE_EXTRA);
            else
                mIsFavorite = false;

        } else {
            //сюда не должны заходить (обычно)
            mNewsTitle.setText(getString(R.string.mock_news_title));
            mNewsDesc.setText(getString(R.string.mock_news_full_desc));
        }
    }

    @Override
    public void onTaskCompleted(News news) {
        mNewsDate.setText(Utils.customFormatDate(news.date));
        mNewsDesc.setText(news.fullDesc);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //здесь реализуем добавление (удаление) в таблицу избранных по клику на звездочку
        getMenuInflater().inflate(R.menu.main, menu);
        mToggleFavorite = menu.findItem(R.id.toggle_favorite);

        mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));

        final NewsActivityOnTaskCompleted newsActivity = this;

        mToggleFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mIsFavorite = !mIsFavorite;
                mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
                Pair<String, Boolean> argsForAsync = new Pair<>(mTitle, mIsFavorite);
                new SetFavoriteAsyncTask(newsActivity).execute(argsForAsync);
                return true;
            }
        });
        new IsFavoriteAsyncTask(this).execute(mTitle);
        return true;
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_IS_FAVORITE_EXTRA, mIsFavorite);
    }


    @Override
    public void onTaskCompleted(Boolean res) {
        if (mToggleFavorite != null) {
            mIsFavorite = res;
            mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
        }
    }

    @Override
    public void onAddFavoriteSuccess() {
        Toast.makeText(this, R.string.toast_added_to_favorites, Toast.LENGTH_SHORT).show();
    }
}


class IsFavoriteAsyncTask extends AsyncTask<String, Void, Boolean> {
    private WeakReference<NewsActivityOnTaskCompleted>mListener;

    IsFavoriteAsyncTask(NewsActivityOnTaskCompleted listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        return NewsRepository.getInstance((Context) mListener.get()).isFavorite(strings[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mListener.get().onTaskCompleted(aBoolean);
    }
}

class GetNewsAsyncTask extends AsyncTask<String, Void, News> {
    private WeakReference<NewsActivityOnTaskCompleted>mListener;

    GetNewsAsyncTask(NewsActivityOnTaskCompleted listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected News doInBackground(String... strings) {
        return NewsRepository.getInstance((Context) mListener.get()).get(strings[0]);
    }

    @Override
    protected void onPostExecute(News news) {
        super.onPostExecute(news);
        mListener.get().onTaskCompleted(news);
    }
}

class SetFavoriteAsyncTask extends AsyncTask<Pair<String, Boolean>, Void, Boolean> {
    private WeakReference<NewsActivityOnTaskCompleted>mListener;

    SetFavoriteAsyncTask(NewsActivityOnTaskCompleted listener) {
        mListener = new WeakReference<>(listener);
    }

    @Override
    protected Boolean doInBackground(Pair<String, Boolean>... pairs) {
        boolean isNowFavorite = pairs[0].second;
        String title = pairs[0].first;
        if (isNowFavorite)
            NewsRepository.getInstance((Context) mListener.get()).addFavorite(title);
        else
            NewsRepository.getInstance((Context) mListener.get()).removeFavorite(title);
        return isNowFavorite;
    }

    @Override
    protected void onPostExecute(Boolean isFavorite) {
        super.onPostExecute(isFavorite);
        if (isFavorite)
            mListener.get().onAddFavoriteSuccess();
    }
}

