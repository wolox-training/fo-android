package ar.com.wolox.android.foandroid.ui;

import java.util.List;

import ar.com.wolox.android.foandroid.model.News;
import ar.com.wolox.android.foandroid.networking.NewsService;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.callback.NetworkCallback;
import okhttp3.ResponseBody;

public class NewsPresenter extends BasePresenter<NewsPresenter.NewsView> {

    private RetrofitServices mRetrofitServices;

    NewsPresenter(NewsPresenter.NewsView newsView, RetrofitServices retrofitServices) {
        super(newsView);
        mRetrofitServices = retrofitServices;
    }

    public void fetchNews(int current, int more) {
        mRetrofitServices.getService(NewsService.class)
                .getNews(current/more, more)
                .enqueue(new NetworkCallback<List<News>>() {

                    @Override
                    public void onResponseSuccessful(List<News> newsList) {
                        for (int i = 0; i < current%more; i++) {
                            newsList.remove(0);
                        }

                        // Repeat news. Server returns too few.
                        if (newsList != null && !newsList.isEmpty()) {
                            for (int i = 0; i < 10; i++) {
                                newsList.add(newsList.get(0));
                            }
                        }

                        if (isViewCreated()) {
                            getView().onNewsLoaded(newsList);
                        }
                    }

                    @Override
                    public void onResponseFailed(ResponseBody responseBody, int i) {
                        if (isViewCreated()) {
                            getView().onNewsLoaded(null);
                        }
                    }

                    @Override
                    public void onCallFailure(Throwable throwable) {
                        if (isViewCreated()) {
                            getView().onNewsLoaded(null);
                        }
                    }
                });
    }

    public void refreshNews(int latestNewsId) {
        refreshNews(latestNewsId, 1, null);
    }

    /*  Fetch news one by one until we find the latest news that was loaded.
        This assumes that no news are ever removed from the server. If more
        recent news are added during the refreshing, some news may not appear,
        while some may show up twice. Consider implementing a more flexible
        way to fetch news on the server side.
    */
    private void refreshNews(int latestNewsId, int page, final List<News> freshNews) {

        mRetrofitServices.getService(NewsService.class)
                .getNews(page, 1)
                .enqueue(new NetworkCallback<List<News>>() {

                    @Override
                    public void onResponseSuccessful(List<News> newsList) {

                        if (isViewCreated()) {
                            NewsView newsView = getView();
                            if (newsList.get(0).getId() == latestNewsId) {
                                if (freshNews == null) {
                                    newsList.remove(0);
                                    newsView.onNewsRefreshed(newsList);
                                } else {
                                    newsView.onNewsRefreshed(freshNews);
                                }
                            }
                            else {
                                if (freshNews == null) {
                                    refreshNews(latestNewsId, page+1, newsList);
                                } else {
                                    freshNews.addAll(newsList);
                                    refreshNews(latestNewsId, page+1, newsList);
                                }
                            }
                        }
                    }

                    @Override
                    public void onResponseFailed(ResponseBody responseBody, int i) {
                        if (isViewCreated()) {
                            getView().onNewsRefreshed(null);
                        }
                    }

                    @Override
                    public void onCallFailure(Throwable throwable) {
                        if (isViewCreated()) {
                            getView().onNewsRefreshed(null);
                        }
                    }
                });
    }

    public interface NewsView {
        void onNewsLoaded(List<News> newsList);
        void onNewsRefreshed(List<News> newsList);
    }
}
