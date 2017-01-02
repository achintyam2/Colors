package com.android.calllogs;

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

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;


public class CustomLogListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private final String nameKey = "name", dateKey = "date", callNumberKey = "number",timeKey = "time";
    private String contactName;
    private Vector<HashMap<String, Object>> callHistoryVector;
    long currentTimeInMiliSecond;
    private HashMap<String, Object> mapFromCallHistory;
    private HashMap<String, ArrayList<String>> mapFromCallTypePerDay;
    Context context;
    HashMap<String,ArrayList<Long>> contactsPerDayCall;
    Calendar c;


    public CustomLogListAdapter(CallLogsFragment mainActivity1,
                                Vector<HashMap<String, Object>> callLogs,
                                HashMap<String,ArrayList<String>> readCallTypePerDay) {
        context = mainActivity1.getContext();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callHistoryVector = callLogs;
        mapFromCallTypePerDay = readCallTypePerDay;
        currentTimeInMiliSecond = System.currentTimeMillis();
        contactsPerDayCall = new HashMap<>();
        c =  Calendar.getInstance();
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

        holder.callDate = (TextView) view.findViewById(R.id.call_date);
        holder.contactName = (TextView) view.findViewById(R.id.contact_name);
        holder.callMade = (TextView) view.findViewById(R.id.call_made);
        holder.callReceived = (TextView) view.findViewById(R.id.call_received);
        holder.callMissed = (TextView) view.findViewById(R.id.call_missed);
        holder.callImage = (ImageView) view.findViewById(R.id.outgoing);
        holder.receivedImage = (ImageView) view.findViewById(R.id.incoming);
        holder.missedImage = (ImageView) view.findViewById(R.id.missed);
        holder.menu = (ImageView) view.findViewById(R.id.menu);

        mapFromCallHistory = callHistoryVector.get(position);

        contactName = (String) mapFromCallHistory.get(nameKey);
        holder.contactName.setText(contactName);

        long seconds = (long) mapFromCallHistory.get(timeKey);
        String date  = getDateTime(seconds);
        holder.callDate.setText(date);

        ArrayList<String> values = mapFromCallTypePerDay.get(contactName);

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
            SimpleDateFormat formatter = new SimpleDateFormat("h:m a");
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
}
