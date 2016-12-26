package com.android.newpinscreen;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ListView;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;


public class MainActivity1 extends Activity {


    ListView listView;

    private String name = "name", date = "date", duration = "duration", type = "type";
    ArrayList<String> s;
    Vector<HashMap<String, Object>> mCallHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        listView = (ListView) findViewById(R.id.logs_list);
        mCallHistory = new Vector<HashMap<String, Object>>();

        getCallDetails();
    }

    private void getCallDetails() {

        String strOrder = CallLog.Calls.DATE + " DESC";
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor c = getApplicationContext().getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                null, null, strOrder);
        c.moveToFirst();

        do {
            HashMap<String, Object> mTemp = new HashMap<String, Object>();

            /*Reading Number*/
            String callerNumber = c.getString(c.
                    getColumnIndex(CallLog.Calls.NUMBER));

            /* Reading Name */
            String nameTemp = c.getString(c
                    .getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (nameTemp==null)
                mTemp.put(name, callerNumber);
            else
                mTemp.put(name, nameTemp);

            /* Reading Date */
            /*String dateTemp = c.getString(c
                    .getColumnIndex(CallLog.Calls.DATE));*/

            long seconds = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
            String dateString = formatter.format(new Date(seconds));
           // Date callDate = new Date(Long.valueOf(dateTemp));
            mTemp.put(date, dateString);

            /*Reading call type*/
            String callTypeCode = c.getString(c.
                    getColumnIndex(CallLog.Calls.TYPE));
            String callType = null;
            int callcode = Integer.parseInt(callTypeCode);
            switch (callcode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    callType = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    callType = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    callType = "Missed";
                    break;
            }
            if (callType==null)
                mTemp.put(type, "Cancelled");
            else
                mTemp.put(type, callType);


            /* Add one call Detail to Vector */
            mCallHistory.add(mTemp);
        } while (c.moveToNext());

        final CustomLogListAdapter adapter = new CustomLogListAdapter(this, mCallHistory);
        listView.setAdapter(adapter);
        Log.d("Check  ", "Data" + mCallHistory);
        c.close();
    }
}
