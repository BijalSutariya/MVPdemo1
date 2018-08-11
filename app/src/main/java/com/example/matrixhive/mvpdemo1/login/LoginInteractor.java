package com.example.matrixhive.mvpdemo1.login;

import android.text.TextUtils;

import com.example.matrixhive.mvpdemo1.register.ResponseModel;
import com.example.matrixhive.mvpdemo1.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginInteractor implements LoginContract.interactor {

    @Override
    public void login(String email, String password, String device_token, String device_type, final IOnLoginFinishedListener loginFinishedListener) {
        Call<ResponseModel> request = ApiClient.getRetrofit().getUser(email, password, "123456", "android");
        request.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body() != null && response.isSuccessful()) {
                    loginFinishedListener.getUserData(response.body());
                } else {
                    loginFinishedListener.getErrorMsg("Problem Create user !! Try again later.");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                loginFinishedListener.getErrorMsg("Problem Create user !! Try again later.");
            }
        });
    }



}
