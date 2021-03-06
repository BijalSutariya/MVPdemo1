package com.example.matrixhive.mvpdemo1.utils;

import com.example.matrixhive.mvpdemo1.register.RequestModel;
import com.example.matrixhive.mvpdemo1.register.ResponseModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @POST("register")
    Call<ResponseModel> getUserList(@Body RequestModel model);

    @FormUrlEncoded
    @POST("login")
    Call<ResponseModel> getUser(@Field("email") String email,
                            @Field("password") String password,
                            @Field("device_token") String device_token,
                            @Field("device_type") String device_type);

    @Multipart
    @POST("addwitness")
    Call<ResponseBody> uploadFile(

            @Part List<MultipartBody.Part> images,
            @Part("user_id") RequestBody user_id,
            @Part("title") RequestBody title,
            @Part("accident_date") RequestBody date,
            @Part("accident_time") RequestBody time,
            @Part("accident_type") RequestBody accidentType,
            @Part("building_name") RequestBody buildingName,
            @Part("location") RequestBody locatio,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("address") RequestBody address,
            @Part("description") RequestBody description,
            @Part("witness_type[]") RequestBody witness
    );
}
