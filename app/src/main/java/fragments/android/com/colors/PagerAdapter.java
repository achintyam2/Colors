package fragments.android.com.colors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.android.calllogs.CallLogsFragment;
import com.android.sms.SMSInboxFragment;
import com.android.mms.MMSInboxFragment;
import contacts.android.socialmedia.FacebookIntegration;
import contacts.android.socialmedia.GmailIntegration;
import contacts.android.socialmedia.GoogleIntegration;
import com.android.dialpad.DialpadFragment;


public class PagerAdapter extends FragmentStatePagerAdapter {


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
            SMSInboxFragment tab0 = new SMSInboxFragment();
            return tab0;
        case 1:
            CallLogsFragment tab1 = new CallLogsFragment();
            return tab1;
        case 2:
            FacebookIntegration tab2 = new FacebookIntegration();
            return tab2;

        case 3:
            GmailIntegration   tab3 = new GmailIntegration();
            return tab3;
        case 4:
            GoogleIntegration tab4 = new GoogleIntegration();
            return tab4;
        case 5:
            DialerFragment tab5 = new DialerFragment();
            return tab5;
        case 6:
            MMSInboxFragment tab6 = new MMSInboxFragment();
            return tab6;
        default:
            return null;
    }

}

    @Override
    public int getCount() {
        return mNumTabs;
    }

}
