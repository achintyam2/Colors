package com.android.mms;


import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.CancellationSignal;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


public class MMSInboxFragment extends Fragment {

    ImageView imageView;
    Button getMMSList;
    TextView textView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mms_inbox_main, container, false);

        getMMSList = (Button) view.findViewById(R.id.getMMSList);
        imageView = (ImageView) view.findViewById(R.id.image);
        textView = (TextView) view.findViewById(R.id.body);
        getMMSList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getReceivedMMSinfo();
            }
        });

        return view;
    }

    private void getReceivedMMSinfo() {
        Uri uri = Uri.parse("content://mms/inbox");
        String str = "";
        Cursor cursor = getContext().getContentResolver().query(uri, null,null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        String mms_id= cursor.getString(cursor.getColumnIndex("_id"));
        String phone = cursor.getString(cursor.getColumnIndex("address"));
        String dateVal = cursor.getString(cursor.getColumnIndex("date"));
        Date date = new Date(Long.valueOf(dateVal));

        // 2 = sent, etc.
        int mtype = cursor.getInt(cursor.getColumnIndex("type"));
        String body="";

        Bitmap bitmap;

        String type = cursor.getString(cursor.getColumnIndex("ct"));
        if ("text/plain".equals(type)){
            String data = cursor.getString(cursor.getColumnIndex("body"));
            if(data != null){
                body = getReceivedMmsText(mms_id);
                textView.setText(body);
            }
            else {
                body = cursor.getString(cursor.getColumnIndex("text"));
                textView.setText(body);
                //body text is stored here
            }
        }
        else if("image/jpeg".equals(type) ||
                "image/bmp".equals(type) ||
                "image/gif".equals(type) ||
                "image/jpg".equals(type) ||
                "image/png".equals(type)){
            bitmap = getReceivedMmsImage(mms_id);
            imageView.setImageBitmap(bitmap);
            //image is stored here
            //now we are storing on SDcard

        }

        str = "Sent MMS: \n phone is: " + phone;
        str +="\n MMS type is: "+mtype;
        str +="\n MMS time stamp is:"+date;
        str +="\n MMS body is: "+body;
        str +="\n id is : "+mms_id;


        Log.v("Debug","sent MMS phone is: "+phone);
        Log.v("Debug","MMS type is: "+mtype);
        Log.v("Debug","MMS time stamp is:"+date);
        Log.v("Debug","MMS body is: "+body);
        Log.v("Debug","MMS id is: "+mms_id);

        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
        Log.v("Debug", "RDC : So we got all informaion " +
                "about Received MMS Message :) ");
    }

    //method to get Text body from Received MMS.........
    private String getReceivedMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/inbox" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getContext().getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString();
    }

    //method to get image from Received MMS..............
    private Bitmap getReceivedMmsImage(String id) {


        Uri partURI = Uri.parse("content://mms/inbox" + id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getContext().getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return bitmap;

    }


}
