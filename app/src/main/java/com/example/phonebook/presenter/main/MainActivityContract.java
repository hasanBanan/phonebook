package com.example.phonebook.presenter.main;

public interface MainActivityContract {

    interface View{

    }

    interface Presenter{
        void initView(MainActivityContract.View view);

        void destroy();
    }
}
