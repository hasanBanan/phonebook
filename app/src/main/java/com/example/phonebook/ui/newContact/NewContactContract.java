package com.example.phonebook.ui.newContact;

import android.content.Context;

import com.example.phonebook.domains.Contact;

public interface NewContactContract {
    interface View{
        void openList();

        void showProgressBar();
    }

    interface Presenter{
        void initView(NewContactContract.View view);

        void addContact(Contact contact, Context context);

        void orderOpenList();
    }
}
