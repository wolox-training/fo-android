package ar.com.wolox.android.foandroid.validations;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import ar.com.wolox.android.foandroid.R;
import ar.com.wolox.android.foandroid.TrainingApplication;

public class EmptyEmailValidation implements Validation<String> {

    public EmptyEmailValidation(Context context) {
        mContext = context;
    }

    @Override
    public ValidationResult validate(String email) {

        boolean ok = !TextUtils.isEmpty(email);
        String errorMessage = null;
        if (!ok) {
            errorMessage = mContext.getResources().getString(R.string.empty_email_error);
        }
        return new ValidationResult(ok, errorMessage);
    }

    private Context mContext;
}
