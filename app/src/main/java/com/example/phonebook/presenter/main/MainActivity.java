package com.example.phonebook.presenter.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.phonebook.R;
import com.example.phonebook.presenter.tabbed.TabbedFragment;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new TabbedFragment())
                .addToBackStack("test")
                .commit();

    }

}
