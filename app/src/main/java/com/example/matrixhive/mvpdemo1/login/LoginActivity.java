package com.example.matrixhive.mvpdemo1.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.matrixhive.mvpdemo1.R;
import com.example.matrixhive.mvpdemo1.navigation.NavigationActivity;
import com.example.matrixhive.mvpdemo1.register.RegisterActivity;
import com.example.matrixhive.mvpdemo1.register.ResponseModel;

public class LoginActivity extends AppCompatActivity implements LoginContract.view, View.OnClickListener {

    private EditText email, password;
    private ProgressBar progressBar;
    private LoginPresenter presenter;
    private View view;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "demo.db";
    private String strName, strContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.login);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        view = findViewById(R.id.layoutLogin);
        email = findViewById(R.id.etLoginEmail);
        password = findViewById(R.id.etLoginPassword);
        Button login = findViewById(R.id.btnLogin);
        Button registration = findViewById(R.id.btnLoginRegister);
        progressBar = findViewById(R.id.progressBar);

        presenter = new LoginPresenter(this);
        login.setOnClickListener(this);
        registration.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        strName = sharedPreferences.getString("name", null);
        strContact = sharedPreferences.getString("contact", null);

        if (sharedPreferences.getBoolean("logged", false)) {
            presenter.gotoMainActivity();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                presenter.onLoginClick(email.getText().toString(), password.getText().toString(), "123456", "android");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                break;
            case R.id.btnLoginRegister:
                presenter.onRegisterClick();
                break;
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void registerIntent() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginIntent(ResponseModel model) {
        if (model.getData().getEmail() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Log.d("TAG", "loginIntent: " + model.getData().toString());
            editor.putString("name",model.getData().getName());
            editor.putString("email", model.getData().getEmail());
            editor.putString("contact",model.getData().getPhone());
            editor.putBoolean("logged", true);
            editor.apply();
            Log.d("TAG", "loginIntent: " + model.getData().getEmail() + "\n" + model.getData().getName() + "\n" + model.getData().getPhone());
            presenter.gotoMainActivity();
        }
    }

    public void gotoMainActivity() {
        Intent intent = new Intent(this, NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void focuseEmail() {
        email.setFocusableInTouchMode(true);
        email.requestFocus();
    }

    @Override
    public void focusePassword() {
        password.setFocusableInTouchMode(true);
        password.requestFocus();
    }

    @Override
    public void showMsg(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showErrorMsg(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
