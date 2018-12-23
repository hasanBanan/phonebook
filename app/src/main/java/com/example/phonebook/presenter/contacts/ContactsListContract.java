package com.example.phonebook.presenter.contacts;

import android.content.Context;

import java.util.List;

public interface ContactsListContract {

    interface View{
        void showProgressBar(Boolean show);

        void loadData();

        void showList(List<String> contacts);
    }

    interface Presenter{
        void initView(ContactsListContract.View view);

        void getContacts();

        void loadData(Context context);

        void destroy();
    }
}
