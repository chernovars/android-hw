package com.example.arseniy.hw8_network;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.arseniy.hw8_network.persistence.News;
import com.example.arseniy.hw8_network.persistence.NewsRepository;

import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

interface OnNewsViewHolderClickListener {
    void onNewsItemClick(News news);
}

public class NewsListFragment extends Fragment {
    private NewsListAdapter mAdapter;
    private boolean isMain;
    private static final String IS_MAIN_EXTRA = "is_main_extra";
    private SwipeRefreshLayout mSwipeRefresh;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Context mContext;

    static NewsListFragment newInstance(boolean isMain) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putBoolean(IS_MAIN_EXTRA, isMain);
        fragment.setArguments(args);
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        mAdapter = new NewsListAdapter(this::onNewsItemClick);
        Bundle args = getArguments();
        isMain = Objects.requireNonNull(args).getBoolean(IS_MAIN_EXTRA);
        updateNewsList(mContext, Utils.isConnected(mContext));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mSwipeRefresh = view.findViewById(R.id.swiperefresh);

        mSwipeRefresh.setOnRefreshListener(() -> {
            boolean isConnected = Utils.isConnected(mContext);
            if (isConnected)
                mCompositeDisposable.add(NewsApp.rxPopulateDBFromAPI(NewsRepository.getInstance(mContext)));
            else
                Utils.showWarningDialog(getActivity());
            updateNewsList(mContext, isConnected);
        });
        RecyclerView recyclerView = view.findViewById(R.id.news_list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void updateNewsList(Context context, boolean isConnected) {
        if (isMain) {
            mCompositeDisposable.add(NewsRepository.getInstance(context).rxGetAllNews(!isConnected)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::adaptAndDisableRefresh));
        }
        else {
            mCompositeDisposable.add(NewsRepository.getInstance(context).getNewsWhichAreFavorite()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::adaptAndDisableRefresh));
        }
    }

    private void adaptAndDisableRefresh(List<News> newsList) {
        mAdapter.adaptNewsToDataset(newsList);
        if (mSwipeRefresh != null) mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        mCompositeDisposable.clear();
        super.onDestroyView();
    }

    private void onNewsItemClick(News news) {
        Intent intent = new Intent(mContext, NewsActivity.class);
        intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, news.title);
        intent.putExtra(NewsActivity.NEWS_ID_EXTRA, news.id);
        this.startActivity(intent);
    }
}
