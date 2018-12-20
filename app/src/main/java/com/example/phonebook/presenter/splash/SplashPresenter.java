package com.example.phonebook.presenter.splash;

public class SplashPresenter implements SplashContract.Presenter {

    private SplashContract.View view;

    @Override
    public void initView(SplashContract.View view) {
        this.view = view;
        this.view.startMainActivity();
    }

    @Override
    public void destroy(){}
}
