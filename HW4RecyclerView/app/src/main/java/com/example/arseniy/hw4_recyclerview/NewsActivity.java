package com.example.arseniy.hw4_recyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
    static final String NEWS_DATE_EXTRA = "news_date_extra";
    static final String BROADCAST_RETURN_IS_FAVORITE_EXTRA = "news_is_favorite_bool_extra";
    static final String BROADCAST_RETURN_NEWS_TITLE_EXTRA = "news_favorite_title_extra";
    static final String BROADCAST_RETURN_NEWS_DATE_EXTRA = "news_favorite_date_extra";
    static final String BROADCAST_INTENT_ACTION = "pass_favorite";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mNewsTitle = findViewById(R.id.news_title);
        mNewsDesc = findViewById(R.id.news_full_desc);
        mNewsDesc.setText(getString(R.string.mock_news_full_desc));
        mNewsDate = findViewById(R.id.news_date);

        Intent startIntent = getIntent();
        if (startIntent != null) {
            mNewsTitle.setText(startIntent.getStringExtra(NEWS_TITLE_EXTRA));
            if (savedInstanceState != null)
                mIsFavorite = savedInstanceState.getBoolean(NEWS_IS_FAVORITE_EXTRA);
            else
                mIsFavorite = startIntent.getBooleanExtra(NEWS_IS_FAVORITE_EXTRA, false);
            mNewsDate.setText(startIntent.getCharSequenceExtra(NEWS_DATE_EXTRA));
        }
        else {
            mNewsTitle.setText(getString(R.string.mock_news_title));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        mToggleFavorite = menu.findItem(R.id.toggle_favorite);

        mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));

        mToggleFavorite.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mIsFavorite = !mIsFavorite;
                mToggleFavorite.setIcon(getDrawable(starIconsIDs[mIsFavorite ? 1 : 0]));
                sendLocalBroadCast();
                return true;
            }
        });
        return true;
    }

    private void sendLocalBroadCast() {
        // передача информации об "избранности" в MainActivity
        Intent retIntent = new Intent(BROADCAST_INTENT_ACTION);
        retIntent.putExtra(BROADCAST_RETURN_IS_FAVORITE_EXTRA, mIsFavorite);
        retIntent.putExtra(BROADCAST_RETURN_NEWS_TITLE_EXTRA, mNewsTitle.getText());
        retIntent.putExtra(BROADCAST_RETURN_NEWS_DATE_EXTRA, mNewsDate.getText());
        LocalBroadcastManager.getInstance(this).sendBroadcast(retIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NEWS_IS_FAVORITE_EXTRA, mIsFavorite);
    }
}
