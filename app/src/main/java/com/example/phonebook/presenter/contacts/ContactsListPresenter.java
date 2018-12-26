package com.example.phonebook.presenter.contacts;


import android.content.Context;
import com.example.phonebook.data.ContactRepository;
import com.example.phonebook.domains.Contact;

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
    public void loadContacts(final Context context) {
        this.context = context;

        final List<Contact> list = repository.getAllContacts(context);

        ((ContactsListFragment)view).getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view.showProgressBar(true);

                view.showList(list);

                view.showProgressBar(true);
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
    public void dataUpdated() {
        if(viewChanging)
            loadContacts(context);
        else viewChanging = true;
    }

    @Override
    public void destroy() {}
}
