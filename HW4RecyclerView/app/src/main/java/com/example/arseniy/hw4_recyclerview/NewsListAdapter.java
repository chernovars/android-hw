package com.example.arseniy.hw4_recyclerview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {
    public void setDataset(ArrayList<CharSequence> mDataset) {
        this.mDataset = mDataset;
    }

    private ArrayList<CharSequence> mDataset;

    private MainActivity mContext;
    String mMockShortDesc;

    class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView mTitle;
        TextView mShortDesc;

        NewsViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.news_title);
            mShortDesc = itemView.findViewById(R.id.news_short_desc);
        }
    }


    public NewsListAdapter(@NonNull MainActivity context, ArrayList<CharSequence> myDataset, String mockShortDesc) {
        mDataset = myDataset;
        mMockShortDesc = mockShortDesc;
        mContext = context;
        LocalBroadcastManager.getInstance(context).registerReceiver(context.getFavoriteBroadcastReceiver(),
                new IntentFilter(NewsActivity.BROADCAST_INTENT_ACTION));
    }

    private void startNewsActivity(NewsViewHolder v) {
        // активити для одной "news"
        Intent intent = new Intent(mContext, NewsActivity.class);
        intent.putExtra(NewsActivity.NEWS_TITLE_EXTRA, v.mTitle.getText());
        intent.putExtra(NewsActivity.NEWS_IS_FAVORITE_EXTRA, mContext.getFavorites().contains(v.mTitle.getText()));
        mContext.startActivity(intent);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_news, parent, false);
        final NewsViewHolder mvh = new NewsViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View _v) {
                startNewsActivity(mvh);
            }
        });
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        holder.mTitle.setText(mDataset.get(position));
        holder.mShortDesc.setText(mMockShortDesc);
    }

    @Override
    public int getItemCount() {
        if (mDataset != null)
            return mDataset.size();
        else
            return 0;
    }
}


