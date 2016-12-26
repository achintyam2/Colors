package com.android.pinquestionnairelogs;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class QuestionnaireVerification extends Activity {

    String oneAnswer, twoAnswer, threeAnswer, oneAnswered, twoAnswered, threeAnswered,qId1,qId2,qId3,aId1,aId2,aId3;;
    EditText a1,a2,a3;
    Button check;
    int oneSizeAnswered, twoSizeAnswered, threeSizeAnswered;
    TextView q1,q2,q3;
    int sizeOfArray;
    Set<String> tags,questions,answers,aIds,qIds;
    StringTokenizer stringTokenizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_verification);

        Intent intent = getIntent();

        a1 = (EditText) findViewById(R.id.a1);
        a2 = (EditText) findViewById(R.id.a2);
        a3 = (EditText) findViewById(R.id.a3);
        check = (Button) findViewById(R.id.check);
        q1 = (TextView) findViewById(R.id.q1);
        q2 = (TextView) findViewById(R.id.q2);
        q3 = (TextView) findViewById(R.id.q3);

        tags =  new HashSet<>();
        questions =  new HashSet<>();
        answers =  new HashSet<>();
        aIds =  new HashSet<>();
        qIds =  new HashSet<>();

        oneAnswered ="";
        twoAnswered ="";
        threeAnswered ="";

        stringTokenizer =new StringTokenizer(getResources().getString(R.string.ques1));
        qId1 = stringTokenizer.nextToken(":");
        stringTokenizer =new StringTokenizer(getResources().getString(R.string.ques2));
        qId2 = stringTokenizer.nextToken(":");
        stringTokenizer =new StringTokenizer(getResources().getString(R.string.ques3));
        qId3 = stringTokenizer.nextToken(":");

        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        tags = prefs.getStringSet("tags",tags);
        questions = prefs.getStringSet("questions",questions);
        answers = prefs.getStringSet("answers",answers);
        qIds = prefs.getStringSet("qIds",qIds);

        Log.d("aa","answers "+answers);

        Object[] array =  questions.toArray();
        sizeOfArray = array.length;
        Log.d("aa","sizeAray"+sizeOfArray);

        if(sizeOfArray ==2) {
            q1.setText(array[0].toString());q1.setVisibility(View.VISIBLE);a1.setVisibility(View.VISIBLE);
            q2.setText(array[1].toString());q2.setVisibility(View.VISIBLE);a2.setVisibility(View.VISIBLE);
        }
        else
        {
            q1.setText(array[0].toString());q1.setVisibility(View.VISIBLE);a1.setVisibility(View.VISIBLE);
            q2.setText(array[1].toString());q2.setVisibility(View.VISIBLE);a2.setVisibility(View.VISIBLE);
            q3.setText(array[2].toString());q3.setVisibility(View.VISIBLE);a3.setVisibility(View.VISIBLE);
        }

        if(tags.contains("one")) {
            oneAnswered = prefs.getString("one", "");
        }
        if(tags.contains("two")) {
            twoAnswered = prefs.getString("two", "");
        }
        if(tags.contains("three")) {
            threeAnswered = prefs.getString("three", "");
        }

        oneSizeAnswered = oneAnswered.length();
        twoSizeAnswered = twoAnswered.length();
        threeSizeAnswered = threeAnswered.length();

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oneAnswer = a1.getText().toString();
                twoAnswer = a2.getText().toString();
                threeAnswer = a3.getText().toString();

                if(oneAnswer.length()>0) {
                    oneAnswer = "1:".concat(oneAnswer);
                    stringTokenizer =new StringTokenizer(oneAnswer);
                    aId1 = stringTokenizer.nextToken(":");
                    aIds.add(aId1);
                }
                if(twoAnswer.length()>0) {
                    twoAnswer = "2:".concat(twoAnswer);
                    stringTokenizer =new StringTokenizer(twoAnswer);
                    aId2 = stringTokenizer.nextToken(":");
                    aIds.add(aId2);
                }
                if(threeAnswer.length()>0) {
                    threeAnswer = "3:".concat(threeAnswer);
                    stringTokenizer =new StringTokenizer(threeAnswer);
                    aId3 = stringTokenizer.nextToken(":");
                    aIds.add(aId3);
                }

                Log.d("aa","aIds "+aIds);

                    if ( aIds.contains(qId1))
                    {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("result","Info Correct");
                        setResult(QuestionnaireVerification.RESULT_OK,resultIntent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Verification Code doesn't match.", Toast.LENGTH_SHORT).show();
                    }
            }
        });
    }
}
