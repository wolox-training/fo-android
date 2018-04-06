package ar.com.wolox.android.foandroid.validations;

import android.content.Context;
import android.text.TextUtils;

import ar.com.wolox.android.foandroid.R;

public class EmptyPasswordValidation implements Validation<String> {

    public EmptyPasswordValidation(Context context) {
        mContext = context;
    }

    @Override
    public ValidationResult validate(String password) {

        boolean ok = !TextUtils.isEmpty(password);
        String errorMessage = null;
        if (!ok) {
            errorMessage = mContext.getResources().getString(R.string.empty_password_error);
        }
        return new ValidationResult(ok, errorMessage);
    }

    private Context mContext;
}
