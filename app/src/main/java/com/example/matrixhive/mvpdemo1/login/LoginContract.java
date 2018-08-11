package com.example.matrixhive.mvpdemo1.login;

import com.example.matrixhive.mvpdemo1.register.ResponseModel;

public interface LoginContract {
    interface view {
        void showProgress();

        void hideProgress();

        void registerIntent();

        void showMsg(String message);

        void showErrorMsg(String message);

        void gotoMainActivity();

        void focuseEmail();

        void focusePassword();

        void loginIntent(ResponseModel user);
    }

    interface interactor {
        void login(String email, String password, String device_token, String device_type, IOnLoginFinishedListener loginFinishedListener);


        interface IOnLoginFinishedListener {

            void getUserData(ResponseModel user);

            void getErrorMsg(String errorMsg);
        }
    }

    interface presenter {
        void onLoginClick(String email, String password, String device_token, String device_type);

        void onRegisterClick();

        void gotoMainActivity();

    }
}
