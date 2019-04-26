package com.example.arseniy.hw8_network;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.arseniy.hw8_network.persistence.News;
import com.example.arseniy.hw8_network.persistence.NewsRepository;

public class NewsActivity extends AppCompatActivity{
    TextView mNewsTitle;
    TextView mNewsDesc;
    TextView mNewsDate;
    MenuItem mToggleFavorite;
    boolean mIsFavorite;
    int starIconsIDs[] = {R.drawable.btn_rating_star_off_normal, R.drawable.btn_rating_star_off_pressed};

    static final String NEWS_ID_EXTRA = "news_id_extra";
    static final String NEWS_TITLE_EXTRA = "news_title_extra";
    static final String NEWS_IS_FAVORITE_EXTRA = "news_is_favorite_extra";

    int mNewsId;
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
            mNewsId = startIntent.getIntExtra(NEWS_ID_EXTRA, 0);
            mTitle = startIntent.getStringExtra(NEWS_TITLE_EXTRA);
            mNewsTitle.setText(mTitle);

            NewsRepository.getInstance(this).rxGetNews(mNewsId, this::newsToView);

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

    public void newsToView(News news) {
        mNewsDate.setText(Utils.customFormatDate(news.date));
        mNewsDesc.setText(news.fullDesc);
    }



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        NewsRepository.getInstance(this).rxDownloadNewsContent(mNewsId, mNewsDesc::setText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //здесь реализуем добавление (удаление) в таблицу избранных по клику на звездочку
        getMenuInflater().inflate(R.menu.main, menu);
        mToggleFavorite = menu.findItem(R.id.toggle_favorite);

        mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));

        NewsActivity activity = this;
        mToggleFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mIsFavorite = !mIsFavorite;
                mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
                NewsRepository.getInstance(activity).rxSetIsFavorite(mNewsId, mIsFavorite, activity::onAddFavoriteSuccess);

                return true;
            }
        });

        NewsRepository.getInstance(this).rxPollIsFavorite(mNewsId, this::setIconOnIsFavoriteResult);
        return true;
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
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_IS_FAVORITE_EXTRA, mIsFavorite);
    }


}



