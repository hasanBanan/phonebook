package com.example.phonebook.presenter.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.phonebook.R;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}
