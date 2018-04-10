package ar.com.wolox.android.foandroid.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import ar.com.wolox.android.foandroid.R;

import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_DEFAULT;
import static ar.com.wolox.android.foandroid.BaseConfiguration.SP_KEY_USER;

import ar.com.wolox.android.foandroid.TrainingApplication;
import ar.com.wolox.android.foandroid.validations.Validation;
import ar.com.wolox.android.foandroid.validations.ValidationResult;
import ar.com.wolox.wolmo.core.activity.WolmoActivity;
import ar.com.wolox.wolmo.core.util.ToastUtils;
import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends WolmoActivity implements LoginPresenter.LoginView {

    @BindView(R.id.activity_login_email_field) protected EditText mEmailField;
    @BindView(R.id.activity_login_password_field) protected EditText mPasswordField;
    @BindView(R.id.activity_login_login_button) protected Button mLoginButton;
    @BindView(R.id.activity_login_signup_button) protected Button mSignupButton;
    @BindView(R.id.activity_login_terms_and_conditions) protected TextView mTermsAndConditions;
    @BindView(R.id.activity_login_progress_bar) protected ProgressBar mProgressBar;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void init() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE);
        final String username = getSharedPreferences(SP_DEFAULT, Context.MODE_PRIVATE)
                .getString(SP_KEY_USER, null);

        if (username != null) {
            startActivity(new Intent(this, BlankActivity.class)
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

        mTermsAndConditions.setMovementMethod(LinkMovementMethod.getInstance());
        mTermsAndConditions.setLinkTextColor(mTermsAndConditions.getCurrentTextColor());

        mLoginPresenter = new LoginPresenter(
                this,
                TrainingApplication.RETROFIT_SERVICES_INSTANCE,
                sharedPreferences);
        mLoginPresenter.onViewCreated();
    }

    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.activity_login_login_button)
    void onLoginClick() {

        if (validateEmail() & validatePassword()) {
            mProgressBar.setVisibility(View.VISIBLE);
            mLoginButton.setEnabled(false);
            mLoginPresenter.login(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

    @OnClick(R.id.activity_login_signup_button)
    void onSignupClick() {
        startActivity(new Intent(this, SignupActivity.class));
    }

    @Override
    public void onLoginSuccessful() {
        startActivity(new Intent(this, BlankActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
    }

    @Override
    public void onLoginFailed(boolean activeInternetConnection) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mLoginButton.setEnabled(true);
        if (activeInternetConnection) {
            ToastUtils.show(R.string.invalid_credentials_error);
        } else {
            ToastUtils.show(R.string.internet_connection_error);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mEmailField.setError(null);
        mPasswordField.setError(null);
        mLoginPresenter.onViewDestroyed();
    }

    private boolean validateEmail() {
        return validateFormElement(mEmailField, mLoginPresenter::validateEmail);
    }

    private boolean validatePassword() {
        return validateFormElement(mPasswordField, mLoginPresenter::validatePassword);
    }

    private boolean validateFormElement(EditText formElement, Validation<String> validation) {
        String textString = formElement.getText().toString();
        ValidationResult validationResult = validation.validate(textString);
        if (!validationResult.ok) {
            formElement.setError(getResources().getString(validationResult.errorMessageID));
        }
        return validationResult.ok;
    }

}