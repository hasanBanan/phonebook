package com.example.phonebook.presenter.tabbed;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.phonebook.R;
import com.example.phonebook.presenter.contacts.ContactsListFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ContactsListFragment();
            case 1:
                return new ContactsListFragment();
            default:
                return null;
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Контакты";
            case 1:
                return "Избранное";
            default:
                return null;
        }
    }

    enum TypeList {
        ALL, ELECT
    }
}