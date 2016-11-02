/*
package fragments.android.com.colors;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.IOException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;



public class ThirdFragment extends Activity implements EasyPermissions.PermissionCallbacks {

    private int count =0;
    ImageView imageView;
    Message message1;
    GoogleAccountCredential mCredential;
    private TextView mOutputText,mSnippetText;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    final String TAG = "ThirdFragment";
    private String name, subjectValue;
    String snipetMessage=null;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.gmail_integration);

            Intent intent = getIntent();
            intent.getStringExtra("HelloGmail");

            imageView = (ImageView) findViewById(R.id.i1);
            Button mCallApiButton = (Button)findViewById(R.id.api_button);
            mOutputText = (TextView) findViewById(R.id.contact_name);
            mSnippetText = (TextView) findViewById(R.id.snippet);



            mCallApiButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    getResultsFromApi();

                }
            });



            mCredential = GoogleAccountCredential.usingOAuth2(
                    getApplicationContext(), Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff());

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent gmailIntent = new Intent(Intent.ACTION_VIEW);
                    gmailIntent.setType("message/rfc822");
                    startActivity(gmailIntent);
                }
            });
        }

    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText("No network connection available.");
        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(getApplicationContext(), Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(this, "This app needs to access your Google account (via Contacts).", REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            switch(requestCode) {
                case REQUEST_GOOGLE_PLAY_SERVICES:
                    if (resultCode != RESULT_OK) {
                        mOutputText.setText("This app requires Google Play Services. Please install " + "Google Play Services on your device and relaunch this app.");
                    } else {
                        getResultsFromApi();
                    }
                    break;
                case REQUEST_ACCOUNT_PICKER:
                    if (resultCode == RESULT_OK && data != null &&
                            data.getExtras() != null) {
                        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                        if (accountName != null) {
                            SharedPreferences settings = getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(PREF_ACCOUNT_NAME, accountName);
                            editor.apply();
                            mCredential.setSelectedAccountName(accountName);
                            getResultsFromApi();
                        }
                    }
                    break;
                case REQUEST_AUTHORIZATION:
                    if (resultCode == RESULT_OK) {
                        getResultsFromApi();
                    }
                    break;
            }
        }







    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(getApplicationContext());
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(ThirdFragment.this, connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(transport, jsonFactory, credential)
                    .setApplicationName("Gmail API Android Quickstart")
                    .build();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String user = "me";
                List<String> str = new ArrayList<String>();
                str.add("INBOX");
                return  getDataFromApi(mService,user,"vikas@contacts-studio.com");
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        private String getDataFromApi(Gmail service, String userId, String query) throws IOException, JSONException {

            ListMessagesResponse response = service.users().messages().list(userId).setQ(query).execute();
            List<Message> messages = new ArrayList<Message>();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(userId).setQ(query).setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            int arraySize = messages.size();
            Log.d(TAG,"ArraySize= "+arraySize);
            for (Message message : messages) {
                //Log.d(TAG,"Message: "+message);
                count++;
                JSONObject jsonObject = new JSONObject(message.toString());
                String idValue = (String) jsonObject.get("id");

                if (count ==1) {
                    Log.d(TAG, "ID: " + idValue);
                    message1 = getMessage(mService, "me", idValue);
                    snipetMessage = message1.getSnippet();
                    break;
                }
            }
            return snipetMessage;
        }

       //Get Message witih given ID.
        public  Message getMessage(Gmail service, String userId, String messageId) throws IOException {
            Message message = service.users().messages().get(userId, messageId).execute();
            Log.d(TAG,"MessageSnippet : " + message.getSnippet());
            List<MessagePartHeader>  l = message.getPayload().getHeaders();
            for(MessagePartHeader messagePartHeader: l){
                name = messagePartHeader.getName();
                if("Subject".equals(name)){
                    subjectValue = messagePartHeader.getValue();
                }
            }
            return message;
        }

        */
/*private  List<Message> getDataFromApi(Gmail service, String userId,
                                              List<String> labelIds) throws IOException {
            ListMessagesResponse response = service.users().messages().list(userId)
                    .setLabelIds(labelIds).execute();

            List<Message> messages = new ArrayList<>();
            while (response.getMessages() != null) {
                messages.addAll(response.getMessages());
                if (response.getNextPageToken() != null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(userId).setLabelIds(labelIds)
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }
            }

            for (Message message : messages) {

//                messages.add(message.clone());
                Log.d("Message","Message: "+message);
                Log.d("Message","Messages: "+messages);
            }

           return messages;
        }*//*


        @Override
        protected void onPreExecute() {
            mOutputText.setText("");
            //mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            //mProgress.hide();
            if (output == null) {
                //mOutputText.setText("No results returned.");
            } else {
                //output.add(0, "Data retrieved using the Gmail API:");
                imageView.setVisibility(View.VISIBLE);
                mOutputText.setText(subjectValue);
                mSnippetText.setText(output);
                //Log.d(TAG,"Output"+ output);
            }
        }

        @Override
        protected void onCancelled() {
            //mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(((GooglePlayServicesAvailabilityIOException) mLastError).getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(((UserRecoverableAuthIOException) mLastError).getIntent(), REQUEST_AUTHORIZATION);
                } else {
                    mOutputText.setText("The following error occurred:\n" + mLastError.getMessage());
                }
            } else {
                mOutputText.setText("Request cancelled.");
            }
        }
    }
    }
*/
