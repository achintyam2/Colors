package contacts.android.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;


public class GoogleIntegration extends Fragment {
    private GoogleApiClient mGoogleApiClient;
    private int RC_SIGN_IN = 100;
    final String TAG = "GoogleIntegration";
    Context context;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.google_integration, container, false);

        context = getActivity().getApplicationContext();

        // Configure sign-in to request the user's ID, email address, and basic profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        if(mGoogleApiClient ==  null) {
            // Build a GoogleApiClient with access to the Google Sign-In API and the options specified by gso.
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .enableAutoManage(getActivity() /* FragmentActivity */, null /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }

        // Customize sign-in button. The sign-in button can be displayed in multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the difference.
        SignInButton signInButton = (SignInButton) v.findViewById(R.id.google_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        return  v;
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            //Uri personPhoto = acct.getPhotoUrl();

            Intent intent = new Intent(context, GoogleActivity.class);
            intent.putExtra("personName",personName);
            intent.putExtra("personGivenName",personGivenName);
            intent.putExtra("personFamilyName",personFamilyName);
            intent.putExtra("personEmail",personEmail);
            intent.putExtra("personId",personId);
            startActivity(intent);
            Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show();

            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            //updateUI(false);
        }
    }

}