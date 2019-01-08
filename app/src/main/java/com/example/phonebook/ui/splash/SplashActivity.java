package com.example.phonebook.ui.splash;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.phonebook.ui.MainActivity;

public class SplashActivity extends AppCompatActivity implements SplashContract.View {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    SplashContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new SplashPresenter();
        mPresenter.initView(this);
    }

    @Override
    public void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            startMainActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMainActivity();
            } else {
                Toast.makeText(this, "Нет доступа к контактам", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.destroy();
    }
}
