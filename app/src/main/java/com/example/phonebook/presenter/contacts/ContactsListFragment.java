package com.example.phonebook.presenter.contacts;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;
import com.example.phonebook.presenter.tabbed.MyFragmentPagerAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsListFragment extends Fragment implements ContactsListContract.View {

    private ContactsListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ContactsListAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ContactsListPresenter();
        mPresenter.initView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        mRecyclerView = view.findViewById(R.id.contacts_list);
//        mRecyclerView.setHasFixedSize(true);
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
        mAdapter = new ContactsListAdapter(contacts, mPresenter);
        mRecyclerView.setAdapter(mAdapter);
        Log.d(this.getTag(), contacts.toString());
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
