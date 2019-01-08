package com.example.phonebook.ui.splash;

public interface SplashContract {
    interface View{
        void checkPermission();

        void startMainActivity();
    }

    interface Presenter{
        void initView(SplashContract.View view);

        void destroy();
    }
}
