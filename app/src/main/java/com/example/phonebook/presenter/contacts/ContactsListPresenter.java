package com.example.phonebook.presenter.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.example.phonebook.data.ContactRepository;
import com.example.phonebook.domains.Contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContactsListPresenter implements ContactsListContract.Presenter {

    private ContactsListContract.View view;
    private ContactRepository repository;

    public ContactsListPresenter() {
        repository = new ContactRepository();
    }

    @Override
    public void initView(ContactsListContract.View view) {
        this.view = view;
    }

    @Override
    public void loadContacts(Context context) {
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

            view.showProgressBar(false);
            view.showList(list);
        }
    }

    @Override
    public void loadFavoritesContacts(Context context) {
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

            view.showProgressBar(false);
            view.showList(list);
        }
    }

    @Override
    public void changeFavorite(Context context, int starred, long id) {
        repository.changeFavorite(context, starred, id);
    }

    @Override
    public void destroy() {}
}
