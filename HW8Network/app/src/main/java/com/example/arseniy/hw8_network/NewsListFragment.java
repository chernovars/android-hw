package com.example.arseniy.hw8_network;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

interface OnNewsViewHolderClickListener {
    void onNewsItemClick(News news);
}

public class NewsListFragment extends Fragment {
    private NewsListAdapter mAdapter;
    private boolean isMain;
    private static final int NEWS_ACTIVITY_RETURN_KEY = 9788;
    private static RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefresh;
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private Application mApplication; // использую это, так как не могу полагаться на Context в onAttach

    static NewsListFragment newInstance(boolean isMain, Application application) {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        fragment.isMain = isMain;
        fragment.setRetainInstance(true);
        fragment.mApplication = application;
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(mApplication);

        mAdapter = new NewsListAdapter(this::onNewsItemClick);

        updateNewsList(mApplication, Utils.isConnected(mApplication));

        if (!isMain) {
            mCompositeDisposable.add(NewsRepository.getInstance(mApplication).getNewsWhichAreFavorite()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(mAdapter::adaptNewsToDataset));
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mSwipeRefresh = view.findViewById(R.id.swiperefresh);

        mSwipeRefresh.setOnRefreshListener(() -> {
            boolean isConnected = Utils.isConnected(mApplication);
            if (isConnected)
                mCompositeDisposable.add(NewsApp.rxPopulateDBFromAPI(NewsRepository.getInstance(mApplication)));
            else
                Utils.showWarningDialog(getActivity());
            updateNewsList(mApplication, isConnected);
            Log.d("FRAGMENT_NEWS", "ANIMATION SHOULD END HERE");
        });
        mRecyclerView = view.findViewById(R.id.news_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void updateNewsList(Context context, boolean isConnected) {
        if (isMain)
            mCompositeDisposable.add(NewsRepository.getInstance(context).rxGetAllNews(!isConnected)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::adaptAndDisableRefresh));
        else
            if (mSwipeRefresh != null) mSwipeRefresh.setRefreshing(false);
    }

    private void adaptAndDisableRefresh(List<News> newsList) {
        mAdapter.adaptNewsToDataset(newsList);
        if (mSwipeRefresh != null) mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null); //из-за адаптера утекает ресайклер вью и далее mainActivity
        mCompositeDisposable.clear();
        super.onDestroyView();
    }

    private void onNewsItemClick(News news) {
        Intent intent = new Intent(mApplication, NewsActivity.class);
        intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, news.title);
        intent.putExtra(NewsActivity.NEWS_ID_EXTRA, news.id);
        this.startActivityForResult(intent, NewsListFragment.NEWS_ACTIVITY_RETURN_KEY);
    }
}
