package com.example.arseniy.hw5_storage;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NewsActivity extends AppCompatActivity {
    TextView mNewsTitle;
    TextView mNewsDesc;
    TextView mNewsDate;
    MenuItem mToggleFavorite;
    boolean mIsFavorite;
    int starIconsIDs[] = {R.drawable.btn_rating_star_off_normal, R.drawable.btn_rating_star_off_pressed};

    static final String NEWS_TITLE_EXTRA = "news_title_extra";
    static final String NEWS_IS_FAVORITE_EXTRA = "news_is_favorite_extra";

    String mTitle;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNewsTitle = findViewById(R.id.news_title);
        mNewsDesc = findViewById(R.id.news_full_desc);
        mNewsDate = findViewById(R.id.news_date);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            mTitle = startIntent.getStringExtra(NEWS_TITLE_EXTRA);
            mNewsTitle.setText(mTitle);

            News news = NewsRepository.getInstance(getApplicationContext()).get(mTitle);
            if (savedInstanceState != null)
                mIsFavorite = savedInstanceState.getBoolean(NEWS_IS_FAVORITE_EXTRA);
            else
                mIsFavorite = NewsRepository.getInstance(getApplicationContext()).isFavorite(mTitle);
            mNewsDate.setText(Utils.customFormatDate(news.date));
            mNewsDesc.setText(news.fullDesc);

        }
        else {
            //сюда не должны заходить (обычно)
            mNewsTitle.setText(getString(R.string.mock_news_title));
            mNewsDesc.setText(getString(R.string.mock_news_full_desc));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //здесь реализуем добавление (удаление) в таблицу избранных по клику на звездочку
        getMenuInflater().inflate(R.menu.main, menu);
        mToggleFavorite = menu.findItem(R.id.toggle_favorite);

        mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));

        mToggleFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mIsFavorite = !mIsFavorite;
                mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
                if (mIsFavorite) {
                    NewsRepository.getInstance(getApplicationContext()).addFavorite(mTitle);
                    Toast.makeText(getApplicationContext(), R.string.toast_added_to_favorites, Toast.LENGTH_SHORT).show();
                }
                else
                    NewsRepository.getInstance(getApplicationContext()).removeFavorite(mTitle);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_IS_FAVORITE_EXTRA, mIsFavorite);
    }
}
