package com.example.phonebook.presenter.splash;

public interface SplashContract {
    interface View{
        void startMainActivity();
    }

    interface Presenter{
        void initView(SplashContract.View view);

        void destroy();
    }
}
