package com.example.phonebook.presenter.favorites;

import android.content.Context;

import com.example.phonebook.data.ContactRepository;

public class FavoritesListPresenter implements FavoritesListContract.Presenter {

    private FavoritesListContract.View view;
    private ContactRepository repository;
    private boolean viewChanging;
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
    public void loadContacts(Context context) {
        this.context = context;

        view.showProgressBar(true);

        view.showList(repository.getFavoriteContacts(context));

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
    public void destroy() {

    }
}
