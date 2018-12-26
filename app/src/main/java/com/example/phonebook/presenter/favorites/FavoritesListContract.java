package com.example.phonebook.presenter.favorites;

import android.content.Context;

import com.example.phonebook.domains.Contact;

import java.util.List;

public interface FavoritesListContract {
    interface View{
        void showProgressBar(Boolean show);

        void showList(List<Contact> contacts);
    }

    interface Presenter{
        void initView(FavoritesListContract.View view);

        void loadContacts(Context context);

        void changeFavorite(Context context, int starred, long id);

        void dataUpdated();

        void destroy();
    }
}
