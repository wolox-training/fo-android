package ar.com.wolox.android.foandroid.ui;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.wolmo.core.activity.WolmoActivity;

import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_DEFAULT;
import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_KEY_USER;

import butterknife.BindView;

public class HomePageActivity extends WolmoActivity {

    @BindView(R.id.activity_blank_welcome_message) TextView mWelcomeMessage;

    @Override
    protected void init() {
        mWelcomeMessage.setText(
                getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                        .getString(SP_KEY_USER, null)
        );
    }

    @Override
    protected boolean handleArguments(Bundle args) {
        return getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE).contains(SP_KEY_USER);
    }

    @Override
    protected int layout() {
        return R.layout.activity_blank;
    }

}
