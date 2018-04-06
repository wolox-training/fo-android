package ar.com.wolox.android.foandroid;

import android.app.Application;
import android.content.Context;

import ar.com.wolox.android.foandroid.networking.TrainingRetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.NetworkingApplication;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;

public class TrainingApplication extends NetworkingApplication {

    private static Context sContext;
    public final static RetrofitServices RETROFIT_SERVICES_INSTANCE = new TrainingRetrofitServices();

    @Override
    public void onInit() {
        sContext = this;
    }

    @Override
    public RetrofitServices getRetrofitServices() {
        return RETROFIT_SERVICES_INSTANCE;
    }

    public static Context getContext() {
        return sContext;
    }
}