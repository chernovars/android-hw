package com.example.arseniy.hw8_network;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DATESTR_VIEWHOLDER_TYPE = 100;
    private static final int NEWS_VIEWHOLDER_TYPE = 101;

    private static final String STRING_VALUE_EQUALS_FALSE = "227e5c51ccf3d061d3e75215479dfe9b";

    private ArrayList<Object> mDataset = new ArrayList<>();
    private NewsListFragment mHostFragment;

    NewsListAdapter(@NonNull NewsListFragment hostFragment) {
        mHostFragment = hostFragment;
    }

    void adaptNewsToDataset(List<News> newsArr) {
        //группировка по датам для датасета адаптера
        mDataset.clear();
        CharSequence lastDateStr = STRING_VALUE_EQUALS_FALSE;//Utils.customFormatDate(new Date()); //текущая дата
        CharSequence curDateStr;
        for (News news : newsArr) {
            curDateStr = Utils.customFormatDate(news.date);
            if (!lastDateStr.equals(curDateStr)) {
                mDataset.add(curDateStr);
                lastDateStr = curDateStr;
            }
            mDataset.add(news);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) instanceof String
                ? DATESTR_VIEWHOLDER_TYPE
                : NEWS_VIEWHOLDER_TYPE;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DATESTR_VIEWHOLDER_TYPE: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_date, parent, false);
                return new DateViewHolder(itemView);
            }
            case NEWS_VIEWHOLDER_TYPE: {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.viewholder_news, parent, false);
                return new NewsViewHolder(itemView);
            }
            default:
                throw new IllegalArgumentException(
                        "unknown viewType=" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case DATESTR_VIEWHOLDER_TYPE: {
                CharSequence dateStr = (CharSequence) mDataset.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.mDateTextView.setText(dateStr);
                break;
            }
            case NEWS_VIEWHOLDER_TYPE: {
                News news = (News) mDataset.get(position);
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.mTitle.setText(news.title);
                newsViewHolder.mShortDesc.setText(news.shortDesc);
                newsViewHolder.mDate = news.date;
                newsViewHolder.mId = news.id;
                break;
            }
            default: throw new IllegalArgumentException("unknown viewType=" + viewType);
        }

    }

    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mShortDesc;
        Date mDate;
        int mId;

        NewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = itemView.findViewById(R.id.news_title);
            mShortDesc = itemView.findViewById(R.id.news_short_desc);
        }

        @Override
        public void onClick(View v) {
            // запускаем активити для новости
            Context context = mHostFragment.getContext();
            if (context != null) {
                Intent intent = new Intent(context, NewsActivity.class);
                intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, mTitle.getText());
                intent.putExtra(NewsActivity.NEWS_ID_EXTRA, mId);
                mHostFragment.startActivityForResult(intent, NewsListFragment.NEWS_ACTIVITY_RETURN_KEY);
            }
        }
    }

    class DateViewHolder extends RecyclerView.ViewHolder {
        TextView mDateTextView;

        DateViewHolder(View itemView) {
            super(itemView);
            mDateTextView = itemView.findViewById(R.id.news_date);
        }
    }
}






