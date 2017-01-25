package com.android.mms;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MMSInboxFragment extends Fragment {

    Button getMMSList;
    Context context;
    ListView mmsList;
    ContentResolver contentResolver;
    Cursor mainCursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.mms_inbox_main, container, false);
        getMMSList = (Button) view.findViewById(R.id.getMMSList);
        context = this.getContext();
        mmsList = (ListView) view.findViewById(R.id.mmsList);
        contentResolver = context.getContentResolver();

        getMMSList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllowed())
                {
                    read();
                    return;
                }
                else
                    requestPermission();

            }
        });
        return view;
    }

    private void read() {
//        final String[] projection = new String[]{"_id","address", "date", "body",  "thread_id"};
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://sms");
        if (mainCursor==null)
            mainCursor = contentResolver.query(uri, projection, null, null,null);
        mainCursor.moveToFirst();

        CustomMMSAdapter adapter = new CustomMMSAdapter(this.getContext(),mainCursor);
        mmsList.setAdapter(adapter);
    }

    private boolean isAllowed()
    {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            read();
            return true;
        }
        return false;
    }

    private  void requestPermission()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.READ_SMS},1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 1){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(getContext(),"Permission granted now you can read.",Toast.LENGTH_LONG).show();

            }else{
                Toast.makeText(getContext(),"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

}
