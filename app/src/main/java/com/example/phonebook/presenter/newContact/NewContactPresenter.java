package com.example.phonebook.presenter.newContact;

import android.content.Context;

import com.example.phonebook.data.ContactRepository;
import com.example.phonebook.domains.Contact;

public class NewContactPresenter  implements NewContactContract.Presenter {

    private NewContactContract.View view;
    private ContactRepository repository;
    private Context context;

    public NewContactPresenter() {
        this.repository = ContactRepository.getInstance();
    }

    @Override
    public void initView(NewContactContract.View view) {
        this.view = view;
    }

    @Override
    public void addContact(final Contact contact, final Context context) {
        view.showProgressBar();

        new Thread(new Runnable() {
            public void run() {
                repository.addContact(contact, context);
            }
        }).start();
    }

    @Override
    public void destroy() {

    }
}
