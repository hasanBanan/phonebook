package com.example.phonebook.presenter.contacts;

import android.content.Context;

import com.example.phonebook.domains.Contact;

import java.util.List;

public interface ContactsListContract {

    interface View{
        void showProgressBar(Boolean show);

        void showList(List<Contact> contacts);
    }

    interface Presenter{
        void initView(ContactsListContract.View view);

        void loadContacts(Context context);

        void loadFavoritesContacts(Context context);

        void destroy();
    }
}
