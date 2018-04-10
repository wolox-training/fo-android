package ar.com.wolox.android.foandroid.networking;

import android.support.annotation.NonNull;

import ar.com.wolox.android.foandroid.BaseConfiguration;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;

public class TrainingRetrofitServices extends RetrofitServices {

    @NonNull
    @Override
    public String getApiEndpoint() {
        return BaseConfiguration.API_ENDPOINT;
    }
}
