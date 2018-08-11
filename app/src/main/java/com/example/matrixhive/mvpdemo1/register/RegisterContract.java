package com.example.matrixhive.mvpdemo1.register;

public interface RegisterContract {
    interface view {
        void showProgress();

        void hideProgress();

        void showMessage(String message);

        void showErrorMsg(String message);

        void successIntent(RequestModel email);

        void gotoMainActivity(RequestModel model);

        void focusName();

        void focusEmail();

        void focusPassword();

        void focusConfirmPsw();

        void focusContact();
    }

    interface interactor {
        void register(RequestModel model, IOnRegisterFinishedListener registerFinishedListener);

        interface IOnRegisterFinishedListener {

            void getUserData(ResponseModel user);

            void getErrorMsg(String errorMsg);
        }

    }

    interface presenter {
        void onRegisterClick(String name, String email, String password, String cPassword, String contact, String deviceToken, String deviceType);

        void gotoMainActivity(RequestModel model);
    }
}
