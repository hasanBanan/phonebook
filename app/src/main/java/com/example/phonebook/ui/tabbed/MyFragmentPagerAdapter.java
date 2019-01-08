package com.example.phonebook.ui.tabbed;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.phonebook.ui.contacts.ContactsListFragment;
import com.example.phonebook.ui.favorites.FavoritesListFragment;

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {

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