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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;


public class SMSInboxFragment extends Fragment {

    ListView smsList;
    Vector<HashMap<String, MyThread>> vectorSMSList;
    Button getSMSList;
    Calendar c;
    HashMap<String, MyThread> myThreads;
    ArrayList<String> threads;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sms_inbox_main, container, false);
        myThreads = new HashMap<>();
        vectorSMSList = new Vector<>();
        threads = new ArrayList<>();
        c = Calendar.getInstance();
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
        String[] projection = {"address", "date", "body","thread_id"};
            Cursor cursor = getContext().getContentResolver().query(uriSms,projection, null,null,"date DESC");
        cursor.moveToFirst();

        do {


            String address = cursor.getString(cursor.getColumnIndex("address"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            long seconds = Long.parseLong(date);
            String dateVal = getDateTime(seconds);
            String body = cursor.getString(cursor.getColumnIndex("body"));
            String thread_id = cursor.getString(cursor.getColumnIndex("thread_id"));


            if (!myThreads.containsKey(thread_id)) {
                myThreads.put(thread_id, new MyThread(thread_id, body, dateVal, address));
                threads.add(thread_id);
            }

            vectorSMSList.add(myThreads);

        } while  (cursor.moveToNext());

        final CustomSMSAdapter adapter = new CustomSMSAdapter(this,vectorSMSList.get(0),threads);
        smsList.setAdapter(adapter);
        Log.d("aa","SMS List "+vectorSMSList.get(0));
        Log.d("aa","thread List "+threads);

    }

    private String getDateTime(long time) {
        String dateString;
        int currentYears = c.get(Calendar.YEAR);
        String currentYear = Integer.toString(currentYears);
        int currentDays = c.get(Calendar.DAY_OF_MONTH);
        String currentDay = Integer.toString(currentDays);

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        String year = yearFormatter.format(new Date(time));

        SimpleDateFormat dayFormatter = new SimpleDateFormat("d");
        String day = dayFormatter.format(new Date(time));
        int previousDay = currentDays - 1;

        if (day.equals(currentDay)&& year.equals(currentYear)) {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            dateString = formatter.format(new Date(time));
        } else if (currentDays != previousDay) {
            if (year.equals(currentYear)) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
                dateString = formatter.format(new Date(time));
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d,yyyy");
                dateString = formatter.format(new Date(time));
            }
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            dateString = formatter.format(new Date(time));
        }
        return dateString;
    }
}
