package ar.com.wolox.android.foandroid.validations;

import android.content.Context;
import android.text.TextUtils;

import ar.com.wolox.android.foandroid.R;

public class EmptyEmailValidation implements Validation<String> {

    @Override
    public ValidationResult validate(String email) {

        boolean ok = !TextUtils.isEmpty(email);
        int errorMessageID = 0;
        if (!ok) {
            errorMessageID = R.string.empty_email_error;
        }
        return new ValidationResult(ok, errorMessageID);
    }

}
