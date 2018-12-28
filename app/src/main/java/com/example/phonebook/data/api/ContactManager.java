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
            iStream = context.getContentResolver().openInputStream(contact.getIconUri());
            inputData = getBytes(iStream);
        } catch (Exception e) {
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

        if(contact.getIconUri() != null) {
            ContentValues contentValues = new ContentValues();
            // Set raw contact id. Data table only has raw_contact_id.
            contentValues.put(ContactsContract.Data.RAW_CONTACT_ID, contact.getId());
            // Set mimetype first.
            contentValues.put(ContactsContract.CommonDataKinds.Photo.MIMETYPE, ContactsContract.CommonDataKinds.Photo.CONTENT_ITEM_TYPE);
            // Set photo
            contentValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO, inputData);
            // Set photo file id.
//            contentValues.put(ContactsContract.CommonDataKinds.Photo.PHOTO_FILE_ID, contact.getPhotoFieldId());

            // Insert to data table.
            context.getContentResolver().insert(ContactsContract.Data.CONTENT_URI, contentValues);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static ContactManager getInstance(){
        if (instance == null)
            instance = new ContactManager();

        return instance;
    }

}
