package com.example.matrixhive.mvpdemo1.register;

import android.text.TextUtils;
import android.util.Log;

import static android.support.constraint.Constraints.TAG;

public class RegisterPresenter implements RegisterContract.presenter {

    private RegisterContract.view mView;
    private RegisterInteractor interactor;

    public RegisterPresenter(RegisterContract.view mView) {
        this.mView = mView;
        this.interactor = new RegisterInteractor();
    }


    @Override
    public void onRegisterClick(String name, String email, String password, String cPassword, String contact, String deviceToken, String deviceType) {
        if (TextUtils.isEmpty(name)) {
            mView.showMessage("you must enter name");
            mView.focusName();
        } else if (!isValidEmail(email)) {
            mView.showMessage("you must enter valid email");
            mView.focusEmail();
        } else if (!isValidPassword(password)) {
            mView.showMessage("you must enter valid password");
            mView.focusPassword();
        } else if (!password.equals(cPassword)) {
            mView.showMessage("Password Not matching");
            mView.focusConfirmPsw();
        } else if (!isValidMobile(contact)) {
            mView.showMessage("contact length must be greater than 10");
            mView.focusContact();
        } else {
            final RequestModel model = new RequestModel();
            model.setName(name);
            model.setEmail(email);
            model.setPassword(password);
            model.setPh_no(contact);
            model.setDevice_token("123456");
            model.setDevice_type("android");
            mView.showProgress();
            interactor.register(model, new RegisterContract.interactor.IOnRegisterFinishedListener() {
                @Override
                public void getUserData(ResponseModel user) {
                    mView.hideProgress();
                    if (!user.getStatus().equals("0")){
                        mView.successIntent(model);
                    }
                    else {
                        mView.showErrorMsg(user.getMessage());
                    }
                }

                @Override
                public void getErrorMsg(String errorMsg) {
                    mView.showProgress();
                    mView.showErrorMsg("Problem Create user !! Try again later.");
                }
            });
        }
    }

    @Override
    public void gotoMainActivity(RequestModel model) {
        mView.gotoMainActivity(model);
    }

    private static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private static boolean isValidPassword(String password) {
        return password.length() >= 6;
    }

    private static boolean isValidMobile(String phone) {
        return phone.length() == 10;
    }
}
