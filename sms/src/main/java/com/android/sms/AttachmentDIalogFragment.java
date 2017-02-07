package com.android.sms;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class AttachmentDIalogFragment extends DialogFragment {

    RadioButton photo, contact, video,audio;
    private static final int SELECT_PICTURE = 1;
    private static final int SELECT_CONTACT = 2;
    private static final int SELECT_VIDEO = 3;
    private static final int SELECT_AUDIO = 4;

    DialogClickHandler call;
    Bitmap bitmapPhoto,bitmapVideo;

    public AttachmentDIalogFragment(){}

    public interface DialogClickHandler {         //Creating a interface to communicate between the fragments
        void onPhotoClicked(Bitmap bitmap);
        void onContactClicked(String contactDetails);
        void onVideoClicked(String path);
        void onAudioClicked(Uri uri);
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

        photo = (RadioButton) rootView.findViewById(R.id.photos);
        contact = (RadioButton) rootView.findViewById(R.id.contacts);
        video = (RadioButton) rootView.findViewById(R.id.videos);
        audio = (RadioButton) rootView.findViewById(R.id.audios);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("content://contact");
                Intent intent = new Intent(Intent.ACTION_PICK, uri);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, SELECT_CONTACT);

            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"), SELECT_VIDEO);
            }
        });

        audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("audio/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Audio "),SELECT_AUDIO);
            }
        });

        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageUri = data.getData();
                try {
                    bitmapPhoto = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String selectedImagePath = getImagePath(selectedImageUri);
                call = (DialogClickHandler) getActivity();
                call.onPhotoClicked(bitmapPhoto);
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
            else if (requestCode == SELECT_VIDEO)
            {
                if(data.getData()!=null)
                {
                    Uri uri = data.getData();

                    String selectedVideoPath = getVideoPath(data.getData());
                    call = (DialogClickHandler) getActivity();
                    call.onVideoClicked(selectedVideoPath);
                    dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Failed to select video" , Toast.LENGTH_LONG).show();
                }
            }
            else if (requestCode == SELECT_AUDIO)
            {
                if(data.getData()!=null)
                {
                    Uri uri = data.getData();
                    call = (DialogClickHandler) getActivity();
                    call.onAudioClicked(uri);
                    dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Failed to select audio" , Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public String getImagePath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(projection[0]);
        return cursor.getString(column_index);
    }
    public String getVideoPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(projection[0]);
        return cursor.getString(column_index);
    }

}
