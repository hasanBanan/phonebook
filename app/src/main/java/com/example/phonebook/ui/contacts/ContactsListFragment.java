package com.example.phonebook.ui.contacts;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsListFragment extends Fragment implements ContactsListContract.View {

    private ContactsListContract.Presenter mPresenter;
    private RecyclerView mRecyclerView;
    private ProgressBar progressBar;
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

        View fab = getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        mRecyclerView = view.findViewById(R.id.contacts_list);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext()){
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                try {
                    super.onLayoutChildren(recycler, state);
                } catch (IndexOutOfBoundsException e) {
                    Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens");
                }
            }
        };
        mRecyclerView.setLayoutManager(layoutManager);

        progressBar = view.findViewById(R.id.progress_bar);

        mPresenter.loadContacts(getContext());

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.divider));
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        registerForContextMenu(mRecyclerView);

        return view;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getGroupId() == 0) {

            mPresenter.delete(getContext(), mAdapter.list.get(item.getItemId()).getId());

            mAdapter.list.remove(item.getItemId());
            mAdapter.notifyItemChanged(item.getItemId());
            mAdapter.notifyItemRangeChanged(item.getItemId(), mAdapter.getItemCount());
        }
        return super.onContextItemSelected(item);
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
