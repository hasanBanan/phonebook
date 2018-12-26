package com.example.phonebook.presenter.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.phonebook.data.ContactRepository;
import com.example.phonebook.data.api.ContactManager;
import com.example.phonebook.domains.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsListPresenter implements ContactsListContract.Presenter {

    private ContactsListContract.View view;
    private ContactRepository repository;
    private boolean viewChanging = true;
    private Context context;

    public ContactsListPresenter() {
        repository = ContactRepository.getInstance();
        repository.initPresenter(this);
    }

    @Override
    public void initView(ContactsListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadContacts(Context context) {
        this.context = context;

        view.showProgressBar(true);

        view.showList(repository.getAllContacts(context));

        view.showProgressBar(true);
    }

    @Override
    public void changeFavorite(Context context, int starred, long id) {
        viewChanging = false;
        repository.changeFavorite(context, starred, id);
    }

    @Override
    public void dataUpdated() {
        if(viewChanging)
            loadContacts(context);
        else viewChanging = true;
    }

    @Override
    public void destroy() {}
}
