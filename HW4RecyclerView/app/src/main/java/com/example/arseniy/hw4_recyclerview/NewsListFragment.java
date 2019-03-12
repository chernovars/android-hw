package com.example.arseniy.hw4_recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListFragment extends Fragment {
    static int MOCK_NEWS_COUNT = 20;
    private RecyclerView mRecyclerView;
    private NewsListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    ArrayList<CharSequence> mNewsTitles;

    public static ArrayList<CharSequence> generateMockData() {
        ArrayList<CharSequence> mockData = new ArrayList<>();
        for (int i=0; i < MOCK_NEWS_COUNT; i++) {
            mockData.add("Mock " + Integer.toString(i));
        }
        return mockData;
    }

    public static NewsListFragment newInstance(ArrayList<CharSequence> newsTitles) {
        NewsListFragment fragment = newInstance();
        fragment.mNewsTitles = newsTitles;
        return fragment;
    }

    public static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mRecyclerView = view.findViewById(R.id.news_list_recyclerview);
        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NewsListAdapter((MainActivity) getActivity(), mNewsTitles, getActivity().getString(R.string.mock_news_short_desc));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (mNewsTitles == null) {
                HashSet<CharSequence> test = ((MainActivity) getActivity()).getFavorites();
                ArrayList<CharSequence> favorites = new ArrayList<>(test);

                mAdapter.setDataset(favorites);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}