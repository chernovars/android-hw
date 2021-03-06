package com.example.arseniy.hw6_async;

import android.content.Context;
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
    private boolean isMain;

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
            mAdapter = new NewsListAdapter((MainActivity) getActivity(), !isMain);
            if (!isMain) {
                // передаем в репозиторий ссылку на адаптер избранных, чтобы он оттуда обновлялся при добавлении/удалении избранных
                NewsRepository.getInstance(context).setFavoritesAdapter(mAdapter);
            }
        }
        else {
            // обновляем ссылку на новую активити (при повороте), для избежания утечки старой активити
            mAdapter.setmContext((MainActivity) getActivity());
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
}
