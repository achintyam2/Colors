package com.android.sms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CustomBodyListAdapter extends BaseAdapter {

    Context context;
    private String name,body,date,time;
    private long timeStamp;
    private static LayoutInflater inflater = null;
    ArrayList<String> bodies;
    ArrayList<Long> times;


    public CustomBodyListAdapter(OpenInbox openInbox,
                                 String contactName,
                                 ArrayList<String> body,
                                 ArrayList<Long> time
                                 )
    {
        context = openInbox.getApplicationContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        name  = contactName;
        bodies = body;
        times = time;

    }


    @Override
    public int getCount() {
        return bodies.size();
    }

    @Override
    public Object getItem(int position) {
        return bodies.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    private class Holder {
        TextView bodyText,smsTime,smsDate;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View view = inflater.inflate(R.layout.body_list_single_row, null);

        holder.bodyText = (TextView) view.findViewById(R.id.bodyText);
        holder.smsTime = (TextView) view.findViewById(R.id.time);
        holder.smsDate = (TextView) view.findViewById(R.id.date);


            body = bodies.get(getCount() - position - 1);
            timeStamp = times.get(getCount() - position - 1);

            time = getTime(timeStamp);
            date = getDate(timeStamp);
            holder.bodyText.setText(body);
            holder.smsDate.setText(date);
            holder.smsTime.setText(time);


        return view;
    }

    private String getDate(long timeStamp)
    {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("MMMM d,yyyy");
        String dateString = formatter.format(date);
        return dateString;
    }

    private String getTime(long timeStamp)
    {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        String dateString = formatter.format(date);
        return dateString;
    }
}
