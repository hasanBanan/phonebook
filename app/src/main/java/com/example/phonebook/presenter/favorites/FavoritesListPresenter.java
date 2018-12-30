package com.example.phonebook.presenter.favorites;

import android.content.Context;

import com.example.phonebook.data.ContactRepository;
import com.example.phonebook.domains.Contact;
import com.example.phonebook.presenter.contacts.ContactsListFragment;

import java.util.List;

public class FavoritesListPresenter implements FavoritesListContract.Presenter {

    private FavoritesListContract.View view;
    private ContactRepository repository;
    private boolean viewChanging = true;
    private Context context;

    public FavoritesListPresenter() {
        this.repository = ContactRepository.getInstance();
        repository.initPresenter(this);
    }

    @Override
    public void initView(FavoritesListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadContacts(final Context context) {
        this.context = context;

        final List<Contact> list = repository.getFavoriteContacts(context);

        ((FavoritesListFragment)view).getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.showList(list);
            }
        });

    }

    @Override
    public void changeFavorite(final Context context, final int starred, final long id) {
        viewChanging = false;

        Thread t = new Thread(new Runnable() {
            public void run() {
                repository.changeFavorite(context, starred, id);
            }
        });

        t.start();
    }

    @Override
    public void delete(final Context context, final long id) {
        viewChanging = false;

        Thread t = new Thread(new Runnable() {
            public void run() {
                repository.delete(context, id);
            }
        });

        t.start();
    }

    @Override
    public void dataUpdated() {
        if(viewChanging)
            loadContacts(context);
        else viewChanging = true;
    }
}
