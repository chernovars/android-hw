package com.example.arseniy.hw6_async;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

class FavoritesLoader extends AsyncTaskLoader<Boolean> {
    String mTitle;
    Context mContext;
    public FavoritesLoader(Context context, String title) {
        super(context);
        mContext = context;
        mTitle = title;
    }
    @Override
    public Boolean loadInBackground() {
        return NewsRepository.getInstance(mContext).isFavorite(mTitle);
    }
}

