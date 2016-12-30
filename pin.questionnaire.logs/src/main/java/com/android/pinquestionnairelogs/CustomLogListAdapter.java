package com.android.pinquestionnairelogs;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class CustomLogListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final String nameKey = "name", dateKey = "date", callNumberKey = "number",timeKey = "time";
    private String contactName,c;
    private Vector<HashMap<String, Object>> callHistoryVector;
    long currentTimeInMiliSecond;
    private HashMap<String, Object> mapFromCallHistory;
    private HashMap<String, ArrayList<String>> mapFromCallTypePerDay;
    Context context;
    HashMap<String,ArrayList<Long>> contactsPerDayCall;


    public CustomLogListAdapter(MainActivity1 mainActivity1,
                                Vector<HashMap<String, Object>> callLogs,
                                HashMap<String,ArrayList<String>> readCallTypePerDay) {
        context = mainActivity1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callHistoryVector = callLogs;
        mapFromCallTypePerDay = readCallTypePerDay;
        currentTimeInMiliSecond = System.currentTimeMillis();
        contactsPerDayCall = new HashMap<>();
    }

    @Override
    public int getCount() {
        return callHistoryVector.size();
    }

    @Override
    public Object getItem(int position) {
        return callHistoryVector.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class Holder {
        TextView contactName, callDate, callMade, callReceived, callMissed;
        int called = 0, received = 0, missed = 0;
        ImageView callImage, receivedImage, missedImage, menu;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View view = inflater.inflate(R.layout.call_log_single_row, null);

        mapFromCallHistory = callHistoryVector.get(position);

        contactName = (String) mapFromCallHistory.get(nameKey);

        //long seconds = (long) mapFromCallHistory.get(timeKey);

        ArrayList<String> values = mapFromCallTypePerDay.get(contactName);
        String contact_date = (String) mapFromCallHistory.get(dateKey);

        /*long day = ((currentTimeInMiliSecond - seconds) / 86400000);

        ArrayList<Long> contactTime = contactsPerDayCall.get(contactName + "_day" + day);
        //countTimeStamps += 1;
        if (contactTime == null)
            contactTime = new ArrayList<>();
        contactTime.add(seconds);
        contactsPerDayCall.put(contactName + "_day" + day, contactTime);*/


        holder.called = 0;
        holder.received = 0;
        holder.missed = 0;

        for (int i = 0; i < values.size(); i++) {
            if ("Outgoing".equals(values.get(i)))
                holder.called += 1;
            else if ("Incoming".equals(values.get(i)))
                holder.received += 1;
            else if ("Missed".equals(values.get(i)))
                holder.missed += 1;
        }

        holder.contactName = (TextView) view.findViewById(R.id.contact_name);
        holder.callDate = (TextView) view.findViewById(R.id.call_date);
        holder.callMade = (TextView) view.findViewById(R.id.call_made);
        holder.callReceived = (TextView) view.findViewById(R.id.call_received);
        holder.callMissed = (TextView) view.findViewById(R.id.call_missed);
        holder.callImage = (ImageView) view.findViewById(R.id.outgoing);
        holder.receivedImage = (ImageView) view.findViewById(R.id.incoming);
        holder.missedImage = (ImageView) view.findViewById(R.id.missed);
        holder.menu = (ImageView) view.findViewById(R.id.menu);

        holder.contactName.setText(contactName);
        holder.callDate.setText(contact_date);

        if (holder.called != 0) {
            holder.callMade.setText("(" + holder.called + ")");
            holder.callImage.setImageResource(R.drawable.ic_call_made_green_24dp);
        }
        if (holder.received != 0) {
            holder.callReceived.setText("(" + holder.received + ")");
            holder.receivedImage.setImageResource(R.drawable.ic_call_received_green_24dp);
        }
        if (holder.missed != 0) {
            holder.callMissed.setText("(" + holder.missed + ")");
            holder.missedImage.setImageResource(R.drawable.ic_call_missed_red_24dp);
        }
        if (holder.called == 0 && holder.received == 0 && holder.missed == 0)
            holder.callImage.setImageResource(R.drawable.ic_cancelled_red_24dp);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapFromCallHistory = callHistoryVector.get(position);
                String number = (String) mapFromCallHistory.get(callNumberKey);
                Log.d("aa", "Number " + number);
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
                new LogsDialogFragment();
                LogsDialogFragment logsDialogFragment = LogsDialogFragment.newInstance();
                logsDialogFragment.show(fragmentManager,"mm");
            }
        });
        return view;
    }
}
