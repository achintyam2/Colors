package com.android.newpinscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class QuestionnaireMain extends Activity {

    Button register, verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_main);

        register = (Button) findViewById(R.id.register);
        verify = (Button) findViewById(R.id.verify);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(QuestionnaireMain.this,QuestionnaireRegistration.class);
                startActivityForResult(registerIntent,0);
            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verifyIntent = new Intent(QuestionnaireMain.this,QuestionnaireVerification.class);
                startActivityForResult(verifyIntent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==0) {
            if (resultCode == RESULT_OK) {
                String result =  data.getExtras().getString("registered");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 1)
        {
            if (resultCode == RESULT_OK) {
                String result =  data.getExtras().getString("result");
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
