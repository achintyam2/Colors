package fragments.android.com.colors;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ColorsDialogFragment.DialogFragmentClickHandler   {

    Button button;
    private static final String TAG="MainActivity";
    TextView text;
    Toolbar myToolbar;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();

        text = (TextView)findViewById(R.id.text);
        button = (Button)findViewById(R.id.button) ;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                //FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ColorsDialogFragment newFragment = ColorsDialogFragment.newInstance();
                newFragment.show(ft,"title");

            }
        });
    }


    @Override
    public void onDialogFragmentClicked(int colorValue) {
        ColorDrawable colorDrawable = new ColorDrawable();
        Log.d(TAG,"setBackgroundColor: "+colorValue);
        colorDrawable.setColor(colorValue);
        actionBar.setBackgroundDrawable(colorDrawable);
        //Toast.makeText(getApplicationContext(),"Value: "+colorValue,Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                   //Creating Menu Options
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
