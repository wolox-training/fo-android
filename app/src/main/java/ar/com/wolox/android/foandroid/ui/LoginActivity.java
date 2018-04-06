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

import ar.com.wolox.android.foandroid.TrainingApplication;
import ar.com.wolox.android.foandroid.model.User;
import ar.com.wolox.android.foandroid.validations.EmailFormatValidation;
import ar.com.wolox.android.foandroid.validations.EmptyEmailValidation;
import ar.com.wolox.android.foandroid.validations.EmptyPasswordValidation;
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

    private List<Validation<String>> emailValidationList = buildEmailValidationList();
    private List<Validation<String>> passwordValidationList = buildPasswordValidationList();

    private LoginPresenter mLoginPresenter;

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

        mLoginPresenter = new LoginPresenter(this, TrainingApplication.RETROFIT_SERVICES_INSTANCE);
        mLoginPresenter.onViewCreated();
    }

    @Override
    protected int layout() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.activity_login_login_button)
    void onLoginClick() {

        if (validateEmail() & validatePassword()) {

            mLoginPresenter.login(mEmailField.getText().toString(), mPasswordField.getText().toString());
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

    @Override
    public void onLoginSuccessful(User user) {
        ToastUtils.show("Login successful, " + user.getName());
    }

    @Override
    public void onLoginFailed() {
        ToastUtils.show(R.string.login_failed_error);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEmailField.setError(null);
        mPasswordField.setError(null);
        mLoginPresenter.onViewDestroyed();
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

    private List<Validation<String>> buildPasswordValidationList() {
        List<Validation<String>> list = new LinkedList<>();
        list.add(new EmptyPasswordValidation(this));
        return list;
    }


}