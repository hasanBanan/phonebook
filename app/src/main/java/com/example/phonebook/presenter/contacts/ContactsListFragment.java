package com.example.phonebook.presenter.contacts;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.phonebook.R;
import com.example.phonebook.presenter.tabbed.MyFragmentPagerAdapter;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsListFragment extends Fragment implements ContactsListContract.View {

    ContactsListContract.Presenter mPresenter;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new ContactsListPresenter();
        mPresenter.initView(this);
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts_list, container, false);
    }

    @Override
    public void showProgressBar(Boolean show) {
        Log.d(this.getTag(), "showProgressBar");

    }

    @Override
    public void loadData() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && getContext().checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            mPresenter.loadData(this.getContext());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mPresenter.loadData(this.getContext());
            } else {
                Toast.makeText(this.getContext(), "Нет доступа к контактам", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void showList(List<String> contacts) {
        Log.d(this.getTag(), "showList");
    }
}
