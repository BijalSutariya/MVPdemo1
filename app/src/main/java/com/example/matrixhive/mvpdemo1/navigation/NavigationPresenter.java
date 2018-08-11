package com.example.matrixhive.mvpdemo1.navigation;

public class NavigationPresenter implements NavigationContract.presenter {
    private NavigationContract.view mView;

    public NavigationPresenter(NavigationContract.view mView) {
        this.mView = mView;
    }

    @Override
    public void loginIntent() {
        mView.loginIntent();
    }

    @Override
    public void displaySelectedScreen(int itemId) {
        mView.displaySelectedScreen(itemId);
    }


}
