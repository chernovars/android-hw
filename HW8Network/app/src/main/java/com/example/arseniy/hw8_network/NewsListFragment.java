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

import io.reactivex.disposables.CompositeDisposable;

interface OnNewsViewHolderClickListener {
    void onNewsItemClick(News news);
}

public class NewsListFragment extends Fragment {
    private NewsListAdapter mAdapter;
    private static NewsListAdapter mFavoritesAdapter; // тут статик, чтобы в любом фрагменте можно было обновить датасет адаптера избранных при выходе из NewsActivity
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

        if (mFavoritesAdapter == null && !isMain) mFavoritesAdapter = mAdapter;

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);

        mSwipeRefresh = view.findViewById(R.id.swiperefresh);

        mSwipeRefresh.setOnRefreshListener(() -> {
            boolean isConnected = Utils.isConnected(mApplication);
            if (isConnected)
                mCompositeDisposable.add(NewsRepository.getInstance(mApplication).rxPopulateDBFromAPI());
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

    private void adaptAndDisableRefresh(List<News> newsList) {
        mAdapter.adaptNewsToDataset(newsList);
        if (mSwipeRefresh != null) mSwipeRefresh.setRefreshing(false);
    }

    private void updateNewsList(Context context, boolean isConnected) {
        if (isMain)
            mCompositeDisposable.add(NewsRepository.getInstance(context).rxGetAllNews(this::adaptAndDisableRefresh, !isConnected));
        else
            mCompositeDisposable.add(NewsRepository.getInstance(context).rxGetFavorites(this::adaptAndDisableRefresh));
    }

    @Override
    public void onDestroyView() {
        mRecyclerView.setAdapter(null); //из-за адаптера утекает ресайклер вью и далее mainActivity
        mCompositeDisposable.clear();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Метод нужен, чтобы обновлять адаптер избранных при закрытии активити новости (так как "избранность" могла в течение активити поменяться)
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEWS_ACTIVITY_RETURN_KEY) {
            if (mFavoritesAdapter != null)
                mCompositeDisposable.add(NewsRepository.getInstance(mApplication).rxGetFavorites(mFavoritesAdapter::adaptNewsToDataset));
        }
    }

    private void onNewsItemClick(News news) {
        Intent intent = new Intent(mApplication, NewsActivity.class);
        intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, news.title);
        intent.putExtra(NewsActivity.NEWS_ID_EXTRA, news.id);
        this.startActivityForResult(intent, NewsListFragment.NEWS_ACTIVITY_RETURN_KEY);
    }
}
