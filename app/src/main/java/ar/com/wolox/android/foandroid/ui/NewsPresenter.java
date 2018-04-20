package ar.com.wolox.android.foandroid.ui;

import ar.com.wolox.wolmo.core.presenter.BasePresenter;

public class NewsPresenter extends BasePresenter<NewsPresenter.NewsView> {

    public NewsPresenter(NewsPresenter.NewsView newsView) {
        super(newsView);
    }

    public interface NewsView {
        void onNewsLoaded();
    }
}
