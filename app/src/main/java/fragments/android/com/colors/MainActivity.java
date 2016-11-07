package fragments.android.com.colors;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import contacts.android.socialmedia.FacebookIntegration;
import contacts.android.socialmedia.GmailIntegration;
import contacts.android.socialmedia.GoogleIntegration;
import contacts.android.themeselector.ColorsDialogFragment;

public class MainActivity extends AppCompatActivity implements ColorsDialogFragment.DialogFragmentClickHandler {

    ViewPager viewPager;
    int settingColor;
    private static final String TAG = "MainActivity";
    Toolbar myToolbar;
    TabLayout tabLayout;
    Context context;
    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;                  //Setting the context to this activity

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(myToolbar);                          //Designating a Toolbar as the action bar for an Activity
        actionBar = getSupportActionBar();                       //Retrieve an instance of ActionBar by calling getSupportActionBar()

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Facebook"));   //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Gmail"));      //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Google"));     //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Themes"));     //Adding the tab to the viewpager
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);            //Setting the gravity to use when laying out the tabs.
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //Setting the background colour the the default tab

        if(savedInstanceState != null)              //When the orientation changes retrieving back the saved instances
        {

            String getSnippet = savedInstanceState.getString("snippet");
            Log.d(TAG,"getSnippet "+getSnippet);
            String getAttachment = savedInstanceState.getString("attachment");
            String getSubject = savedInstanceState.getString("subject");
            String getSender = savedInstanceState.getString("from");
            String getReceiver = savedInstanceState.getString("to");
            String getDate = savedInstanceState.getString("date");
            settingColor = savedInstanceState.getInt("color");
            if(settingColor == 0)
            {
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
            else {
                setSupportActionBar(myToolbar);
                actionBar = getSupportActionBar();
                onDialogFragmentClicked(settingColor);              //Calling the method to set the colour to the actionBar
            }
        }

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()); //Setting the viewpager through PagerAdapter accroding to the getTabCount() value
        viewPager.setAdapter(pagerAdapter);        //Setting the adapter to the viewPager

        //tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout)); //Changing from one tab to another
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Toast.makeText(context, "Unselected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Toast.makeText(context, "Reselected",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDialogFragmentClicked(int colorValue) {

        ColorDrawable colorDrawable = new ColorDrawable();
        Log.d(TAG, "setBackgroundColor: " + colorValue);
        colorDrawable.setColor(colorValue);                 //Setting the color to the bar
        actionBar.setBackgroundDrawable(colorDrawable);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(colorValue);                      //Setting the color to the statusbar
        tabLayout.setBackgroundColor(colorValue);
        settingColor = colorValue;
    }

    private void setStatusBarColor(int color) {

        try {
            int bg_color = getDarkerShade(color);
            if (bg_color == -1) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {    //If the SDK version is >=21 then setting the colour of the status bar
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(bg_color);
            }
        } catch (Exception e) {
        }
    }

    public int getDarkerShade(int color) {
        double fraction = 0.25;
        int red = (int) Math.round(Math.max(0, Color.red(color) - 255 * fraction));
        int green = (int) Math.round(Math.max(0, Color.green(color) - 255 * fraction));
        int blue = (int) Math.round(Math.max(0, Color.blue(color) - 255 * fraction));

        int alpha = Color.alpha(color);

        int darkColor = Color.argb(alpha, red, green, blue);
        return darkColor;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    /*@Override
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
    @Override
    public void onSaveInstanceState(Bundle outState) {  //Saving the color value of the item to use it when orientation changes
        super.onSaveInstanceState(outState);
        outState.putInt("color", settingColor);

    }


}
