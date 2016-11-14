package com.example.achintya.dialpaddialogfragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class DialpadFragment extends Fragment {

    Context context;
    private static final String TAG = "MainActivity";
    DigitsEditText editText;
    LinearLayout digitsContainer;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // load the layout
        //setContentView(R.layout.dialpad_fragment);
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
            Intent my_callIntent = new Intent(Intent.ACTION_VIEW);
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                my_callIntent.setData(Uri.parse(number));
                startActivity(my_callIntent);
            }
        });
        return v;
    }
}
