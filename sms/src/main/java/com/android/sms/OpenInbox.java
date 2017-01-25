package com.android.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;


public class OpenInbox extends AppCompatActivity implements AttachmentDIalogFragment.DialogClickHandler {

    ListView bodyList;
    Context context;
    EditText smsToSend;
    ImageButton sendButton;
    String smsData, phoneNo, thread_id;
    Toolbar myToolbar;
    ActionBar actionBar;
    ImageView photo;
    int tid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.open_inbox);

        context = this.getApplicationContext();
        bodyList = (ListView) findViewById(R.id.bodyList);
        smsToSend = (EditText) findViewById(R.id.smsData);
        photo = (ImageView) findViewById(R.id.imageView);
        smsToSend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });
        sendButton = (ImageButton) findViewById(R.id.send);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();

        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String name = intent.getStringExtra("name");
        thread_id = intent.getStringExtra("thread_id");
        phoneNo = name;
        getSupportActionBar().setTitle(name);
        myToolbar.setTitleTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);

        Uri mUri = Uri.parse("content://mms-sms/conversations/" + thread_id);
        String[] projection1 = new String[]{"type", "_id", "date", "body", "ct_t", "date_sent", "normalized_date", "msg_box", "thread_id", "address"};

        Cursor cursor = getContentResolver().query(mUri, projection1, null, null, null);

        cursor.moveToFirst();
        tid = cursor.getInt(cursor.getColumnIndex("thread_id"));
        CustomBodyListAdapter inboxAdapter = new CustomBodyListAdapter(this, cursor);
        bodyList.setStackFromBottom(true);
        bodyList.setAdapter(inboxAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsData = smsToSend.getText().toString();
                Intent intent = new Intent(getApplicationContext(), OpenInbox.class);
                PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, smsData, pi, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                /*if(isSmsSendingAllowed())
                {

                }*/

            }
        });

    }

    private boolean isSmsSendingAllowed() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                   //Creating Menu Options
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.call) {
            Toast.makeText(context, "You Clicked Call", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.block) {
            Toast.makeText(context, "You Clicked Block", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.add_attachment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            AttachmentDIalogFragment fragment = new AttachmentDIalogFragment().newInstance();
            fragment.show(ft, "title");
            return true;
        } else if (i == R.id.add_to_contacts) {
            Toast.makeText(context, "You Clicked Add to contact", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.delete_thread) {
//            Toast.makeText(context,"You Clicked delete thread",Toast.LENGTH_SHORT).show();
            Cursor c = OpenInbox.this.getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
            c.moveToFirst();
            do {
                try {
                    int threadID = c.getInt(c.getColumnIndex("thread_id"));
                    Log.d("aa", "threadID " + threadID);
                    Log.d("aa", "tid " + tid);
                    if (threadID == tid) {
                        int rows = OpenInbox.this.getContentResolver().delete(Uri.parse("content://sms/conversations/" + threadID), null,null);
                        Toast.makeText(context, rows + " Message Deleted", Toast.LENGTH_LONG).show();
                        break;
                    }
                } catch (Exception e) {
                    Log.d("exception", "occurred" + e.getMessage());
                }
            } while (c.moveToNext());
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPhotoClicked(Bitmap bitmap) {
        photo.setVisibility(View.VISIBLE);
        photo.setImageBitmap(bitmap);
    }

    @Override
    public void onContactClicked(String contactDetails) {
        smsToSend.setText(contactDetails);
    }

    @Override
    public void onVideoClicked(final String path) {

        photo.setVisibility(View.VISIBLE);
        Log.d("aa", "Video path " + path);
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        photo.setImageBitmap(bitmap);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO send the video to the video player
            }
        });
    }

    @Override
    public void onAudioClicked(Uri uri) {
        String path = uri.getPath();
        Log.d("aa", "Audio path " + path);
        photo.setVisibility(View.GONE);


    }
}
