package com.example.matrixhive.mvpdemo1.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
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

public class RegisterActivity extends AppCompatActivity implements RegisterContract.view, View.OnClickListener {

    private EditText name, email, psw, confirmPsw, contact;
    private View view;
    private ProgressBar progressBar;
    private RegisterPresenter presenter;
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "demo.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        view = findViewById(R.id.layoutRegister);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.registration);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        name = findViewById(R.id.etProfileName);
        email = findViewById(R.id.etRegistrationEmail);
        psw = findViewById(R.id.etRegistrationPsw);
        confirmPsw = findViewById(R.id.etRegistrationConfirmPsw);
        contact = findViewById(R.id.etRegistrationContact);
        Button register = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.circularProgressBar);
        presenter = new RegisterPresenter(this);
        register.setOnClickListener(this);

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        RequestModel model = getIntent().getParcelableExtra("profile");

        if (sharedPreferences.getBoolean("logged", false)){
            gotoMainActivity(model);
        }
    }

    @Override
    public void onClick(View v) {
        presenter.onRegisterClick(name.getText().toString(), email.getText().toString(), psw.getText().toString(),
                confirmPsw.getText().toString(), contact.getText().toString(), "Android", "123456");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showErrorMsg(String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void successIntent(RequestModel model) {
        if (model.getEmail() != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name",model.getName());
            editor.putString("email", model.getEmail());
            editor.putString("contact",model.getPh_no());
            editor.putBoolean("logged", true);
            editor.apply();
            presenter.gotoMainActivity(model);
        }
    }


    public void gotoMainActivity(RequestModel model) {
        Intent intent = new Intent(RegisterActivity.this,NavigationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void focusName() {
        name.setFocusableInTouchMode(true);
        name.requestFocus();
    }

    @Override
    public void focusEmail() {
        email.setFocusableInTouchMode(true);
        email.requestFocus();
    }

    @Override
    public void focusPassword() {
        psw.setFocusableInTouchMode(true);
        psw.requestFocus();
    }

    @Override
    public void focusConfirmPsw() {
        confirmPsw.setFocusableInTouchMode(true);
        confirmPsw.requestFocus();
    }

    @Override
    public void focusContact() {
        contact.setFocusableInTouchMode(true);
        contact.requestFocus();
    }
}
