package ar.com.wolox.android.foandroid.ui;

import android.widget.ListView;

import java.util.List;

import ar.com.wolox.android.foandroid.model.User;
import ar.com.wolox.android.foandroid.networking.LoginService;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.callback.NetworkCallback;
import okhttp3.ResponseBody;

public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {

    private RetrofitServices mRetrofitServices;

    public LoginPresenter(LoginView loginView, RetrofitServices retrofitServices) {
        super(loginView);
        mRetrofitServices = retrofitServices;
    }

    public void login(String email, String password) {
        mRetrofitServices.getService(LoginService.class)
                .getUsers(email, password).
                enqueue(new NetworkCallback<List<User>>() {
                    @Override
                    public void onResponseSuccessful(List<User> users) {
                        LoginView loginView = getView();
                        if (users.isEmpty()) {
                            loginView.onLoginFailed();
                        } else {
                            loginView.onLoginSuccessful(users.get(0));
                        }
                    }

                    @Override
                    public void onResponseFailed(ResponseBody responseBody, int i) {
                        getView().onLoginFailed();
                    }

                    @Override
                    public void onCallFailure(Throwable throwable) {
                        getView().onLoginFailed();
                    }
                });

    }

    public interface LoginView {
        void onLoginSuccessful(User user);
        void onLoginFailed();
    }
}
