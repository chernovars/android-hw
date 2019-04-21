package com.example.arseniy.hw8_network;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.widget.LinearLayout.VERTICAL;

public class NewsListFragment extends Fragment {
    static int MOCK_NEWS_COUNT = 20;
    private NewsListAdapter mAdapter;
    private static NewsListAdapter mFavoritesAdapter; // тут статик, чтобы в любом фрагменте можно было обновить датасет адаптера избранных при выходе из NewsActivity
    private boolean isMain;
    static final int NEWS_ACTIVITY_RETURN_KEY = 9788;

    static NewsListFragment newInstance(boolean isMain) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        fragment.isMain = isMain;
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (mAdapter == null) {
            mAdapter = new NewsListAdapter(this);
            if (isMain) NewsRepository.getInstance(context).rxGetAllNews(mAdapter::adaptNewsToDataset);
            else NewsRepository.getInstance(context).rxGetFavorites(mAdapter::adaptNewsToDataset);

            if (mFavoritesAdapter == null && !isMain) mFavoritesAdapter = mAdapter;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.news_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Метод нужен, чтобы обновлять адаптер избранных при закрытии активити новости (так как "избранность" могла в течение активити поменяться)
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEWS_ACTIVITY_RETURN_KEY){
            if (mFavoritesAdapter != null) NewsRepository.getInstance(getContext()).rxGetFavorites(mFavoritesAdapter::adaptNewsToDataset);
        }
    }
}
