package com.example.achintya.dialpaddialogfragment;

import android.Manifest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class DialpadFragment extends Fragment {

    Context context;
    private static final String TAG = "MainActivity";
    DigitsEditText editText;
    LinearLayout digitsContainer;
    Intent my_callIntent;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = this.getActivity();

        View v = inflater.inflate(R.layout.dialpad_fragment, container, false);

        editText = (DigitsEditText) v.findViewById(R.id.digits);
        digitsContainer = (LinearLayout) v.findViewById(R.id.digits_container) ;

        DialpadKeyButton one = (DialpadKeyButton) v.findViewById(R.id.one);
        DialpadKeyButton two = (DialpadKeyButton) v.findViewById(R.id.two);
        DialpadKeyButton three = (DialpadKeyButton) v.findViewById(R.id.three);
        DialpadKeyButton four = (DialpadKeyButton) v.findViewById(R.id.four);
        DialpadKeyButton five = (DialpadKeyButton) v.findViewById(R.id.five);
        DialpadKeyButton six = (DialpadKeyButton) v.findViewById(R.id.six);
        DialpadKeyButton seven = (DialpadKeyButton) v.findViewById(R.id.seven);
        DialpadKeyButton eight = (DialpadKeyButton) v.findViewById(R.id.eight);
        DialpadKeyButton nine = (DialpadKeyButton) v.findViewById(R.id.nine);
        DialpadKeyButton star = (DialpadKeyButton) v.findViewById(R.id.star);
        DialpadKeyButton zero = (DialpadKeyButton) v.findViewById(R.id.zero);
        DialpadKeyButton pound = (DialpadKeyButton) v.findViewById(R.id.pound);

        ImageView delete = (ImageView) v.findViewById(R.id.deleteButton);
        ImageView calling = (ImageView) v.findViewById(R.id.dialpad_floating_action_button);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("1");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        one.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Voicemail", Toast.LENGTH_LONG).show();
                return true;
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("2");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("3");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("4");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("5");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("6");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("7");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("8");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("9");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("*");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onCLick");
                editText.append("0");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
            }
        });
        zero.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"onLongCLick");
                editText.append("+");
                if(editText.toString().length()>0)
                    digitsContainer.setVisibility(View.VISIBLE);
                return true;
            }
        });
        pound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("#");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                int size = text.length();
                if(size>0) {
                    editText.setText(text.substring(0, text.length() - 1));
                }
                if(size==1)
                    digitsContainer.setVisibility(View.GONE);
            }
        });
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editText.setText("");
                return false;
            }
        });
        calling.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                my_callIntent = new Intent(Intent.ACTION_CALL);
                String number = editText.getText().toString();
                my_callIntent.setData(Uri.parse("tel:"+number));
                if(isCallingAllowed())
                {
                    //If permission is already having then showing the toast
                    //Toast.makeText(getActivity(),"You already have the permission",Toast.LENGTH_LONG).show();
                    //Existing the method with return
                    startActivity(my_callIntent);
                    return;
                }
                requestCallPermission();

            }
        });
        return v;
    }

    private boolean isCallingAllowed()
    {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }
        //If permission is not granted returning false
        return false;
    }


    private  void requestCallPermission()
    {
        //And finally ask for the permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE},MY_PERMISSIONS_REQUEST_CALL_PHONE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                Toast.makeText(context,"Permission granted now you can call.",Toast.LENGTH_LONG).show();
                startActivity(my_callIntent);

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(context,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
