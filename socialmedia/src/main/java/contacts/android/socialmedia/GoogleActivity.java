package contacts.android.socialmedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class GoogleActivity extends Activity {

    TextView t1,t2,t3,t4,t5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.google_activity);

        Intent intent = getIntent();
        String personName = intent.getStringExtra("personName").toString();
        String personGivenName = intent.getStringExtra("personGivenName").toString();
        String personFamilyName = intent.getStringExtra("personFamilyName").toString();
        String personEmail = intent.getStringExtra("personEmail").toString();
        String personId = intent.getStringExtra("personId").toString();

        t1=(TextView) findViewById(R.id.t1);t1.setText(personName);
        t2=(TextView) findViewById(R.id.t2);t2.setText(personGivenName);
        t3=(TextView) findViewById(R.id.t3);t3.setText(personFamilyName);
        t4=(TextView) findViewById(R.id.t4);t4.setText(personEmail);
        t5=(TextView) findViewById(R.id.t5);t5.setText(personId);

    }
}
