package com.example.phonebook.domains;

import android.net.Uri;

public class Contact {
    long id;
    String name;
    String number;
    Uri iconUri;
    int starred;

    public Contact(long id, String name, String number, Uri iconUri, int starred) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.iconUri = iconUri;
        this.starred = starred;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public int getStarred() {
        return starred;
    }

    public void setStarred(int starred) {
        this.starred = starred;
    }
}
