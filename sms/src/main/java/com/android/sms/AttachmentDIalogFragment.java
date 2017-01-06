package com.android.sms;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import static android.app.Activity.RESULT_OK;

public class AttachmentDIalogFragment extends DialogFragment {

    RadioButton photos,contacts;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CONTACT = 2;
    private String selectedImagePath;
    DialogClickHandler call;

    public AttachmentDIalogFragment(){}

    public interface DialogClickHandler {         //Creating a interface to communicate between the fragments
        void onPhotoClicked(Uri uri);
        void onContactClicked(String contactDetails);
    }

    public static AttachmentDIalogFragment newInstance() {
        AttachmentDIalogFragment fragment = new AttachmentDIalogFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.attachment_dialog, container, false);

        getDialog().setTitle("Select Attachment type");

        photos = (RadioButton) rootView.findViewById(R.id.photos);
        contacts = (RadioButton) rootView.findViewById(R.id.contacts);

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://contacts");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, SELECT_CONTACT);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                call = (DialogClickHandler) getActivity();
                call.onPhotoClicked(selectedImageUri);
                dismiss();
            }
            else if (requestCode == SELECT_CONTACT)
            {
                Uri uri = data.getData();
                String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };

                Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
                cursor.moveToFirst();

                int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumnIndex);

                int nameColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumnIndex);
                String contact = name +"\n"+number;
                call = (DialogClickHandler) getActivity();
                call.onContactClicked(contact);
                dismiss();
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(projection[0]);

        return cursor.getString(column_index);
    }

}
