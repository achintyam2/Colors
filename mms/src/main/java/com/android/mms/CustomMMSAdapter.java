package com.android.mms;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class CustomMMSAdapter  extends  CursorAdapter {


    Context con;
    Calendar c;
    int duplicateThread;
    int previos,present;
    boolean flag = true;
    boolean trag = false;
    Set<Integer> threads;
    HashMap<Integer,ArrayList<Integer>> threadHashMap;


    public CustomMMSAdapter(Context context, Cursor cursor) {
        super(context, cursor,0);
        c =  Calendar.getInstance();
        con = context;
        threadHashMap = new HashMap<>();



    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.mms_single_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        TextView contactID = (TextView)view.findViewById(R.id.contactID);
        TextView contactName = (TextView)view.findViewById(R.id.contact_name);
        TextView msgBody = (TextView)view.findViewById(R.id.sms_body);
        TextView msgDate = (TextView)view.findViewById(R.id.sms_date);
        RelativeLayout r1 = (RelativeLayout)view.findViewById(R.id.r1);
        RelativeLayout r2 = (RelativeLayout)view.findViewById(R.id.r2);
        RelativeLayout r3 = (RelativeLayout)view.findViewById(R.id.r3);

        final String phone = cursor.getString(cursor.getColumnIndex("address"));
        final String name = getContactName(context,phone);
        String id = name.toUpperCase().substring(0, 1);
        String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        long dateVal = cursor.getLong(cursor.getColumnIndex("date"));
        String date  = getDateTime(dateVal);
        final int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));

        duplicateThread = thread_id;

        contactID.setText(id);
        contactName.setText(name);
        msgBody.setText(body);
        msgDate.setText(date);

        ArrayList<Integer> threadsCount = threadHashMap.get(thread_id);
        if (threadsCount == null)
            threadsCount = new ArrayList<>();
        threadsCount.add(1);
        threadHashMap.put(thread_id,threadsCount);

        ArrayList<Integer> countList = threadHashMap.get(thread_id);
        int size = countList.size();

        if (size>1)
        {
            /*r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);*/
        }
        else
        {
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.VISIBLE);
        }

        Log.d("aa","thread "+threadHashMap.size());
        /*if (threads == null)
            threads = new HashSet<>();
        threads.add(duplicateThread);*/

        /*if (flag)
        {
            previos = thread_id;
            flag =false;
        }

        if (trag)
        {
            present = thread_id;
            trag = false;
        }
        trag = true;
        if (present == thread_id)
        {
            previos = present;
            present = thread_id;
        }

        if (previos==present)
        {
            if (count==0) {
                r1.setVisibility(View.VISIBLE);
                r2.setVisibility(View.VISIBLE);
                r3.setVisibility(View.VISIBLE);
                count +=1;
            }
        }
        else
        {
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
        }*/

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,Inbox.class);
                intent.putExtra("thread",thread_id);
                intent.putExtra("name",name);
                context.startActivity(intent);

            }
        });
    }

    private String getDateTime(long time)
    {
        String dateString;
        int currentYears = c.get(Calendar.YEAR);
        String currentYear = Integer.toString(currentYears);
        int currentDays = c.get(Calendar.DAY_OF_MONTH);
        String currentDay = Integer.toString(currentDays);

        SimpleDateFormat yearFormatter = new SimpleDateFormat("yyyy");
        String year = yearFormatter.format(new Date(time));

        SimpleDateFormat dayFormatter = new SimpleDateFormat("d");
        String day = dayFormatter.format(new Date(time));
        int previousDay  = currentDays -1;

        if (day.equals(currentDay))
        {
            SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
            dateString = formatter.format(new Date(time));
        }
        else if (currentDays!=previousDay)
        {
            if (year.equals(currentYear)) {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d");
                dateString = formatter.format(new Date(time));
            }
            else
            {
                SimpleDateFormat formatter = new SimpleDateFormat("MMM d,yyyy");
                dateString = formatter.format(new Date(time));
            }
        }
        else
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            dateString = formatter.format(new Date(time));
        }
        return dateString;
    }

    private String getContactName(Context context, String number) {

        String name = null;

        // define the columns I want the query to return
        String[] projection = new String[] {
                ContactsContract.PhoneLookup.DISPLAY_NAME,
                ContactsContract.PhoneLookup._ID};

        // encode the phone number and build the filter URI
        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        // query time
        Cursor cursor = context.getContentResolver().query(contactUri, projection, null, null, null);

        if(cursor != null) {
            if (cursor.moveToFirst()) {
                name =     cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
//                Log.v(TAG, "Started uploadcontactphoto: Contact Found @ " + number);
//                Log.v(TAG, "Started uploadcontactphoto: Contact name  = " + name);
            } else {
//                Log.v(TAG, "Contact Not Found @ " + number);
                name  = number;
            }
            cursor.close();
        }
        return name;
    }

    private boolean isReadingContactsAllowed() {
        int result = ContextCompat.checkSelfPermission(con, Manifest.permission.READ_CONTACTS);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

}
