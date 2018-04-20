package ar.com.wolox.android.foandroid.ui;

import android.os.Handler;

import org.joda.time.DateTime;
import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.model.News;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;

public class NewsPresenter extends BasePresenter<NewsPresenter.NewsView> {

    NewsPresenter(NewsPresenter.NewsView newsView) {
        super(newsView);
    }

    public void fetchNews() {
        new Handler().postDelayed(() -> {   // TODO: news should be queried from the server

            List<News> newsList = new LinkedList<>();
            for (int i = 0; i < 10; i++) {
                News news = new News();
                news.setTitle(generateTitle());
                news.setText(generateText());
                DateTime dateTime = DateTime.parse("2016-07-18T14:00:29.985Z");
                long millis = dateTime.getMillis();
                Date date = new Date(millis);
                PrettyTime pt = new PrettyTime();
                news.setCreatedAt(pt.format(date)); // No i18n or l10n
                newsList.add(news);
            }
            if (isViewCreated()) {
                getView().onNewsLoaded(newsList);
            }
        }, 2000);
    }

    private String generateTitle() {
        return "Title " + (int)(java.lang.Math.random()*500);
    }

    private String generateText() {
        return "Summary " + (int)(java.lang.Math.random()*500);
    }

    public interface NewsView {
        void onNewsLoaded(List<News> newsList);
    }
}
