package com.example.phonebook.ui.tabbed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phonebook.R;


public class TabbedFragment extends Fragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabbed, container, false);

        ViewPager viewPager = view.findViewById(R.id.viewpager);

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(this.getContext(), getFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = view.findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
