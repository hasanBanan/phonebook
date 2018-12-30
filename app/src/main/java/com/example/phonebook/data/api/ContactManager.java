package com.example.phonebook.data.api;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
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
        ContentValues valuess = new ContentValues();
        Uri rawContactUri = context.getContentResolver().insert(ContactsContract.RawContacts.CONTENT_URI, valuess);
// Get the newly created contact raw id.
        long ret = ContentUris.parseId(rawContactUri);

        InputStream iStream = null;
        byte[] inputData = null;
        try {
            inputData = getBytes(contact.getIconUri());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, ret);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getNumber());
        values.put(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM);
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);

        values.put(ContactsContract.Contacts.Data.RAW_CONTACT_ID, ret);
        values.put(ContactsContract.Contacts.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);
        values.put(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME, contact.getName());
        context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, values);


        int photoRow = -1;
        String where = ContactsContract.Data.RAW_CONTACT_ID + " = " + contact.getId() + " AND " + ContactsContract.Data.MIMETYPE + "=='" + ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE + "'";
        Cursor cursor = context.getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, where, null, null);
        int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Data._ID);
        if (cursor.moveToFirst()) {
            photoRow = cursor.getInt(idIdx);
        }
        cursor.close();

        if(contact.getIconUri() != null) {
            values = new ContentValues();
            // Set raw contact id. Data table only has raw_contact_id.
            values.put(ContactsContract.Data.RAW_CONTACT_ID, contact.getId());
            values.put(ContactsContract.Data.IS_SUPER_PRIMARY, 1);
            values.put(ContactsContract.CommonDataKinds.Photo.PHOTO, inputData);
            values.put(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);

            // Insert to data table.
            context.getContentResolver().update(ContactsContract.Data.CONTENT_URI, values, ContactsContract.Data._ID + " = " + photoRow, null);
        }
    }

    public byte[] getBytes(Uri data) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(data.getPath()));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bbytes = baos.toByteArray();
        return bbytes;
    }

    public static ContactManager getInstance(){
        if (instance == null)
            instance = new ContactManager();

        return instance;
    }

}
