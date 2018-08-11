package com.example.matrixhive.mvpdemo1.accidentDetails;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.matrixhive.mvpdemo1.accidentDetails.imageDetails.AccidentImageModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class AccidentPresenter implements AccidentConteract.presenter {
    private AccidentConteract.view mView;
    private AccidentInteractor interactor;

    public AccidentPresenter(AccidentConteract.view mView) {
        this.mView = mView;
        this.interactor = new AccidentInteractor();
    }

    @Override
    public void selectPhoto() {
        mView.selectPhoto();
    }

    @Override
    public void onItemCLick(String option) {
        if (option.equals("Take Photo")) {
            mView.requestPermission();
        } else if (option.equals("Choose photo")) {
            mView.displayImage();
        }
    }

    @Override
    public void onPermissionsResult(String permission) {
        if (permission.equals("camera")) {
            mView.intentCamera();
        } else if (permission.equals("gallery")) {
            mView.openImage();
        }
    }

    @Override
    public void getImage() {
        mView.getImage();
    }

    @Override
    public void addImage(String realPathFromURI) {
        mView.addImage(realPathFromURI);
    }

    @Override
    public void selectDate() {
        mView.selectDate();
    }

    @Override
    public void onProfileAddClick(List<AccidentImageModel> photoList, String accidentType, String date, String title, String name,
                                  String contact, String email, String location, String description) {

        if (TextUtils.isEmpty(date)) {
            mView.showMsg("you must select date");
            mView.focusDate();
        } else if (TextUtils.isEmpty(title)) {
            mView.showMsg("you must enter title");
            mView.focusTitle();
        } else if (TextUtils.isEmpty(name)) {
            mView.showMsg("you must enter your name");
            mView.focusName();
        } else if (!isValidMobile(contact)) {
            mView.showMsg("contact length must be greater than 10");
            mView.focusPhoneNo();
        } else if (!isValidEmail(email)) {
            mView.showMsg("you must enter valid email");
            mView.focusEmail();
        } else if (TextUtils.isEmpty(location)) {
            mView.showMsg("you must enter location");
            mView.focusLocation();
        } else if (TextUtils.isEmpty(description)) {
            mView.showMsg("you must enter accident details");
            mView.focusDescription();
        } else {
            mView.showProgress();
            MultipartBody.Part images;
            List<MultipartBody.Part> parts = new ArrayList<>();
            for (int i = 0; i < photoList.size(); i++) {

                if (photoList.get(i).getImages() != null) {
                    File file = new File(photoList.get(i).getImages());
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
                    images = MultipartBody.Part.createFormData("images[" + i + "]", file.getName(), requestFile);
                    parts.add(images);
                }
                Log.d("TAG+", "onClick: " + parts);
            }
            RequestBody user_id = RequestBody.create(MultipartBody.FORM, "51");
            RequestBody titles = RequestBody.create(MultipartBody.FORM, title);
            RequestBody accident_date = RequestBody.create(MultipartBody.FORM, date);
            RequestBody accident_time = RequestBody.create(MultipartBody.FORM, "2:30 PM");
            RequestBody accident_type = RequestBody.create(MultipartBody.FORM, accidentType);
            RequestBody building_name = RequestBody.create(MultipartBody.FORM, "Test");
            RequestBody locations = RequestBody.create(MultipartBody.FORM, location);
            RequestBody latitude = RequestBody.create(MultipartBody.FORM, "27.5555");
            RequestBody longitude = RequestBody.create(MultipartBody.FORM, "72.2323");
            RequestBody address = RequestBody.create(MultipartBody.FORM, "demo");
            RequestBody descriptions = RequestBody.create(MultipartBody.FORM, description);
            RequestBody witnessType = RequestBody.create(MultipartBody.FORM, "witness");
            interactor.onProfileAdd(parts, user_id, titles, accident_date, accident_time, accident_type,
                    building_name, locations, latitude, longitude, address, descriptions, witnessType,
                    new AccidentConteract.interactor.IOnDetailsFinishedListener() {
                        @Override
                        public void getUserData(Response<ResponseBody> user) {
                            if (user.isSuccessful()) {
                                mView.hideProgress();
                            } else {
                                mView.showMsg(user.message());
                            }
                        }

                        @Override
                        public void getErrorMsg(String errorMsg) {
                            mView.showMsg(errorMsg);
                        }
                    });

        }
    }

    @Override
    public String getRealPathFromURI(Uri uri) {
        return mView.getRealPathFromURI(uri);
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private static boolean isValidMobile(String phone) {
        return phone.length() == 10;
    }

}
