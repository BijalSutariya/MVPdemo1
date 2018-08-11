package com.example.matrixhive.mvpdemo1.navigation;

public interface NavigationContract {

    interface view {
        void loginIntent();

        void displaySelectedScreen(int itemId);

    }

    interface interactor {
    }

    interface presenter {
        void loginIntent();

        void displaySelectedScreen(int itemId);

    }
}
