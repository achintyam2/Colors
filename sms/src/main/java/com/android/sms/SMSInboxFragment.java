package com.android.sms;


import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class SMSInboxFragment extends Fragment {

    ListView smsList;
    Vector<HashMap<String, Object>> vectorSMSList;
    Button getSMSList;
    HashMap<String,ArrayList<String>> nameAndBodyList;
    HashMap<String,ArrayList<Long>> nameAndTimeList;
    HashMap<String,ArrayList<Integer>> nameAndTypeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sms_inbox_main, container, false);

        vectorSMSList = new Vector<>();

        nameAndBodyList = new HashMap<>();
        nameAndTimeList = new HashMap<>();
        nameAndTypeList = new HashMap<>();

        smsList = (ListView) view.findViewById(R.id.listViewSMS);

        getSMSList = (Button) view.findViewById(R.id.getSMSList);

        getSMSList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchInbox();
            }
        });

        return view;
    }

    public void fetchInbox()
    {
        Uri uriSms = Uri.parse("content://sms");
        String[] projection = {"address", "date", "body","type"};
        Cursor cursor = getContext().getContentResolver().query(uriSms,projection, null,null,"date DESC");
        cursor.moveToFirst();

        do {
            HashMap<String, Object> readingData = new HashMap<>();

            String address = cursor.getString(0);
            readingData.put("address",address);
            String date = cursor.getString(1);
            long seconds = Long.parseLong(date);
            readingData.put("date",seconds);
            String body = cursor.getString(2);
            readingData.put("body",body);
            String typeSms = cursor.getString(3);
            int type = Integer.parseInt(typeSms);
            readingData.put("type",type);

            ArrayList<String> bodyList = nameAndBodyList.get(address);
            if (bodyList == null)
                bodyList = new ArrayList<>();
            bodyList.add(body);
            nameAndBodyList.put(address,bodyList);

            ArrayList<Long> timeList = nameAndTimeList.get(address);
            if (timeList == null)
                timeList = new ArrayList<>();
            timeList.add(seconds);
            nameAndTimeList.put(address,timeList);

            ArrayList<Integer> typeList = nameAndTypeList.get(address);
            if (typeList == null)
                typeList = new ArrayList<>();
            typeList.add(type);
            nameAndTypeList.put(address,typeList);

            vectorSMSList.add(readingData);

        } while  (cursor.moveToNext());

        final CustomSMSAdapter adapter = new CustomSMSAdapter(this,vectorSMSList,nameAndBodyList,nameAndTimeList,nameAndTypeList);
        smsList.setAdapter(adapter);

        Log.d("aa","SMS List "+vectorSMSList);
        Log.d("aa","Name and Body List " +nameAndBodyList);

    }
}
