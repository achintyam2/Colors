package com.android.newpinscreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

public class QuestionnaireRegistration extends Activity {

    EditText answer1,answer2,answer3;
    Button submit;
    String one,two,three,oneTag,twoTag,threeTag,aId1,aId2,aId3;
    private int count;
    Set<String> tags,questions,answers,qIds;
    StringTokenizer  stringTokenizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questionnaire_registration);

        Intent intent = getIntent();

        answer1 = (EditText)findViewById(R.id.answer1);
        answer2 = (EditText)findViewById(R.id.answer2);
        answer3 = (EditText)findViewById(R.id.answer3);
        submit = (Button) findViewById(R.id.submit);


        tags = new HashSet<>();
        questions = new HashSet<>();
        answers = new HashSet<>();
        qIds = new HashSet<>();


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                count =0;
                one = answer1.getText().toString();
                two = answer2.getText().toString();
                three = answer3.getText().toString();

                if(one!=null)
                    one="1:".concat(one);
                if(two!=null)
                    two="2:".concat(two);
                if(three!=null)
                    three="3:".concat(three);

                stringTokenizer =new StringTokenizer(one);
                aId1 = stringTokenizer.nextToken(":");
                stringTokenizer =new StringTokenizer(two);
                aId2 = stringTokenizer.nextToken(":");
                stringTokenizer =new StringTokenizer(three);
                aId3 = stringTokenizer.nextToken(":");

                int size1 = one.length();
                int size2 = two.length();
                int size3 = three.length();

                if(size1>2) {
                    oneTag = (String)answer1.getTag();
                    questions.add(getResources().getString(R.string.ques1));
                    answers.add(one);
                    qIds.add("1");
                    count+=1;
                }
                if(size2>2) {
                    twoTag = (String)answer2.getTag();
                    questions.add(getResources().getString(R.string.ques2));
                    answers.add(two);
                    count+=1;
                    qIds.add("2");
                }
                if(size3>2) {
                    threeTag = (String)answer3.getTag();
                    questions.add(getResources().getString(R.string.ques3));
                    answers.add(three);
                    count+=1;
                    qIds.add("3");
                }

                if(count>1) {
                    if(!tags.contains(oneTag)) {
                        tags.add(oneTag);
                    }

                    if(!tags.contains(twoTag)) {
                        tags.add(twoTag);
                    }
                    if(!tags.contains(threeTag)) {
                        tags.add(threeTag);
                }
                    saveAnswers(one,two,three);
                    goBackToMain();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Atleast enter two answers.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void saveAnswers(String answer1,String answer2,String answer3) {
        SharedPreferences prefs = getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(oneTag, answer1);
        editor.putString(twoTag, answer2);
        editor.putString(threeTag, answer3);
        editor.putStringSet("questions", questions);
        Log.d("aa","answers "+answers);
        Log.d("aa","qIds "+qIds);
        editor.putStringSet("qIds ", qIds);
        editor.putStringSet("answers", answers);
        editor.putStringSet("tags", tags);
        editor.apply();
    }

    public void goBackToMain()
    {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("registered", "Registered");
        setResult(QuestionnaireRegistration.RESULT_OK, resultIntent);
        finish();
    }

}
