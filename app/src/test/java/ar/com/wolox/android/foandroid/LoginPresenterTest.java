package ar.com.wolox.android.foandroid;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.model.User;
import ar.com.wolox.android.foandroid.networking.LoginService;
import ar.com.wolox.android.foandroid.ui.LoginPresenter;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.callback.NetworkCallback;
import retrofit2.Call;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

    private LoginPresenter mLoginPresenter;
    @Mock private LoginPresenter.LoginView mLoginViewMock;
    @Mock private RetrofitServices mRetrofitServicesMock;
    @Mock private SharedPreferences mSharedPreferencesMock;
    @Mock private LoginService mLoginServiceMock;
    @Mock private Call<List<User>> mCallMock;
    @Mock private SharedPreferences.Editor mEditorMock;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private static final String EXAMPLE_EMAIL = "email@wolox.com.ar";
    private static final String EXAMPLE_PASSWORD = "password";

    @Before
    public void setUp() {
        when(mRetrofitServicesMock.getService(LoginService.class)).thenReturn(mLoginServiceMock);
        when(mLoginServiceMock.getUsers(any(String.class), any(String.class))).thenReturn(mCallMock);
        when(mSharedPreferencesMock.edit()).thenReturn(mEditorMock);
        when(mEditorMock.putString(any(String.class), any(String.class))).thenReturn(mEditorMock);
        mLoginPresenter = new LoginPresenter(mLoginViewMock, mRetrofitServicesMock, mSharedPreferencesMock);
        mLoginPresenter.onViewCreated();
    }

    @Test
    public void login_noInternet() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onCallFailure(null);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verify(mLoginViewMock, times(1)).onLoginFailed(eq(false));
    }

    @Test
    public void login_requestFailed() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseFailed(null, 0);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verify(mLoginViewMock, times(1)).onLoginFailed(eq(true));
    }

    @Test
    public void login_emptyResponse() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseSuccessful( new LinkedList<>());
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verify(mLoginViewMock, times(1)).onLoginFailed(eq(true));
    }

    @Test
    public void login_successfulResponse() {
        List<User> singleUserList = new LinkedList<>();
        User user = new User();
        user.setUsername("");
        singleUserList.add(user);

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseSuccessful(singleUserList);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verify(mLoginViewMock, times(1)).onLoginSuccessful();
    }

    @Test
    public void login_noInternetAfterViewDestroyed() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onCallFailure(null);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.onViewDestroyed();
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verifyZeroInteractions(mLoginViewMock);
    }

    @Test
    public void login_requestFailedAfterViewDestroyed() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseFailed(null, 0);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.onViewDestroyed();
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verifyZeroInteractions(mLoginViewMock);
    }

    @Test
    public void login_emptyResponseAfterViewDestroyed() {

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseSuccessful( new LinkedList<>());
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.onViewDestroyed();
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);

        verifyZeroInteractions(mLoginViewMock);
    }

    @Test
    public void login_successfulResponseAfterViewDestroyed() {
        List<User> singleUserList = new LinkedList<>();
        User user = new User();
        user.setUsername("");
        singleUserList.add(user);

        doAnswer(invocation -> {
            NetworkCallback<List<User>> networkCallback = (NetworkCallback<List<User>>)invocation.getArguments()[0];
            networkCallback.onResponseSuccessful(singleUserList);
            return null;
        }).when(mCallMock).enqueue(any(NetworkCallback.class));
        mLoginPresenter.onViewDestroyed();
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        verifyZeroInteractions(mLoginViewMock);
    }
}
