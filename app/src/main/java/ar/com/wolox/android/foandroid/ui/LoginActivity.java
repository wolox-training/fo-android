package ar.com.wolox.android.foandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.text.method.LinkMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import ar.com.wolox.android.foandroid.R;

import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_DEFAULT;
import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_KEY_EMAIL;
import ar.com.wolox.android.foandroid.validations.EmailFormatValidation;
import ar.com.wolox.android.foandroid.validations.EmptyEmailValidation;
import ar.com.wolox.android.foandroid.validations.EmptyPasswordValidation;
import ar.com.wolox.android.foandroid.validations.Validation;
import ar.com.wolox.android.foandroid.validations.ValidationResult;
import ar.com.wolox.wolmo.core.activity.WolmoActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends WolmoActivity {

    @BindView(R.id.activity_login_email_field) protected EditText mEmailField;
    @BindView(R.id.activity_login_password_field) protected EditText mPasswordField;
    @BindView(R.id.activity_login_login_button) protected Button mLoginButton;
    @BindView(R.id.activity_login_signup_button) protected Button mSignupButton;
    @BindView(R.id.activity_login_terms_and_conditions) protected TextView mTermsAndConditions;

    private List<Validation<String>> emailValidationList = buildEmailValidationList();
    private List<Validation<String>> passwordValidationList = buildPasswordValidationList();

    @Override
    protected void init() {
        final String savedEmail = getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                .getString(SP_KEY_EMAIL, null);

        if (savedEmail != null) {
            startActivity(new Intent(this, BlankActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        mTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        mTermsAndConditions.setLinkTextColor(mTermsAndConditions.getCurrentTextColor());
    }

    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.activity_login_login_button)
    void onLoginClick() {

        if (validateEmail() & validatePassword()) {
            // Save email
            final String email = mEmailField.getText().toString();
            getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                    .edit()
                    .putString(SP_KEY_EMAIL, email)
                    .apply();

            // Start blank activity
            startActivity(new Intent(this, BlankActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }

    @OnClick(R.id.activity_login_signup_button)
    void onSignupClick() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    private boolean validateFormElement(EditText formElement, List<Validation<String>> validationList) {
        for (Validation<String> validation : validationList) {
            ValidationResult res = validation.validate(formElement.getText().toString());
            if (!res.ok) {
                formElement.setError(res.errorMessage);
                return false;
            }
        }
        return true;
    }

    private boolean validateEmail() {
        return validateFormElement(mEmailField, emailValidationList);
    }

    private boolean validatePassword() {
        return validateFormElement(mPasswordField, passwordValidationList);
    }

    private List<Validation<String>> buildEmailValidationList() {
        List<Validation<String>> list = new LinkedList<>();
        list.add(new EmptyEmailValidation(this));
        list.add(new EmailFormatValidation(this));
        return list;
    }

    @Override
    public void onStop() {
        super.onStop();
        mEmailField.setError(null);
        mPasswordField.setError(null);
    }

    private List<Validation<String>> buildPasswordValidationList() {
        List<Validation<String>> list = new LinkedList<>();
        list.add(new EmptyPasswordValidation(this));
        return list;
    }

}