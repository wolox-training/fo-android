package ar.com.wolox.android.foandroid.networking;

import java.util.List;

import ar.com.wolox.android.foandroid.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoginService {
    @GET("/users")
    Call<List<User>> getUsers(@Query("email") String email, @Query("password") String password);
}
