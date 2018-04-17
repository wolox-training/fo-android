package ar.com.wolox.android.foandroid.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ar.com.wolox.android.foandroid.R;
import butterknife.BindView;

public class NewsFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);
        mRecyclerView = view.findViewById(R.id.fragment_news_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NewsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    private static class NewsAdapter extends RecyclerView.Adapter {

        private static final String TITLE = "Ali Connors";
        private static final String SUMMARY = "I'll be in your neighbourhood doing errands ...";
        private static final String TIME = "15m";
        private static final int COUNT = 40;    // Arbitrary

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ConstraintLayout v = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_list_item, parent, false);

            return new NewsViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((NewsViewHolder) holder).mTitle.setText(TITLE);
            ((NewsViewHolder) holder).mSummary.setText(SUMMARY);
            ((NewsViewHolder) holder).mTime.setText(TIME);
        }

        @Override
        public int getItemCount() {
            return COUNT;
        }

        private static class NewsViewHolder extends RecyclerView.ViewHolder {

            TextView mTitle;
            TextView mSummary;
            TextView mTime;
            NewsViewHolder(ConstraintLayout v) {
                super(v);
                mTitle = v.findViewById(R.id.news_title);
                mSummary = v.findViewById(R.id.news_summary);
                mTime = v.findViewById(R.id.news_time);
            }
        }
    }
}
