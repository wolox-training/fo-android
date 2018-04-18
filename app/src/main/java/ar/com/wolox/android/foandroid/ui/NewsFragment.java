package ar.com.wolox.android.foandroid.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.model.News;

public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<News> mNewsList = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        for (int i = 0; i < 10; i++) {
            addFakeNews();
        }

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_news_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(mRecyclerView, mNewsList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener( () -> {
                if (mNewsList.size() <= 40) {
                    mNewsList.add(null);    // Add progress bar
                    mAdapter.notifyItemInserted(mNewsList.size() - 1);
                    new Handler().postDelayed(() -> {

                            mNewsList.remove(mNewsList.size() - 1); // Remove progress bar
                            mAdapter.notifyItemRemoved(mNewsList.size());

                            //Generating more data
                            int index = mNewsList.size();
                            int end = index + 10;
                            for (int i = index; i < end; i++) {
                                addFakeNews();
                            }
                            mAdapter.notifyDataSetChanged();
                            mAdapter.setLoaded();
                    }, 2000);
                } else {
                    Toast.makeText(NewsFragment.this.getContext(), R.string.no_more_news, Toast.LENGTH_SHORT).show();
                }
        });

        return view;
    }

    private void addFakeNews() {
        News news = new News();
        news.setTitle(generateTitle());
        news.setText(generateText());
        DateTime dateTime = DateTime.parse("2016-07-18T14:00:29.985Z");
        long millis = dateTime.getMillis();
        Date date = new Date(millis);
        PrettyTime pt = new PrettyTime();
        news.setCreatedAt(pt.format(date));
        mNewsList.add(news);
    }

    private String generateTitle() {
        return "Title " + (int)(java.lang.Math.random()*500);
    }

    private String generateText() {
        return "Summary " + (int)(java.lang.Math.random()*500);
    }

    private static class NewsAdapter extends RecyclerView.Adapter {

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
}
