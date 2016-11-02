package contacts.android.socialmedia;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class FacebookIntegration extends Fragment{

    protected LoginButton fbButton;
    private CallbackManager callbackManager;
    final String TAG = "SecondFragment";
    Context context;

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult login_result) {

            new GraphRequest(

                    //login_result.getAccessToken(),
                    AccessToken.getCurrentAccessToken(),
                    "/"+ login_result.getAccessToken().getUserId()+"/friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            Intent intent = new Intent(context, FriendsList.class);

                            try {

                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                Log.d(TAG, "rawName" + rawName);
                                intent.putExtra("jsondata", rawName.toString());
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

        }

        @Override
        public void onCancel() {
            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.facebook_integration, container, false);
        return v;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fbButton = (LoginButton) view.findViewById(R.id.fb_button);
        fbButton.setReadPermissions("user_friends");
        fbButton.setFragment(this);
        fbButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        LoginManager.getInstance().logOut();
    }

    /**
     * Created by Achintya on 27-10-2016.
     */
    public static class MainApplication extends Application {

        @Override
        public void onCreate() {
            super.onCreate();
            FacebookSdk.sdkInitialize(getApplicationContext());
        }
    }

    public static class FriendsList extends Activity {

        final String TAG = "FriendsList";
        @Override
        public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);
        Intent intent = getIntent();

        String jsondata = intent.getStringExtra("jsondata");
        JSONArray friendslist;
        ArrayList<String> friends = new ArrayList<String>();


        try {

            friendslist = new JSONArray(jsondata);
            Log.d(TAG,"friends: "+friendslist.length());
            for (int i=0; i < friendslist.length(); i++) {
                friends.add(friendslist.getJSONObject(i).getString("name"));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_listview, friends); // simple textview for list item
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    }
}
