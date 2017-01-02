package fragments.android.com.colors;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import contacts.android.themeselector.ColorsDialogFragment;



public class MainActivity extends AppCompatActivity implements ColorsDialogFragment.DialogFragmentClickHandler {

    DialerFragment dialerFragment;
    PagerAdapter pagerAdapter;
    int position;
    ViewPager viewPager;
    int settingColor;
    private static final String TAG = "MainActivity";
    Toolbar myToolbar;
    TabLayout tabLayout;
    Context context;
    ActionBar actionBar;
    private boolean flag = false;

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
        tabLayout.addTab(tabLayout.newTab().setText("Call Logs"));   //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Facebook"));   //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Gmail"));      //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Google"));     //Adding the tab to the viewpager
        tabLayout.addTab(tabLayout.newTab().setText("Dialer"));     //Adding the tab to the viewpager
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);            //Setting the gravity to use when laying out the tabs.
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary)); //Setting the background colour the the default tab
        setStatusBarColor(R.color.colorPrimary);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount()); //Setting the viewpager through PagerAdapter accroding to the getTabCount() value
        viewPager.setAdapter(pagerAdapter);        //Setting the adapter to the viewPager

        //tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout)); //Changing from one tab to another
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                position = tab.getPosition();
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
            e.printStackTrace();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                   //Creating Menu Options
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_themes:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ColorsDialogFragment newFragment = ColorsDialogFragment.newInstance();
                newFragment.show(ft, "title");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    @Override
    public void onBackPressed(){
        if(position ==4) {
            pagerAdapter.getItem(position);
            DialerFragment dialerFragment = (DialerFragment) viewPager.getAdapter().instantiateItem(viewPager, position);
            dialerFragment.dialer.setVisibility(View.GONE);
            dialerFragment.keypadShow.setVisibility(View.VISIBLE);
            dialerFragment.keypadHide.setVisibility(View.GONE);
            flag = true;
        }
        else if (flag)
        {
            finish();
        }
        else
            finish();
    }
}
