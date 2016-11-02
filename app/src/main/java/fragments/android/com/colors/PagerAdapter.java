package fragments.android.com.colors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import contacts.android.socialmedia.FacebookIntegration;
import contacts.android.socialmedia.GmailIntegration;
import contacts.android.socialmedia.GoogleIntegration;



public class PagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public PagerAdapter(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
    switch (position) {

        case 0:
            FirstFragment tab1 = new FirstFragment();
            return tab1;
        case 1:
            FacebookIntegration tab2 = new FacebookIntegration();
            return tab2;
        case 2:
            GmailIntegration tab3 =   new GmailIntegration();
            return tab3;
        case 3:
            GoogleIntegration tab4 = new GoogleIntegration();
            return tab4;
        default:
            return null;
    }
}

    @Override
    public int getCount() {
        return tabCount;
    }


}
