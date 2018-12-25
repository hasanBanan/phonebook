package com.example.phonebook.data;

import android.content.ContentValues;
import android.content.Context;
import android.provider.ContactsContract;

public class ContactRepository {

    public void changeFavorite(Context context, int starred, long contactId){
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.STARRED, starred);
        context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, values,
                ContactsContract.Contacts._ID + "= ?", new String[] { String.valueOf(contactId) });
    }
}
