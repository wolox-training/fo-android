package ar.com.wolox.android.foandroid.networking;

import java.util.List;

import ar.com.wolox.android.foandroid.model.News;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsService {
    @GET("/news")
    Call<List<News>> getNews(@Query("_page") int page, @Query("_limit") int limit);
}