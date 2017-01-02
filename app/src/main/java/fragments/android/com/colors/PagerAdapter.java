package fragments.android.com.colors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.android.calllogs.CallLogsFragment;

import contacts.android.socialmedia.FacebookIntegration;
import contacts.android.socialmedia.GmailIntegration;
import contacts.android.socialmedia.GoogleIntegration;



public class PagerAdapter extends FragmentStatePagerAdapter {

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
            CallLogsFragment tab0 = new CallLogsFragment();
            return tab0;
        case 1:
            FacebookIntegration tab1 = new FacebookIntegration();
            return tab1;
        case 2:
            if (tab2 == null)
                tab2 = new GmailIntegration();
            return tab2;
        case 3:
            GoogleIntegration tab3 = new GoogleIntegration();
            return tab3;
        case 4:
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
