package com.example.phonebook.data.api;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import com.example.phonebook.domains.Contact;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactManager {

    private static ContactManager instance;

    public List<Contact> getAllContacts(Context context){
        List<Contact> list = new ArrayList();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                long id = cur.getLong(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));
                int starred = cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.STARRED));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        list.add(new Contact(id, name, phoneNo, pURI, starred));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        if(!list.isEmpty()){
            Collections.sort(list, new Comparator<Contact>()
            {
                @Override
                public int compare(Contact contact1, Contact contact2)
                {
                    return contact1.getName().compareToIgnoreCase(contact2.getName());
                }
            });
        }
        return list;
    }

    public List<Contact> getFavoriteContacts(Context context){
        List<Contact> list = new ArrayList();

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, ContactsContract.Contacts.STARRED + "='1'", null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                long id = cur.getLong(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {

                    Uri person = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, new Long(id));
                    Uri pURI = Uri.withAppendedPath(person, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);

                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        list.add(new Contact(id, name, phoneNo, pURI, 1));
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }

        if(!list.isEmpty()){
            Collections.sort(list, new Comparator<Contact>()
            {
                @Override
                public int compare(Contact contact1, Contact contact2)
                {
                    return contact1.getName().compareToIgnoreCase(contact2.getName());
                }
            });
        }

        return list;
    }

    public void delete(Context context, long id){

        ContentResolver cr = context.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, ContactsContract.Contacts._ID + " = ?", new String[]{String.valueOf(id)}, null);

        while (cur.moveToNext())
        {
            String lookupKey = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

            cr.delete(uri, null, null);
        }
    }

    public void changeFavorite(Context context, int starred, long contactId) {
        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.STARRED, starred);
        context.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, values,
                ContactsContract.Contacts._ID + "= ?", new String[]{String.valueOf(contactId)});
    }

    public void addContact(Contact contact, Context context){

        ArrayList<ContentProviderOperation> ops = new ArrayList();
        int rawContactID = ops.size();

        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getName())
                .build());

        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) rawContactID)
                .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        if(contact.getIconUri() != null)
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, (int) rawContactID)
                    .withValue(ContactsContract.Data.IS_SUPER_PRIMARY, 1)
                    .withValue(ContactsContract.Data.MIMETYPE,ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Photo.PHOTO,contact.getImageByteCode())
                    .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ContactManager getInstance(){
        if (instance == null)
            instance = new ContactManager();

        return instance;
    }

}
