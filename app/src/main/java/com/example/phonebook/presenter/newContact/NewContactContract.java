package com.example.phonebook.presenter.newContact;

import android.content.Context;

import com.example.phonebook.domains.Contact;

import java.util.List;

public interface NewContactContract {
    interface View{
        void showProgressBar();
    }

    interface Presenter{
        void initView(NewContactContract.View view);

        void addContact(Contact contact, Context context);

        void destroy();
    }
}
