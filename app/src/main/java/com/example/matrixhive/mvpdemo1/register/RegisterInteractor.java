package com.example.matrixhive.mvpdemo1.register;

import android.text.TextUtils;

import com.example.matrixhive.mvpdemo1.utils.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterInteractor implements RegisterContract.interactor {

    @Override
    public void register(RequestModel model, final IOnRegisterFinishedListener registerFinishedListener) {
        Call<ResponseModel> responseModelCall = ApiClient.getRetrofit().getUserList(model);
        responseModelCall.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.body()!=null && response.isSuccessful()){
                    registerFinishedListener.getUserData(response.body());
                }
                else {
                    registerFinishedListener.getErrorMsg("Problem Create user !! Try again later.");
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                registerFinishedListener.getErrorMsg("Problem Create user !! Try again later.");
            }
        });
    }
}
