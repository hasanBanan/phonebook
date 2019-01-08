package com.example.phonebook.ui.favorites;

import android.content.Context;

import com.example.phonebook.domains.Contact;

import java.util.List;

public interface FavoritesListContract {
    interface View{
        void showList(List<Contact> contacts);
    }

    interface Presenter{
        void initView(FavoritesListContract.View view);

        void loadContacts(Context context);

        void changeFavorite(Context context, int starred, long id);

        void delete(Context context, long id);

        void dataUpdated();
    }
}
