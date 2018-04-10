package ar.com.wolox.android.foandroid.ui;

import android.content.SharedPreferences;

import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.model.User;
import ar.com.wolox.android.foandroid.networking.LoginService;
import ar.com.wolox.android.foandroid.validations.EmailFormatValidation;
import ar.com.wolox.android.foandroid.validations.EmptyEmailValidation;
import ar.com.wolox.android.foandroid.validations.EmptyPasswordValidation;
import ar.com.wolox.android.foandroid.validations.Validation;
import ar.com.wolox.android.foandroid.validations.ValidationResult;
import ar.com.wolox.wolmo.core.presenter.BasePresenter;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.callback.NetworkCallback;
import okhttp3.ResponseBody;

import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_KEY_USER;

public class LoginPresenter extends BasePresenter<LoginPresenter.LoginView> {

    private RetrofitServices mRetrofitServices;
    private SharedPreferences mSharedPreferences;
    private List<Validation<String>> emailValidationList = buildEmailValidationList();
    private List<Validation<String>> passwordValidationList = buildPasswordValidationList();

    public LoginPresenter(LoginView loginView, RetrofitServices retrofitServices, SharedPreferences sharedPreferences) {
        super(loginView);
        mRetrofitServices = retrofitServices;
        mSharedPreferences = sharedPreferences;
    }

    public void login(String email, String password) {
        mRetrofitServices.getService(LoginService.class)
                .getUsers(email, password).
                enqueue(new NetworkCallback<List<User>>() {
                    @Override
                    public void onResponseSuccessful(List<User> users) {
                        LoginView loginView = getView();
                        if (users.isEmpty()) {
                            loginView.onLoginFailed(true);
                        } else {
                            mSharedPreferences
                                    .edit()
                                    .putString(SP_KEY_USER, users.get(0).getUsername())
                                    .apply();
                            loginView.onLoginSuccessful();
                        }
                    }

                    @Override
                    public void onResponseFailed(ResponseBody responseBody, int i) {
                        getView().onLoginFailed(true);
                    }

                    @Override
                    public void onCallFailure(Throwable throwable) {
                        getView().onLoginFailed(false);
                    }
                });

    }

    public ValidationResult validateEmail(String email) {
        return validateAgainstList(email, emailValidationList);
    }

    public ValidationResult validatePassword(String password) {
        return validateAgainstList(password, passwordValidationList);
    }

    private <T> ValidationResult validateAgainstList(T objectToValidate, List<Validation<T>> validationList) {
        if (validationList.isEmpty()) {
            return new ValidationResult();
        }
        ValidationResult validationResult = null;
        for (Validation<T> validation: validationList) {
            validationResult = validation.validate(objectToValidate);
            if (!validationResult.ok) {
                return validationResult;
            }
        }
        return validationResult;
    }

    private List<Validation<String>> buildEmailValidationList() {
        List<Validation<String>> list = new LinkedList<>();
        list.add(new EmptyEmailValidation());
        list.add(new EmailFormatValidation());
        return list;
    }

    private List<Validation<String>> buildPasswordValidationList() {
        List<Validation<String>> list = new LinkedList<>();
        list.add(new EmptyPasswordValidation());
        return list;
    }

    public interface LoginView {
        void onLoginSuccessful();
        void onLoginFailed(boolean activeInternetConnection);
    }
}
