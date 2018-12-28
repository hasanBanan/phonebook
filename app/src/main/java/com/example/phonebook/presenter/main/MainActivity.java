package com.example.phonebook.presenter.main;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.phonebook.R;
import com.example.phonebook.presenter.newContact.NewContactFragment;
import com.example.phonebook.presenter.tabbed.TabbedFragment;

public class MainActivity extends AppCompatActivity{

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new TabbedFragment())
                .commit();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NewContactFragment())
                        .addToBackStack("list")
                        .commit();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
