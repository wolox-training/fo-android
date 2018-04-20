package ar.com.wolox.android.foandroid.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.TrainingApplication;
import ar.com.wolox.android.foandroid.adapters.NewsAdapter;
import ar.com.wolox.android.foandroid.model.News;
import ar.com.wolox.wolmo.core.fragment.WolmoFragment;
import butterknife.BindView;

public class NewsFragment extends WolmoFragment<NewsPresenter> implements NewsPresenter.NewsView {

    @BindView(R.id.fragment_news_recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.fragment_news_FAB) protected FloatingActionButton mFAB;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<News> mNewsList = new LinkedList<>();

    private static final int PAGE_SIZE = 10;

    @Override
    public int layout() {
        return R.layout.fragment_news;
    }

    @Override
    public NewsPresenter createPresenter() {
        return new NewsPresenter(this, TrainingApplication.RETROFIT_SERVICES_INSTANCE);
    }

    @Override
    public void init() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(mRecyclerView, mNewsList);
        mRecyclerView.setAdapter(mAdapter);
        mNewsList.add(null);
        mAdapter.setOnLoadMoreListener(this::startFetchingNews);
    }

    private void startFetchingNews() {
        getPresenter().fetchNews(mNewsList.size(), PAGE_SIZE);
        if (mNewsList.size() != 0 && mNewsList.get(mNewsList.size()-1) != null) {
            mNewsList.add(null);    // Add progress bar
            mRecyclerView.post(() -> mAdapter.notifyItemInserted(mNewsList.size() - 1));
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mFAB != null) {
            System.out.println("Becoming visible");
            mFAB.show();
        }
    }

    public void onNewsLoaded(List<News> newsList) {
        mNewsList.remove(mNewsList.size() - 1);
        mAdapter.notifyItemRemoved(mNewsList.size());
        if (newsList != null) {
            mNewsList.addAll(newsList);
            mAdapter.notifyDataSetChanged();
            mAdapter.setLoaded();
        } else {
            Toast.makeText(getContext(), R.string.internet_connection_error, Toast.LENGTH_LONG).show();
        }
    }

}
