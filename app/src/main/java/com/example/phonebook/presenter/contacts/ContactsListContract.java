package com.example.phonebook.presenter.contacts;

import android.content.Context;

import com.example.phonebook.domains.Contact;

import java.util.List;

public interface ContactsListContract {
    interface View{
        void showList(List<Contact> contacts);
    }

    interface Presenter{
        void initView(ContactsListContract.View view);

        void loadContacts(Context context);

        void changeFavorite(Context context, int starred, long id);

        void delete(Context context, long id);

        void dataUpdated();
    }
}
