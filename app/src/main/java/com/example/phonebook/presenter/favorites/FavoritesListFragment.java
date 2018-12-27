package com.example.phonebook.presenter.favorites;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesListFragment extends Fragment implements FavoritesListContract.View {


    private FavoritesListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private FavoritesListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new FavoritesListPresenter();
        mPresenter.initView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        mRecyclerView = view.findViewById(R.id.contacts_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mPresenter.loadContacts(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void showProgressBar(Boolean show) {
        Log.d(this.getTag(), "showProgressBar");

    }

    @Override
    public void showList(List<Contact> contacts) {
        mAdapter = new FavoritesListAdapter(contacts, mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        Log.d(this.getTag(), contacts.toString());
    }
}
