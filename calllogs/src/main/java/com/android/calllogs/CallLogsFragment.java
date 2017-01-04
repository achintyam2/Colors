package com.android.calllogs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import static android.provider.CallLog.Calls.CONTENT_URI;


public class CallLogsFragment extends Fragment {

    ListView callLogsList;
    private final String nameKey = "name", dateKey = "date", typeKey = "type", timeKey = "time", callNumberKey = "number";
    Vector<HashMap<String, Object>> vectorCallHistory;
    HashMap<Object, Object> readTimeAndCallType;
    HashMap<String, ArrayList<Long>> nameAndTimeMap;
    HashMap<String, ArrayList<Long>> contactsPerDayCall;
    long currentTimeInMiliSecond;
    int countTimeStamps;
    HashMap<String, ArrayList<String>> callTypePerDay;
    Button getDetails;



    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_main1,container,false);
        callLogsList = (ListView) view.findViewById(R.id.logs_list);
        vectorCallHistory = new Vector<>();
        readTimeAndCallType = new HashMap<>();
        nameAndTimeMap = new HashMap<>();
        contactsPerDayCall = new HashMap<>();
        currentTimeInMiliSecond = System.currentTimeMillis();
        callTypePerDay = new HashMap<>();
        getDetails = (Button) view.findViewById(R.id.call_logs);

        getDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCallDetails();
            }
        });

        return view;
    }

    private void getCallDetails() {

        String strOrder = CallLog.Calls.DATE + " DESC";
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Cursor c = getContext().getContentResolver().query(CONTENT_URI, null,
                null, null, strOrder);
        c.moveToFirst();

        do {
            HashMap<String, Object> readingData = new HashMap<>();


            /*Reading Number*/
            String callerNumber = c.getString(c.
                    getColumnIndex(CallLog.Calls.NUMBER));

            readingData.put(callNumberKey, callerNumber);

            /* Reading Name */
            String contactName = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
            if (contactName == null) {
                contactName = callerNumber;
                readingData.put(nameKey, contactName);
            } else
                readingData.put(nameKey, contactName);

            long seconds = c.getLong(c.getColumnIndex(CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM,HH:mm");
            String dateString = formatter.format(new Date(seconds));
            readingData.put(dateKey, dateString);
            readingData.put(timeKey, seconds);



            /*Reading call types*/
            String callTypeCode = c.getString(c.getColumnIndex(CallLog.Calls.TYPE));
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
            if (callType == null) {
                callType = "Cancelled";
                readingData.put(typeKey, callType);
            } else
                readingData.put(typeKey, callType);

            readTimeAndCallType.put(seconds, callType);

            /* Add one call Detail to Vector */
            vectorCallHistory.add(readingData);

            long day = ((currentTimeInMiliSecond - seconds) / 86400000);
            ArrayList<Long> contactTime = contactsPerDayCall.get(contactName + "_day" + day);
            //countTimeStamps += 1;
            if (contactTime == null)
                contactTime = new ArrayList<>();
            contactTime.add(seconds);
            contactsPerDayCall.put(contactName + "_day" + day, contactTime);

            countTimeStamps = contactsPerDayCall.get(contactName + "_day" + day).size();

            for (int i = 0; i < countTimeStamps; i++) {
                ArrayList<String> typeOfCall = callTypePerDay.get(contactName);
                if (typeOfCall == null)
                    typeOfCall = new ArrayList<>();
                long timeStamp = contactTime.get(i);
                String callTypeFromMap = (String) readTimeAndCallType.get(timeStamp);
                typeOfCall.add(callTypeFromMap);
                callTypePerDay.put(contactName, typeOfCall);
            }

        } while (c.moveToNext());

        final CustomLogListAdapter adapter = new CustomLogListAdapter(this, vectorCallHistory, callTypePerDay);
        callLogsList.setAdapter(adapter);
        Log.d("Check  ", "VectorCalHistory" + vectorCallHistory);
        Log.d("Check  ", "SingleTimeAndCallType" + readTimeAndCallType);
        Log.d("Check  ", "ContactsPerDayCall " + contactsPerDayCall);
        Log.d("Check  ", "callTypePerDay " + callTypePerDay);

        c.close();
    }

}
