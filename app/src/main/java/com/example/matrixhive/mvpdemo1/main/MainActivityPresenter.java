package com.example.matrixhive.mvpdemo1.main;

import android.os.Handler;

/**
 * Responsible for handling action from the view and updating the UI as required.
 */
public class MainActivityPresenter implements MainActivityContract.presenter {

    private MainActivityContract.view mView;

    public MainActivityPresenter(MainActivityContract.view mView) {
        this.mView = mView;
    }


    @Override
    public void onButtonClick(String name, String email, String contact, String image) {
        final MainActivityInteractor model = new MainActivityInteractor(name, email, contact, image);
        int isSuccessCode = model.isValidateData();

        if (isSuccessCode == 0) {
            mView.onError("you must enter your name");
        } else if (isSuccessCode == 1) {
            mView.onError("you must enter valid email");
        } else if (isSuccessCode == 2) {
            mView.onError("contact length must be greater than 10");
        } else if (isSuccessCode == 3){
            mView.onError("you must select your image");
        }else {
            mView.showProgress();
            //mView.onSuccess("success");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mView.hideProgress();
                    mView.setQuote(model.getName(), model.getEmail(), model.getContact(), model.getImage());
                }
            }, 5000);
        }
    }

    @Override
    public void showImage(String image) {
        mView.showImage(image);
    }

    @Override
    public void loginIntent() {
        mView.loginIntent();
    }

    @Override
    public void onEditClick() {
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
        if (permission.equals("gallery")) {
            mView.openImage();
        } else if (permission.equals("camera")) {
            mView.intentCamera();
        }
    }


}
