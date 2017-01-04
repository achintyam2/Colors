package com.android.sms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class OpenInbox extends Activity {

    ListView bodyList;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.open_inbox);

        context = this.getApplicationContext();
        bodyList = (ListView) findViewById(R.id.bodyList);


        String name = intent.getStringExtra("name");

        HashMap<String,ArrayList<String>> nameAndBody = (HashMap<String, ArrayList<String>>) intent.getSerializableExtra("nameAndBody");
        ArrayList<String> bodies = nameAndBody.get(name);

        HashMap<String,ArrayList<Long>> nameAndTime = (HashMap<String, ArrayList<Long>>) intent.getSerializableExtra("nameAndTime");
        ArrayList<Long> times = nameAndTime.get(name);

        CustomBodyListAdapter customBodyListAdapter = new CustomBodyListAdapter(this,name,bodies,times);
        bodyList.setAdapter(customBodyListAdapter);

    }
}
