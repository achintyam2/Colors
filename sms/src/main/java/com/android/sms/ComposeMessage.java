package com.android.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;


public class ComposeMessage extends Activity {

    EditText bodyEditText,contactEditText;
    ImageButton sim1,sim2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.compose_message);
        Intent intent = getIntent();

        bodyEditText = (EditText) findViewById(R.id.bodyText);
        contactEditText = (EditText) findViewById(R.id.contactText);
        sim1 = (ImageButton) findViewById(R.id.sim1);
        sim2 = (ImageButton) findViewById(R.id.sim2);

        String sms_body = intent.getStringExtra("sms_body");

        bodyEditText.setText(sms_body);


    }
}
