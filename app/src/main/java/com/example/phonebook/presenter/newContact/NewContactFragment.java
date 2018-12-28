package com.example.phonebook.presenter.newContact;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;
import com.example.phonebook.presenter.tabbed.TabbedFragment;

import java.io.File;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewContactFragment extends Fragment implements NewContactContract.View {

    private ProgressBar mProgressBar;
    private ImageButton backArrow;
    private ImageButton saveBtn;
    private ImageView contactsPhoto;
    private EditText nameEditText;
    private EditText phoneEditText;

    Uri selectedImageUri = null;

    private NewContactContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new NewContactPresenter();
        mPresenter.initView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_new_contact, container, false);

        mProgressBar = view.findViewById(R.id.progress_bar);
        backArrow = view.findViewById(R.id.back_btn);
        saveBtn = view.findViewById(R.id.save_btn);
        contactsPhoto = view.findViewById(R.id.contact_photo);
        nameEditText = view.findViewById(R.id.full_name);
        phoneEditText = view.findViewById(R.id.phone);

        contactsPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                view.setEnabled(true);
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), 1);
            }
        });

        phoneEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameEditText.getText().toString().isEmpty() && !phoneEditText.getText().toString().isEmpty()) {

                    Contact contact = new Contact(0, nameEditText.getText().toString(), phoneEditText.getText().toString(), selectedImageUri, 0);

                    mPresenter.addContact(contact, getContext());

                    getView().setEnabled(true);

//                    getActivity().getSupportFragmentManager().beginTransaction()
//                            .addToBackStack(null)
//                            .replace(R.id.fragment_container, new TabbedFragment())
//                            .commit();
                }else
                    Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            super.onActivityResult(requestCode, resultCode, data);

            selectedImageUri = data.getData();

            contactsPhoto.setImageURI(selectedImageUri);

//        contactsPhoto.setImageURI(selectedImageUri);
        }
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

        getView().setClickable(false);
    }
}
