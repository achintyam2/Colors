package com.android.mms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;


public class Inbox extends AppCompatActivity {

    Toolbar myToolbar;
    ActionBar actionBar;
    Context context;
    ListView bodyList;
    ImageButton sendButton;
    EditText smsToSend;
    String smsData, phoneNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.inbox);

        context = this.getApplicationContext();

        int thread_id = intent.getIntExtra("thread", 0);
        String contactName = intent.getStringExtra("name");
        phoneNo = contactName;
        bodyList = (ListView) findViewById(R.id.bodyList);
        sendButton = (ImageButton) findViewById(R.id.send);
        smsToSend = (EditText) findViewById(R.id.smsData);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();

        getSupportActionBar().setTitle(contactName);
        myToolbar.setTitleTextColor(Color.WHITE);
        myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);


        Uri mUri = Uri.parse("content://mms-sms/conversations/" + thread_id);
//        String[] projection1 = new String[]{"tid","type", "_id", "address", "date", "body", "text_only","error_code","ct_t","msg_box","v","sub","rr","status","retr_txt_cs","ct_l","ct_cls","m_size","exp","sub_cs","st","person","tr_id","read","pri","read_status","d_rept","resp_txt","rpt_a","retr_st","m_cls","service_center"};
        String[] projection1 = new String[]{"type", "_id", "address", "date", "body", "text_only","ct_t","date_sent"};
        Cursor cursor = getContentResolver().query(mUri, projection1, null, null, "date ASC");
        cursor.moveToFirst();

        InboxAdapter inboxAdapter = new InboxAdapter(this, cursor);
        bodyList.setStackFromBottom(true);
        bodyList.setAdapter(inboxAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsData = smsToSend.getText().toString();
                if (isSmsSendingAllowed()) {
                    sendSMS();
                    return;
                }
                else
                    requestPermission();

            }
        });

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
            /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            AttachmentDIalogFragment fragment = new AttachmentDIalogFragment().newInstance();
            fragment.show(ft,"title");*/
            return true;
        } else if (i == R.id.add_to_contacts) {
            Toast.makeText(context, "You Clicked Add to contacts", Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.delete_thread) {
            Toast.makeText(context, "You Clicked delete thread", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean isSmsSendingAllowed() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    public void sendSMS() {
        Intent intent = new Intent(getApplicationContext(), Inbox.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, smsData, pi, null);
        Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();

    }

    private  void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(getApplicationContext(),"Permission granted now you can read.",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getApplicationContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }
}
