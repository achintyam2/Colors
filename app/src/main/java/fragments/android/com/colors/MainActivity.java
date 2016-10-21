package fragments.android.com.colors;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ColorsDialogFragment.DialogFragmentClickHandler   {

    Button button;
    private static final String TAG="MainActivity";
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         text = (TextView)findViewById(R.id.text);
        button = (Button)findViewById(R.id.button) ;
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                showDialog();

            }
        });
    }
        void showDialog() {
            //FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ColorsDialogFragment newFragment = ColorsDialogFragment.newInstance();
            newFragment.show(ft,"title");

        }

    @Override
    public void onDialogFragmentClicked(int colorValue) {
        Toast.makeText(getApplicationContext(),"Value: "+colorValue,Toast.LENGTH_LONG).show();

    }

}
