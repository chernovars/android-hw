package com.example.arseniy.hw4_recyclerview;

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
    private RecyclerView mRecyclerView;
    private NewsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Pair<CharSequence, Date>> mNewsTitleDates = new ArrayList<>();

    static NewsListFragment newInstance(ArrayList<CharSequence> newsTitles, ArrayList<Date> newsDates) {
        NewsListFragment fragment = newInstance();
        if (newsTitles.size() != newsDates.size())
            throw new RuntimeException("newsTitles size should be equal to newsDates size");

        for (int i = 0; i < newsTitles.size(); i++) {
            fragment.mNewsTitleDates.add(new Pair<>(newsTitles.get(i), newsDates.get(i)));
        }

        Collections.sort(fragment.mNewsTitleDates, new Comparator<Pair<CharSequence, Date>>() {
            @Override
            public int compare(final Pair<CharSequence, Date> o1, final Pair<CharSequence, Date> o2) {
                return o2.second.compareTo(o1.second);
            }
        });

        return fragment;
    }

    static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new NewsListAdapter((MainActivity) getActivity(), mNewsTitleDates, getActivity().getString(R.string.mock_news_short_desc));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = view.findViewById(R.id.news_list_recyclerview);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        // Если захочется увидеть разделитель между новостями - раскоментировать следующие две строчки.
        //DividerItemDecoration decoration = new DividerItemDecoration(getActivity(), VERTICAL);
        //mRecyclerView.addItemDecoration(decoration);

        return view;
    }

    NewsListAdapter getAdapter() {
        return mAdapter;
    }
}