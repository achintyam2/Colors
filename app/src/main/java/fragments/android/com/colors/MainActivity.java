package fragments.android.com.colors;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity implements ColorsDialogFragment.DialogFragmentClickHandler   {


    private static final String TAG="MainActivity";
    Toolbar myToolbar;
    ActionBar actionBar;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(myToolbar);
        actionBar = getSupportActionBar();

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));


        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount()));
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void onDialogFragmentClicked(int colorValue) {
        ColorDrawable colorDrawable = new ColorDrawable();
        Log.d(TAG,"setBackgroundColor: "+colorValue);
        colorDrawable.setColor(colorValue);
        actionBar.setBackgroundDrawable(colorDrawable);
//        actionBar.setDisplayHomeAsUpEnabled(true);
        setStatusBarColor(colorValue);
        tabLayout.setBackgroundColor(colorValue);
    }

    private void setStatusBarColor(int color) {

        try {
            int bg_color = getDarkerShade(color);
            if (bg_color == -1) return;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = this.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        int mNumTabs;
        public MyPagerAdapter(FragmentManager fm,int numTabs) {
            super(fm);
            this.mNumTabs=numTabs;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {

                case 0:
                    FirstFragment tab1 = new FirstFragment();
                    return tab1;
                case 1:
                    SecondFragment tab2 = new SecondFragment();
                    return tab2;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return mNumTabs;
        }
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

}
