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
        ContactManager.getInstance().changeFavorite(context, starred, contactId);

        fPresenter.dataUpdated();
        cPresenter.dataUpdated();
    }

    public void addContact(Contact contact, Context context){
        ContactManager.getInstance().addContact(contact, context);
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
