package fragments.android.com.colors;

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
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Achintya on 26-10-2016.
 */
public class SecondFragment extends Fragment {
    LoginButton loginButton;
    CallbackManager callbackManager;
    final String TAG ="SecondFragment";
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult login_result) {

 new GraphRequest(

                    //login_result.getAccessToken(),
                    AccessToken.getCurrentAccessToken(),
                    "/me/taggable_friends",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback()
                    {
                        public void onCompleted(GraphResponse response) {
                            Intent intent = new Intent(getContext(),FriendsList.class);

                            try {

                                JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                Log.d(TAG,"rawName"+rawName);
                                intent.putExtra("jsondata", rawName.toString());
                                startActivity(intent);
                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();

        }

        @Override
        public void onCancel() {
            Toast.makeText(getContext(), "Cancel" ,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException e) {
            Toast.makeText(getContext(), "Error: "+e ,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         callbackManager = CallbackManager.Factory.create();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
