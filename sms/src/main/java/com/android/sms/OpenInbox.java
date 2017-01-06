package com.android.sms;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class OpenInbox extends AppCompatActivity {

    ListView bodyList;
    Context context;
    EditText smsToSend;
    ImageButton sendButton;
    String smsData, phoneNo;
    Toolbar myToolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.open_inbox);

        context = this.getApplicationContext();
        bodyList = (ListView) findViewById(R.id.bodyList);
        smsToSend = (EditText) findViewById(R.id.smsData);
        sendButton = (ImageButton) findViewById(R.id.send);
        myToolbar = (Toolbar)findViewById(R.id.toolbar);
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
        phoneNo = name;
        getSupportActionBar().setTitle(name);
        myToolbar.setTitleTextColor(Color.WHITE);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_more_vert_white_24dp);
        myToolbar.setOverflowIcon(drawable);

        HashMap<String, ArrayList<String>> nameAndBody = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra("nameAndBody");
        ArrayList<String> bodies = nameAndBody.get(name);

        HashMap<String, ArrayList<Long>> nameAndTime = (HashMap<String, ArrayList<Long>>) intent.getSerializableExtra("nameAndTime");
        ArrayList<Long> times = nameAndTime.get(name);

        HashMap<String, ArrayList<Integer>> nameAndType = (HashMap<String, ArrayList<Integer>>) intent.getSerializableExtra("nameAndType");
        ArrayList<Integer> types = nameAndType.get(name);

        CustomBodyListAdapter customBodyListAdapter = new CustomBodyListAdapter(this, name, bodies, times,types);
        bodyList.setAdapter(customBodyListAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsData = smsToSend.getText().toString();
                Intent intent=new Intent(getApplicationContext(),OpenInbox.class);
                PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, smsData, pi, null);
                Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                /*if(isSmsSendingAllowed())
                {

                }*/

            }
        });

    }

    private boolean isSmsSendingAllowed()
    {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
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
            Toast.makeText(context,"You Clicked Call",Toast.LENGTH_SHORT).show();
            return true;
        }else if (i == R.id.block) {
            Toast.makeText(context,"You Clicked Block",Toast.LENGTH_SHORT).show();
            return true;
        }else if (i == R.id.add_attachment) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            AttachmentDIalogFragment fragment = new AttachmentDIalogFragment().newInstance();
            fragment.show(ft,"title");
            return true;
        } else if (i == R.id.add_to_contacts) {
            Toast.makeText(context,"You Clicked Add to contacts",Toast.LENGTH_SHORT).show();
            return true;
        } else if (i == R.id.delete_thread) {
            Toast.makeText(context,"You Clicked delete thread",Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
