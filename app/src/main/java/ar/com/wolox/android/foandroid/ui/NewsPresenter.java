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

    public interface NewsView {
        void onNewsLoaded(List<News> newsList);
    }
}
