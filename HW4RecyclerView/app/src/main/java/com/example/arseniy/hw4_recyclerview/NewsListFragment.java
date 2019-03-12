package com.example.arseniy.hw4_recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListFragment extends Fragment {
    static int MOCK_NEWS_COUNT = 20;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<String> mMockData;

    public static ArrayList<String> generateMockData() {
        ArrayList<String> mockData = new ArrayList<>();
        for (int i=0; i < MOCK_NEWS_COUNT; i++) {
            mockData.add("Mock " + Integer.toString(i));
        }
        return mockData;
    }

    public static NewsListFragment newInstance(ArrayList<String> mockData) {
        NewsListFragment fragment = newInstance();
        fragment.mMockData = mockData;
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
        recyclerView = view.findViewById(R.id.news_list_recyclerview);
        //recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new NewsListAdapter(getActivity(), mMockData, getActivity().getString(R.string.mock_news_short_desc));
        recyclerView.setAdapter(mAdapter);

        return view;
    }
}