package com.example.phonebook.data;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;

import com.example.phonebook.data.api.ContactManager;
import com.example.phonebook.domains.Contact;
import com.example.phonebook.presenter.contacts.ContactsListContract;
import com.example.phonebook.presenter.favorites.FavoritesListContract;

import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    private static ContactRepository instance;
    private static ContactsListContract.Presenter cPresenter;
    private static FavoritesListContract.Presenter fPresenter;

    public List<Contact> getAllContacts(Context context){
        return ContactManager.getInstance().getAllContacts(context);
    }

    public List<Contact> getFavoriteContacts(Context context){
        return ContactManager.getInstance().getFavoriteContacts(context);
    }

    public void changeFavorite(Context context, int starred, long contactId){
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.STARRED, starred);
        context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, values,
                ContactsContract.Contacts._ID + "= ?", new String[] { String.valueOf(contactId) });

        fPresenter.dataUpdated();
        cPresenter.dataUpdated();
    }

    public void initPresenter(ContactsListContract.Presenter presenter){
        this.cPresenter = presenter;
    }

    public void initPresenter(FavoritesListContract.Presenter presenter){
        this.fPresenter = presenter;
    }

    public static ContactRepository getInstance(){
        if (instance == null){
            instance = new ContactRepository();
        }

        return instance;
    }
}
