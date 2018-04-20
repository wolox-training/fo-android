package ar.com.wolox.android.foandroid.adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.model.News;
import ar.com.wolox.android.foandroid.ui.OnLoadMoreListener;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_ITEM = 0;
    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VISIBLE_THRESHOLD = 5;

    private OnLoadMoreListener mOnLoadMoreListener;
    private List<News> mNewsList;
    private int mLastVisibleItem;
    private int mTotalItemCount;
    private boolean mIsLoading;

    public NewsAdapter(RecyclerView recyclerView, List<News> newsList) {
        this.mNewsList = newsList;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy != 0) {
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mLastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!mIsLoading && mTotalItemCount <= (mLastVisibleItem + VISIBLE_THRESHOLD)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        mIsLoading = true;
                    }
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
            newsViewHolder.mImage.setImageURI(news.getPicture());
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

    protected static class NewsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.news_title) TextView mTitle;
        @BindView(R.id.news_summary) TextView mSummary;
        @BindView(R.id.news_time) TextView mTime;
        @BindView(R.id.news_image) SimpleDraweeView mImage;

        NewsViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loader_progressbar) ProgressBar progressBar;

        LoadingViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}