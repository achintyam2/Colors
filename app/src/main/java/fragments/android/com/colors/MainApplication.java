package fragments.android.com.colors;

import android.app.Application;
import com.facebook.FacebookSdk;

/**
 * Created by Achintya on 27-10-2016.
 */
public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}
