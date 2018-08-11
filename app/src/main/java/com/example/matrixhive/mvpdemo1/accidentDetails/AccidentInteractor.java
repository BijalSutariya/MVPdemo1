package com.example.matrixhive.mvpdemo1.accidentDetails;

import com.example.matrixhive.mvpdemo1.utils.ApiClient;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AccidentInteractor implements AccidentConteract.interactor {

    @Override
    public void onProfileAdd(List<MultipartBody.Part> images, RequestBody user_id, RequestBody title, RequestBody accident_date,
                             RequestBody accident_time, RequestBody accident_type, RequestBody building_name, RequestBody location,
                             RequestBody latitude, RequestBody longitude, RequestBody address, RequestBody description,
                             RequestBody witnessType, final IOnDetailsFinishedListener detailsFinishedListener) {
        Call<ResponseBody> call = ApiClient.getRetrofit().uploadFile(images, user_id, title, accident_date, accident_time, accident_type,
                building_name, location, latitude, longitude, address, description, witnessType);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body()!=null && response.isSuccessful()){
                    detailsFinishedListener.getUserData(response);
                }
                else {
                    detailsFinishedListener.getErrorMsg("Try again later.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                detailsFinishedListener.getErrorMsg("Try again later.");
            }
        });
    }
}
