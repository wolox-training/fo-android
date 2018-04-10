package ar.com.wolox.android.foandroid.validations;

import android.util.Patterns;

import ar.com.wolox.android.foandroid.R;

public class EmailFormatValidation implements Validation<String> {

    public ValidationResult validate(String email) {

        int errorMessageID = 0;
        boolean ok = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!ok) {
            errorMessageID = R.string.invalid_email_error;
        }
        return new ValidationResult(ok, errorMessageID);
    }

}
