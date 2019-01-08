package com.example.phonebook.ui;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.phonebook.R;
import com.example.phonebook.ui.newContact.NewContactFragment;
import com.example.phonebook.ui.tabbed.TabbedFragment;

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
                        .add(R.id.fragment_container, new NewContactFragment())
                        .addToBackStack("list")
                        .commit();

                ((View) fab).setVisibility(View.INVISIBLE);
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ((View) fab).setVisibility(View.VISIBLE);
    }
}
