package com.android.sms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;


public class CustomSMSAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    Context context;
    Vector<HashMap<String, Object>> smsListVector;
    HashMap<String, Object> mapFromSMSVector;
    String contactName, smsBody, contactID,date;
    Calendar c;
    HashMap<String,ArrayList<String>> nameAndBodyList;
    HashMap<String,ArrayList<Long>> nameAndTimeList;
    HashMap<String,ArrayList<Integer>> nameAndTypeList;
    ArrayList<String> bodies;
    long seconds;


    public CustomSMSAdapter(SMSInboxFragment smsInboxFragment,
                            Vector<HashMap<String, Object>> receivedSMSVector,
                            HashMap<String,ArrayList<String>> nameAndBody,
                            HashMap<String,ArrayList<Long>> nameAndTime,
                            HashMap<String,ArrayList<Integer>> nameAndType) {
        context = smsInboxFragment.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        smsListVector = receivedSMSVector;
        c = Calendar.getInstance();
        nameAndBodyList = nameAndBody;
        nameAndTimeList = nameAndTime;
        nameAndTypeList = nameAndType;
        mapFromSMSVector = new HashMap<>();
        bodies = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return smsListVector.size();
    }

    @Override
    public Object getItem(int position) {
        return smsListVector.get(position);
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
        if (convertView==null)
            convertView = inflater.inflate(R.layout.sms_single_row, null);


            holder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            holder.smsDate = (TextView) convertView.findViewById(R.id.sms_date);
            holder.smsBody = (TextView) convertView.findViewById(R.id.sms_body);
            holder.contactID = (TextView) convertView.findViewById(R.id.contactID);

            mapFromSMSVector = smsListVector.get(position);
            contactName = (String) mapFromSMSVector.get("address");

            bodies = nameAndBodyList.get(contactName);

            if (contactName == null)
                contactID = "3";
            else
                contactID = contactName.toUpperCase().substring(0, 1);
            holder.contactName.setText(contactName);
            holder.contactID.setText(contactID + "");

            smsBody = (String) mapFromSMSVector.get("body");
            holder.smsBody.setText(bodies.get(0));

            seconds = (long) mapFromSMSVector.get("date");
            date = getDateTime(seconds);
            holder.smsDate.setText(date);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mapFromSMSVector = smsListVector.get(position);
                    contactName = (String) mapFromSMSVector.get("address");
                    Intent intent = new Intent(context, OpenInbox.class);
                    intent.putExtra("name", contactName);
                    intent.putExtra("nameAndBody", nameAndBodyList);
                    intent.putExtra("nameAndTime", nameAndTimeList);
                    intent.putExtra("nameAndType", nameAndTypeList);
                    context.startActivity(intent);
                }
            });

        return convertView;
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

        if (day.equals(currentDay)) {
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
