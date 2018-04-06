package ar.com.wolox.android.foandroid.validations;

import android.content.Context;
import android.util.Patterns;

import ar.com.wolox.android.foandroid.R;

public class EmailFormatValidation implements Validation<String> {

    public EmailFormatValidation (Context context) {
        mContext = context;
    }

    public ValidationResult validate(String email) {

        String errorMessage = null;
        boolean ok = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!ok) {
            errorMessage = mContext.getResources().getString(R.string.invalid_email_error);
        }
        return new ValidationResult(ok, errorMessage);
    }

    private Context mContext;
}
