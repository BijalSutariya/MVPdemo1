package com.example.matrixhive.mvpdemo1.login;

import android.text.TextUtils;
import android.util.Log;

import com.example.matrixhive.mvpdemo1.register.RequestModel;
import com.example.matrixhive.mvpdemo1.register.ResponseModel;

import static android.support.constraint.Constraints.TAG;

public class LoginPresenter implements LoginContract.presenter {

    private LoginContract.view mView;
    private LoginInteractor mInteractor;

    public LoginPresenter(LoginContract.view mView) {
        this.mView = mView;
        this.mInteractor = new LoginInteractor();
    }

    @Override
    public void onLoginClick(final String email, String password, String device_token, String device_type) {

        if (TextUtils.isEmpty(email)) {
            mView.showMsg("invalid email");
            mView.focuseEmail();
        } else if (!isValidPassword(password)) {
            mView.showMsg("invalid password");
            mView.focusePassword();
        }else {
            mView.showProgress();
            mInteractor.login(email, password, device_token, device_type, new LoginContract.interactor.IOnLoginFinishedListener() {
                @Override
                public void getUserData(ResponseModel user) {
                    if (!user.getStatus().equals("0")) {
                        mView.hideProgress();
                        mView.loginIntent(user);
                        Log.d(TAG, "getUserData: "+user.getData().getPhone());
                    } else {
                        mView.hideProgress();
                        mView.showErrorMsg(user.getMessage());
                    }
                }

                @Override
                public void getErrorMsg(String errorMsg) {
                    mView.hideProgress();
                    mView.showErrorMsg("Problem getting user !! Try again later.");
                }
            });
        }

    }

    @Override
    public void onRegisterClick() {
        mView.registerIntent();
    }

    @Override
    public void gotoMainActivity() {
        mView.gotoMainActivity();
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 6;
    }
}
