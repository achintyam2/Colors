package com.android.sms;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class CustomBodyListAdapter extends BaseAdapter implements AttachmentDIalogFragment.DialogClickHandler {

    Context context;
    private String name, body, date, time;
    private long timeStamp;
    private int type;
    private static LayoutInflater inflater = null;
    ArrayList<String> bodies;
    ArrayList<Long> times;
    ArrayList<Integer> types;


    public CustomBodyListAdapter(OpenInbox openInbox,
                                 String contactName,
                                 ArrayList<String> body,
                                 ArrayList<Long> time,
                                 ArrayList<Integer> type) {
        context = openInbox.getApplicationContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        name = contactName;
        bodies = body;
        times = time;
        types = type;
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

    @Override
    public void onPhotoClicked(Uri uri) {
        Holder holder = new Holder();
        holder.photo.setImageURI(uri);
    }

    @Override
    public void onContactClicked(String contactDetails) {
        Holder holder = new Holder();
        holder.contact.setText(contactDetails);
    }


    private class Holder {
        TextView bodyLeft, timeLeft, smsDateLeft, statusLeft,bodyRight,timeRight,statusRight,smsDateRight,contact;
        RelativeLayout smsLeft,smsRight;
        ImageView photo;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        if(convertView==null)
        convertView = inflater.inflate(R.layout.body_list_single_row, null);

        holder.bodyLeft = (TextView) convertView.findViewById(R.id.bodyLeft);
        holder.bodyRight = (TextView) convertView.findViewById(R.id.bodyRight);
        holder.timeLeft = (TextView) convertView.findViewById(R.id.timeLeft);
        holder.timeRight = (TextView) convertView.findViewById(R.id.timeRight);
        holder.smsDateLeft = (TextView) convertView.findViewById(R.id.dateLeft);
        holder.smsDateRight = (TextView) convertView.findViewById(R.id.dateRight);
        holder.statusLeft = (TextView) convertView.findViewById(R.id.statusLeft);
        holder.statusRight = (TextView) convertView.findViewById(R.id.statusRight);
        holder.smsLeft = (RelativeLayout) convertView.findViewById(R.id.messagesLeft);
        holder.smsRight = (RelativeLayout) convertView.findViewById(R.id.messagesRight);
        holder.contact = (TextView) convertView.findViewById(R.id.contactDetails);
        holder.photo = (ImageView) convertView.findViewById(R.id.photoSelected);

        body = bodies.get(getCount() - position - 1);
        timeStamp = times.get(getCount() - position - 1);
        type = types.get(getCount() - position - 1);
        time = getTime(timeStamp);
        date = getDate(timeStamp);

        holder.bodyLeft.setText(body);
        holder.bodyRight.setText(body);
        holder.timeLeft.setText(time);
        holder.timeRight.setText(time);

        if (type==1)
        {
            holder.statusLeft.setText("Received");
            holder.statusLeft.setTextColor(Color.GREEN);
            holder.smsDateLeft.setText(date);
            holder.smsDateRight.setVisibility(View.INVISIBLE);
            holder.smsDateLeft.setVisibility(View.VISIBLE);
            holder.smsRight.setVisibility(View.INVISIBLE);
            holder.smsLeft.setVisibility(View.VISIBLE);
        }
       else if (type==2)
        {
            holder.statusRight.setText("Sent");
            holder.statusRight.setTextColor(Color.GREEN);
            holder.smsDateRight.setText(date);
            holder.smsDateLeft.setVisibility(View.INVISIBLE);
            holder.smsDateRight.setVisibility(View.VISIBLE);
            holder.smsLeft.setVisibility(View.INVISIBLE);
            holder.smsRight.setVisibility(View.VISIBLE);
        }
        else if (type==3)
        {
            holder.statusRight.setText("Draft");
            holder.statusRight.setTextColor(Color.BLUE);
            holder.smsDateRight.setText(date);
            holder.smsDateLeft.setVisibility(View.INVISIBLE);
            holder.smsDateRight.setVisibility(View.VISIBLE);
            holder.smsLeft.setVisibility(View.INVISIBLE);
            holder.smsRight.setVisibility(View.VISIBLE);
        }
        else if(type==5)
        {
            holder.statusRight.setText("Failed");
            holder.statusRight.setTextColor(Color.RED);
            holder.smsDateRight.setText(date);
            holder.smsDateLeft.setVisibility(View.INVISIBLE);
            holder.smsDateRight.setVisibility(View.VISIBLE);
            holder.smsLeft.setVisibility(View.INVISIBLE);
            holder.smsRight.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private String getDate(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("MMMM d,yyyy");
        String dateString = formatter.format(date);
        return dateString;
    }

    private String getTime(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        String dateString = formatter.format(date);
        return dateString;
    }
}
