package fragments.android.com.colors;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Achintya on 20-10-2016.
 */
public class MainActivity extends FragmentActivity   {

    Button button;
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}
