package com.example.matrixhive.mvpdemo1.accidentDetails;

import android.net.Uri;

import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public interface AccidentConteract {
    interface view {

        void getImage();

        void selectPhoto();

        void requestPermission();

        void displayImage();

        void openImage();

        void intentCamera();

        File getOutputMediaFile() throws IOException;

        String getRealPathFromURI(Uri uri);

        void addImage(String realPathFromURI);

        void selectDate();

        void showMsg(String message);

        void hideProgress();

        void showProgress();

        void focusDate();

        void focusTitle();

        void focusName();

        void focusPhoneNo();

        void focusEmail();

        void focusDescription();

        void focusLocation();
    }

    interface interactor {

        void onProfileAdd(List<MultipartBody.Part> images, RequestBody user_id, RequestBody title, RequestBody accident_date, RequestBody accident_time,
                          RequestBody accident_type, RequestBody building_name, RequestBody location, RequestBody latitude, RequestBody longitude,
                          RequestBody address, RequestBody description, RequestBody witnessType, IOnDetailsFinishedListener detailsFinishedListener);

        interface IOnDetailsFinishedListener {

            void getUserData(Response<ResponseBody> user);

            void getErrorMsg(String errorMsg);
        }
    }

    interface presenter {

        void getImage();

        void selectPhoto();

        void onItemCLick(String option);

        void onPermissionsResult(String permission);

        void addImage(String realPathFromURI);

        void selectDate();


        void onProfileAddClick(List<AccidentImageModel> photoList, String accidentType, String date, String title, String name, String contact, String email, String location, String description);

        String getRealPathFromURI(Uri uri);
    }
}
