package com.android.dialpad;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


public class DialpadFragment extends Fragment {

    Context context;
    private static final String TAG = "MainActivity";
    DigitsEditText editText;
    final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    String number;
    Intent intent;

    public  DialpadFragment(){}

    public DialpadFragment newInstance()
    {
        DialpadFragment dialpadFragment = new DialpadFragment();
        return  dialpadFragment;
    }


    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        // load the layout
        //setContentView(R.layout.dialpad_fragment);
        context = this.getActivity();

       View v = inflater.inflate(R.layout.dialpad_fragment, container, false);

        editText = (DigitsEditText) v.findViewById(R.id.digits);

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

        ImageButton delete = (ImageButton) v.findViewById(R.id.deleteButton);
        ImageView calling = (ImageView) v.findViewById(R.id.dialpad_floating_action_button);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("1");
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
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("9");
            }
        });
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.append("*");
            }
        });
        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onCLick");
                editText.append("0");
            }
        });
        zero.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG,"onLongCLick");
                editText.append("+");
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
                number = editText.getText().toString();
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
                /*if(isCallingAllowed())
                {
                    //If permission is already having then showing the toast
                    //Toast.makeText(getActivity(),"You already have the permission",Toast.LENGTH_LONG).show();
                    //Existing the method with return
                    startActivity(intent);
                    return;
                }
                requestCallPermission();*/
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
            startActivity(intent);
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

                //Toast.makeText(context,"Permission granted now you can call.",Toast.LENGTH_LONG).show();
                startActivity(intent);

            }else{
                //Displaying another toast if permission is not granted
                Toast.makeText(context,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }


}
