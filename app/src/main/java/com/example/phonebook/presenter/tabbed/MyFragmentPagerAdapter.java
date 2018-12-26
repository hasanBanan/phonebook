package com.example.phonebook.presenter.tabbed;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import android.os.Bundle;
import com.example.phonebook.presenter.contacts.ContactsListFragment;
import com.example.phonebook.presenter.favorites.FavoritesListFragment;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:{
                return new ContactsListFragment();
            }
            case 1:{
                return new FavoritesListFragment();
            }
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Контакты";
            case 1:
                return "Избранные";
            default:
                return null;
        }
    }
}