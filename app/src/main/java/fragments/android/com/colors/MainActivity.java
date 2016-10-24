package fragments.android.com.colors;

import android.content.Context;
import android.os.Build;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
        setStatusBarColor(colorValue);
        //Toast.makeText(getApplicationContext(),"Value: "+colorValue,Toast.LENGTH_LONG).show();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                   //Creating Menu Options
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_favorite:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }*/


    private void setStatusBarColor(int color) {

        try {
            int bg_color = getDarkerShade(color);
            if (bg_color == -1) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(bg_color);
            }
        } catch (Exception e) {
        }
    }

    public int getDarkerShade(int color){
        double fraction = 0.25;
        int red = (int) Math.round(Math.max(0, Color.red(color) - 255 * fraction));
        int green = (int) Math.round(Math.max(0, Color.green(color) - 255 * fraction));
        int blue = (int) Math.round(Math.max(0, Color.blue(color) - 255 * fraction));

        int alpha = Color.alpha(color);

        int darkColor = Color.argb(alpha, red, green, blue);
        return darkColor;
    }

}
