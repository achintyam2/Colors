package contacts.android.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONException;

public class FacebookIntegration extends Fragment {

    protected LoginButton fbButton;
    private CallbackManager callbackManager;
    final String TAG = "FacebookIntegration";
    Context context;


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.facebook_integration, container, false);

        callbackManager = CallbackManager.Factory.create();
        context = this.getContext();

        fbButton = (LoginButton) v.findViewById(R.id.fb_button);
        fbButton.setReadPermissions("user_friends");                    //Setting the read permissions to request to the user
        fbButton.setReadPermissions("email");
        fbButton.setFragment(this);                                     //Setting the fragment
        fbButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {  //Registering a callback after the login button is pressed
            @Override
            public void onSuccess(final LoginResult login_result) {
                Log.d(TAG, "t1 : " + login_result.getAccessToken().getToken());
                Log.d(TAG, "t2 : " + AccessToken.getCurrentAccessToken().getToken());

                FacebookSdk.addLoggingBehavior(LoggingBehavior.REQUESTS);

                //TODO Retrieving Complete FriendList of the user
                GraphRequest request = GraphRequest.newGraphPathRequest(    //Creating a new request
                        AccessToken.getCurrentAccessToken(),
                        "/me/friends/",
                        new GraphRequest.Callback() {
                            @Override
                            public void onCompleted(GraphResponse response) {
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    Log.d(TAG,"List : "+"\n"+rawName);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();                      //Creating a bundle
                parameters.putString("fields", "name");                //Setting parameters to the bundle
                request.setParameters(parameters);                     //Setting parameters to the particular request
                request.executeAsync();

            /*GraphRequest request = GraphRequest.newMyFriendsRequest(

                     login_result.getAccessToken(),
                    //AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONArrayCallback() {
                        @Override
                        public void onCompleted(JSONArray object, GraphResponse response) {
                            Intent intent = new Intent(context, FriendsList.class);
                            try {
                                Log.d(TAG,"ID : "+ login_result.getAccessToken());
                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                intent.putExtra("jsondata", rawName.toString());
                                startActivity(intent);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "name,picture");
            request.setParameters(parameters);
            request.executeAsync();*/
            }

            @Override
            public void onCancel() {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(FacebookException e) {
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        LoginManager.getInstance().logOut();                            //Logging out the fb account after the completion of the process
    }
}

