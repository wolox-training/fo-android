package ar.com.wolox.android.foandroid.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.model.News;
import ar.com.wolox.android.foandroid.ui.OnLoadMoreListener;

public class NewsAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;
    private List<News> mNewsList;
    private int mVisibleThreshold = 5;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private boolean mIsLoading;

    public NewsAdapter(RecyclerView recyclerView, List<News> newsList) {
        this.mNewsList = newsList;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = linearLayoutManager.getItemCount();
                mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + mVisibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    mIsLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        // null represents the progress bar
        return mNewsList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list_item, parent, false);
            return new NewsViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof NewsViewHolder) {
            News news = mNewsList.get(position);
            NewsViewHolder newsViewHolder = (NewsViewHolder) holder;
            newsViewHolder.mTitle.setText(news.getTitle());
            newsViewHolder.mSummary.setText(news.getText());
            newsViewHolder.mTime.setText(news.getCreatedAt());
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mNewsList == null ? 0 : mNewsList.size();
    }

    public void setLoaded() {
        mIsLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mSummary;
        TextView mTime;

        NewsViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.news_title);
            mSummary = v.findViewById(R.id.news_summary);
            mTime = v.findViewById(R.id.news_time);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        LoadingViewHolder(View view) {
            super(view);
            progressBar = view.findViewById(R.id.loader_progressbar);
        }
    }
}