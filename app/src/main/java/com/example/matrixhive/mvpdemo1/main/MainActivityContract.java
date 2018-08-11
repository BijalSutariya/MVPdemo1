package com.example.matrixhive.mvpdemo1.main;

import android.net.Uri;

import java.io.File;

/**
 * Defines the contract between the view{@link MainActivity} and the presenter
 * {@link MainActivityPresenter}
 */
public interface MainActivityContract {
    interface view {
        void selectPhoto();

        void requestPermission();

        void displayImage();

        void intentCamera();

        void openImage();

        File getOutputMediaFile();

        void showImage(String file);

        String getRealPathFromURI(Uri uri);

        void showProgress();

        void hideProgress();

        void onError(String message);

        void onSuccess(String message);

        void setQuote(String name, String email, String contact, String image);

        void loginIntent();


    }

    interface interactor {
        String getName();

        String getEmail();

        String getContact();

        String getImage();

        int isValidateData();

    }

    interface presenter {
        void onButtonClick(String name, String email, String contact, String image);

        void onEditClick();

        void onItemCLick(String option);

        void onPermissionsResult(String permission);

        void showImage(String file);

        void loginIntent();
    }
}

