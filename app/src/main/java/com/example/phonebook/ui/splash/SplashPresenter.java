package com.example.phonebook.ui.splash;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;

    @Override
    public void initView(SplashContract.View view) {
        this.view = view;
        this.view.checkPermission();
    }

    @Override
    public void destroy(){}
}
