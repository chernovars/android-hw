package com.example.arseniy.hw4_recyclerview;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int DATE_VIEWHOLDER_TYPE = 100;
    private static final int NEWS_VIEWHOLDER_TYPE = 101;

    private ArrayList<Object> mDataset = new ArrayList<>();
    private MainActivity mContext;
    private String mMockShortDesc;

     void setDataset(List<Pair<CharSequence, Date>> dataset) {
         // Приводит список пар в список объектов (дата или заголовок новости), "группируя" новости по датам
         this.mDataset.clear();
         Date lastDate = new Date();
         Date curDate;
         for (Pair<CharSequence, Date> pair : dataset) {
             curDate = pair.second;
             if (!lastDate.equals(curDate)) {
                 this.mDataset.add(curDate);
                 lastDate = curDate;
             }
             this.mDataset.add(pair);

         }
         this.notifyDataSetChanged();
     }

    NewsListAdapter(@NonNull MainActivity context, ArrayList<Pair<CharSequence, Date>> myDataset, String mockShortDesc) {
        mMockShortDesc = mockShortDesc;
        mContext = context;
        if (myDataset != null)
            setDataset(myDataset);
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position) instanceof Date
                ? DATE_VIEWHOLDER_TYPE
                : NEWS_VIEWHOLDER_TYPE;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case DATE_VIEWHOLDER_TYPE: {
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
            case DATE_VIEWHOLDER_TYPE: {
                Date date = (Date) mDataset.get(position);
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                dateViewHolder.mDateTextView.setText(Utils.customFormatDate(date));
                break;
            }
            case NEWS_VIEWHOLDER_TYPE: {
                Pair<CharSequence, Date> pairTitleDate = (Pair<CharSequence, Date>) mDataset.get(position);
                NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
                newsViewHolder.mTitle.setText(pairTitleDate.first);
                newsViewHolder.mShortDesc.setText(mMockShortDesc);
                newsViewHolder.mDate = pairTitleDate.second;
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

        NewsViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitle = itemView.findViewById(R.id.news_title);
            mShortDesc = itemView.findViewById(R.id.news_short_desc);
        }

        @Override
        public void onClick(View v) {
            // запускаем активити для новости
            Intent intent = new Intent(mContext, NewsActivity.class);
            intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, mTitle.getText());
            intent.putExtra(NewsActivity.NEWS_IS_FAVORITE_EXTRA, mContext.isFavorite(mTitle.getText()));
            intent.putExtra(NewsActivity.NEWS_DATE_EXTRA, Utils.dateFormat.format(mDate));
            mContext.startActivity(intent);
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







