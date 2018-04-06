package ar.com.wolox.android.foandroid;

import android.app.Application;
import android.content.Context;

public class TrainingApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
    }

    public static Context getContext() {
        return sContext;
    }
}
