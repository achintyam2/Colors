package com.android.newpinscreen;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.Vector;


public class CustomLogListAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private String name = "name", date = "date", type = "type";
    private Context context;
    private String names, dates, types;
    Vector<HashMap<String, Object>> callHistory ;
    HashMap<String, Object> mTemp = new HashMap<String, Object>();

    public  CustomLogListAdapter(MainActivity1 mainActivity1,Vector<HashMap<String, Object>> mCallHistory)
    {
        context = mainActivity1;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        callHistory = mCallHistory;

    }

    @Override
    public int getCount() {
        return callHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return callHistory.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class  Holder
    {
        TextView contactName, callDate,callType;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        View view = inflater.inflate(R.layout.call_log_single_row, null);

        RelativeLayout  r1 = (RelativeLayout) view.findViewById(R.id.r1);
        RelativeLayout  r2 = (RelativeLayout) view.findViewById(R.id.r2);

        mTemp = callHistory.get(position);
        names = (String) mTemp.get(name);
        dates = (String) mTemp.get(date);
        types = (String) mTemp.get(type);

        holder.contactName = (TextView) view.findViewById(R.id.contact_name);
        holder.callDate = (TextView) view.findViewById(R.id.call_date);
        holder.callType = (TextView) view.findViewById(R.id.call_type);

        holder.contactName.setText(names);
        holder.callDate.setText(dates);
        holder.callType.setText(types);

        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
                LogsDialogFragment logsDialogFragment = new LogsDialogFragment();
                logsDialogFragment.show(fragmentManager,"mm");
            }
        });
        return view;
    }
}
