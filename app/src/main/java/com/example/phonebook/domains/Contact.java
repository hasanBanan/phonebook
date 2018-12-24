package com.example.phonebook.domains;

import android.net.Uri;

public class Contact {
    public String name;
    public String number;
    public Uri iconUri;
    public String starred;

    public Contact(String name, String number, Uri iconUri, String starred) {
        this.name = name;
        this.number = number;
        this.iconUri = iconUri;
        this.starred = starred;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Uri getIconUri() {
        return iconUri;
    }

    public void setIconUri(Uri iconUri) {
        this.iconUri = iconUri;
    }

    public String getStarred() {
        return starred;
    }

    public void setStarred(String starred) {
        this.starred = starred;
    }
}
