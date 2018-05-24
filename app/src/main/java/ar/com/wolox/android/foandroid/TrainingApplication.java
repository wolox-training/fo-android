package ar.com.wolox.android.foandroid;

import com.facebook.drawee.backends.pipeline.Fresco;

import ar.com.wolox.android.foandroid.networking.TrainingRetrofitServices;
import ar.com.wolox.wolmo.networking.retrofit.NetworkingApplication;
import ar.com.wolox.wolmo.networking.retrofit.RetrofitServices;

public class TrainingApplication extends NetworkingApplication {

    public final static RetrofitServices RETROFIT_SERVICES_INSTANCE = new TrainingRetrofitServices();

    @Override
    public void onInit() {
        Fresco.initialize(this);
    }

    @Override
    public RetrofitServices getRetrofitServices() {
        return RETROFIT_SERVICES_INSTANCE;
    }

}