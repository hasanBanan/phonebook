package com.example.phonebook.presenter.newContact;


import android.content.ContentUris;
import android.content.ContentValues;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.phonebook.R;
import com.example.phonebook.domains.Contact;
import com.example.phonebook.presenter.tabbed.TabbedFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewContactFragment extends Fragment implements NewContactContract.View {

    private ProgressBar mProgressBar;
    private ImageButton backArrow;
    private ImageButton saveBtn;
    private EditText nameEditText;
    private EditText phoneEditTetx;

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
        nameEditText = view.findViewById(R.id.full_name);
        phoneEditTetx = view.findViewById(R.id.phone);

        phoneEditTetx.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!nameEditText.getText().toString().isEmpty() && !phoneEditTetx.getText().toString().isEmpty()) {

                    Contact contact = new Contact(0, nameEditText.getText().toString(), phoneEditTetx.getText().toString(), null, 0);

                    mPresenter.addContact(contact, getContext());

                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, new TabbedFragment())
                            .commit();
                }else
                    Toast.makeText(getContext(), "Введите данные", Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);

        getView().setClickable(false);
    }
}
