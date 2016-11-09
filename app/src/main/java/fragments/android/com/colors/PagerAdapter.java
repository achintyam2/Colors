package fragments.android.com.colors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import contacts.android.socialmedia.FacebookIntegration;
import contacts.android.socialmedia.GmailIntegration;
import contacts.android.socialmedia.GoogleIntegration;



public class PagerAdapter extends FragmentPagerAdapter {

    GmailIntegration tab2;
    private static final String TAG = "PagerAdapter";
    int mNumTabs;

    public PagerAdapter(FragmentManager fm, int numTabs) {
        super(fm);
        this.mNumTabs= numTabs;
    }

    @Override
    public Fragment getItem(int position) {
    switch (position) {

        case 0:
            Log.d(TAG, " here: 0");
            FacebookIntegration tab1 = new FacebookIntegration();
            return tab1;
        case 1:
            Log.d(TAG, " here: 1");
            if (tab2 == null)
                tab2 = new GmailIntegration();
            return tab2;
        case 2:
            Log.d(TAG, " here: 2");
            GoogleIntegration tab3 = new GoogleIntegration();
            return tab3;
        case 3:
            Log.d(TAG, " here: 3");
            DialerFragment tab4 = new DialerFragment();
            return tab4;
        default:
            return null;
    }

}

    @Override
    public int getCount() {
        return mNumTabs;
    }

}
