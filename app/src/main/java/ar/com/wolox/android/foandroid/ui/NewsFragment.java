package ar.com.wolox.android.foandroid.ui;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.adapters.NewsAdapter;
import ar.com.wolox.android.foandroid.model.News;
import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends Fragment {

    @BindView(R.id.fragment_news_recycler_view) protected RecyclerView mRecyclerView;
    @BindView(R.id.fragment_news_FAB) protected FloatingActionButton mFAB;
    private RecyclerView.LayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<News> mNewsList = new LinkedList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        for (int i = 0; i < 10; i++) {  // TODO: news should be queried from the server
            addFakeNews();
        }

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter(mRecyclerView, mNewsList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener( () -> {
                if (mNewsList.size() <= 40) {
                    mNewsList.add(null);    // Add progress bar
                    mRecyclerView.post(() -> mAdapter.notifyItemInserted(mNewsList.size() - 1));
                    //mAdapter.notifyItemInserted(mNewsList.size() - 1);
                    new Handler().postDelayed(() -> {   // TODO: news should be queried from the server

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
        news.setCreatedAt(pt.format(date)); // No i18n or l10n
        mNewsList.add(news);
    }

    private String generateTitle() {
        return "Title " + (int)(java.lang.Math.random()*500);
    }

    private String generateText() {
        return "Summary " + (int)(java.lang.Math.random()*500);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mFAB != null) {
            System.out.println("Becoming visible");
            mFAB.show();
        }
    }
}
