package ar.com.wolox.android.foandroid.validations;

import android.text.TextUtils;

import ar.com.wolox.android.foandroid.R;

public class EmptyPasswordValidation implements Validation<String> {

    @Override
    public ValidationResult validate(String password) {

        boolean ok = !TextUtils.isEmpty(password);
        int errorMessageID = 0;
        if (!ok) {
            errorMessageID = R.string.empty_password_error;
        }
        return new ValidationResult(ok, errorMessageID);
    }

}
