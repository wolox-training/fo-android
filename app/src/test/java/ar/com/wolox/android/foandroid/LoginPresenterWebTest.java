package ar.com.wolox.android.foandroid;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.IOException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import ar.com.wolox.android.foandroid.ui.LoginPresenter;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterWebTest {

    private LoginPresenter mLoginPresenter;
    @Mock private LoginPresenter.LoginView mLoginViewMock;
    @Mock private SharedPreferences mSharedPreferencesMock;
    @Mock private SharedPreferences.Editor mEditorMock;
    private RetrofitServices mTestRetrofitServices;
    private MockWebServer mServer;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    public Semaphore mSemaphore;

    private static final String EXAMPLE_EMAIL = "email@wolox.com.ar";
    private static final String EXAMPLE_PASSWORD = "password";

    @Before
    public void setUp() throws IOException {
        mSemaphore = new Semaphore(0, false);
        mServer = new MockWebServer();
        mServer.start();
        mTestRetrofitServices = new TestRetrofitServices(mServer.url("").toString());
        mTestRetrofitServices.init();
        when(mSharedPreferencesMock.edit()).thenReturn(mEditorMock);
        when(mEditorMock.putString(any(String.class), any(String.class))).thenReturn(mEditorMock);
        doAnswer(invocation -> {
            mSemaphore.release();
            return null;
        }).when(mLoginViewMock).onLoginFailed(any(Boolean.class));
        doAnswer(invocation -> {
            mSemaphore.release();
            return null;
        }).when(mLoginViewMock).onLoginSuccessful();
        mLoginPresenter = new LoginPresenter(mLoginViewMock, mTestRetrofitServices, mSharedPreferencesMock);
        mLoginPresenter.onViewCreated();
    }

    @Test
    public void login_serverError() throws Exception {
        mServer.enqueue(new MockResponse().setResponseCode(500));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        mSemaphore.tryAcquire(1, TimeUnit.SECONDS);
        verify(mLoginViewMock, times(1)).onLoginFailed(eq(true));
    }

    @Test
    public void login_serverUnavailable() throws Exception {
        mServer.shutdown();
        mServer = null;
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        mSemaphore.tryAcquire(1, TimeUnit.SECONDS);
        verify(mLoginViewMock, times(1)).onLoginFailed(eq(false));
    }

    @Test
    public void login_successful() throws Exception {
        mServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .addHeader("Content-Type: JSON")
                .setBody(SAMPLE_JSON_ANSWER));
        mLoginPresenter.login(EXAMPLE_EMAIL, EXAMPLE_PASSWORD);
        mSemaphore.tryAcquire(1, TimeUnit.SECONDS);
        verify(mLoginViewMock, times(1)).onLoginSuccessful();
    }

    @After
    public void tearDown() {
        if (mServer != null) {
            try {
                mServer.shutdown();
            } catch (IOException e) {}
        }
    }

    private static class TestRetrofitServices extends RetrofitServices {

        private String mApiEndpoint;

        public TestRetrofitServices(String apiEndpoint) {
            mApiEndpoint = apiEndpoint;
        }

        @NonNull
        @Override
        public String getApiEndpoint() {
            return mApiEndpoint;
        }
    }

    private static final String SAMPLE_JSON_ANSWER = "[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"username\": \"susanstevens\",\n" +
            "    \"email\": \"susan.stevens38@example.com\",\n" +
            "    \"password\": \"12345678\",\n" +
            "    \"picture\": \"https://randomuser.me/api/portraits/women/72.jpg\",\n" +
            "    \"cover\": \"http://maxcdn.thedesigninspiration.com/wp-content/uploads/2012/06/Facebook-Covers-002.jpg\",\n" +
            "    \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\n" +
            "    \"location\": \"2234 Samaritan Dr, LA\",\n" +
            "    \"name\": \"Susan Stevens\",\n" +
            "    \"phone\": \"(977)-769-5019\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 2,\n" +
            "    \"username\": \"lambert.ml\",\n" +
            "    \"email\": \"melvin.lambert15@example.com\",\n" +
            "    \"password\": \"qwerty\",\n" +
            "    \"picture\": \"https://randomuser.me/api/portraits/men/51.jpg\",\n" +
            "    \"cover\": \"http://maxcdn.thedesigninspiration.com/wp-content/uploads/2012/06/Facebook-Covers-001.jpg\",\n" +
            "    \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\n" +
            "    \"location\": \"8874 Ferncliff Dr, FL\",\n" +
            "    \"name\": \"Melvin Lambert\",\n" +
            "    \"phone\": \"(207)-569-8122\"\n" +
            "  },\n" +
            "  {\n" +
            "    \"id\": 3,\n" +
            "    \"username\": \"harris59\",\n" +
            "    \"email\": \"clinton.harris59@example.com\",\n" +
            "    \"password\": \"asdasd\",\n" +
            "    \"picture\": \"https://randomuser.me/api/portraits/men/67.jpg\",\n" +
            "    \"cover\": \"http://maxcdn.thedesigninspiration.com/wp-content/uploads/2012/06/Facebook-Covers-012.jpg\",\n" +
            "    \"description\": \"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.\",\n" +
            "    \"location\": \"6742 Priceton Ave, NY\",\n" +
            "    \"name\": \"Clinton Harris\",\n" +
            "    \"phone\": \"(270)-310-1898\"\n" +
            "  }\n" +
            "]";
}
