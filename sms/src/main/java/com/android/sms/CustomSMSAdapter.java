package com.android.sms;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;


class CustomSMSAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;
    private HashMap<String, MyThread> mapFromSMSVector;
    private String contactName,thread_id;
    private ArrayList<String> threads;


    CustomSMSAdapter(SMSInboxFragment smsInboxFragment,
                            HashMap<String, MyThread> receivedSMSMap,
                            ArrayList<String> threadIds) {
        context = smsInboxFragment.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mapFromSMSVector = receivedSMSMap;
        threads = threadIds;
        Log.d("aa","threadsList "+threads);
    }

    @Override
    public int getCount() {
        return threads.size();
    }

    @Override
    public Object getItem(int position) {
        return mapFromSMSVector.get(threads.get(position));
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class Holder {
        TextView contactName, smsDate, smsBody, contactID;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();

        View view = inflater.inflate(R.layout.sms_single_row, null);

        holder.contactName = (TextView) view.findViewById(R.id.contact_name);
        holder.smsDate = (TextView) view.findViewById(R.id.sms_date);
        holder.smsBody = (TextView) view.findViewById(R.id.sms_body);
        holder.contactID = (TextView) view.findViewById(R.id.contactID);

        MyThread myThread = mapFromSMSVector.get(threads.get(position));
        thread_id = myThread.getThreadId();
        contactName = myThread.getName();
        String cName = getContactName(context,contactName);
        String date = myThread.getTime();
        String smsBody = myThread.getMessage();
        final String contactID = contactName.toUpperCase().substring(0, 1);

        holder.contactName.setText(cName);
        holder.contactID.setText("".concat(contactID));
        holder.smsDate.setText(date);
        holder.smsBody.setText(smsBody);

        view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyThread m = (MyThread) getItem(position);
                    String phone = m.getName();
                    String name = getContactName(context,phone);
                    String thread = m.getThreadId();
                    Intent intent = new Intent(context, OpenInbox.class);
                    intent.putExtra("name", name);
                    intent.putExtra("thread_id",thread);
                    context.startActivity(intent);
                }
            });

        return view;
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


}
