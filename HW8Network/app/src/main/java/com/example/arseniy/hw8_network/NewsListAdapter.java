package com.example.arseniy.hw8_network;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arseniy.hw8_network.persistence.News;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DATESTR_VIEWHOLDER_TYPE = 100;
    private static final int NEWS_VIEWHOLDER_TYPE = 101;

    private ArrayList<Object> mDataset = new ArrayList<>();

    private OnNewsViewHolderClickListener onNewsViewHolderClickListener;

    NewsListAdapter(@NonNull OnNewsViewHolderClickListener listener) {
        onNewsViewHolderClickListener = listener;
    }

    void adaptNewsToDataset(List<News> newsArr) {
        //группировка по датам для датасета адаптера
        mDataset.clear();
        CharSequence lastDateStr = "";
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
                itemView.setOnClickListener(v -> {
                    News news = (News) v.getTag();
                    onNewsViewHolderClickListener.onNewsItemClick(news);
                });
                return new NewsViewHolder(itemView);
            }
            default:
                throw new IllegalArgumentException(
                        "unknown viewType=" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
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
                newsViewHolder.itemView.setTag(news);
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

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;

        NewsViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
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






