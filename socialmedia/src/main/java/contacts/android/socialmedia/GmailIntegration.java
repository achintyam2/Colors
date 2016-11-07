package contacts.android.socialmedia;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import static android.app.Activity.RESULT_OK;



public class GmailIntegration extends Fragment implements EasyPermissions.PermissionCallbacks {

    private Boolean flag = true;
    private int count = 0;
    int countAttachment =0;
    ImageView imageView,attachmentView;
    Message message1;
    GoogleAccountCredential mCredential;
    private TextView mOutputText, mSnippetText, mDetailsText,mAttachmentText;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = {GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_READONLY};
    final String TAG = "ThirdFragment";
    private String subjectValue,fromValue,toValue,dateValue,out,atatchmentValue;
    Context context;
    private ProgressBar mProgress;


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if(savedInstanceState != null)               //Retrieving the mail when orientation changes
        {
            String getSnippet = savedInstanceState.getString("snippet");
            String getAttachment = savedInstanceState.getString("attachment");
            String getSubject = savedInstanceState.getString("subject");
            String getSender = savedInstanceState.getString("from");
            String getReceiver = savedInstanceState.getString("to");
            String getDate = savedInstanceState.getString("date");
            int countAttach = savedInstanceState.getInt("countAttachment");
            imageView.setVisibility(View.VISIBLE);
            if(countAttach>0)
            {
                attachmentView.setVisibility(View.VISIBLE);
                if(countAttach==1)
                    mAttachmentText.setText(getAttachment + getResources().getString(R.string.attachment));
                else
                    mAttachmentText.setText(getAttachment + getResources().getString(R.string.attachments));
            }
            mOutputText.setText(getSubject);
            mDetailsText.setText(getResources().getString(R.string.from)+getSender +"\n"+getResources().getString(R.string.to) +getReceiver +"\n"+getResources().getString(R.string.on)+getDate);
            mSnippetText.setText(getSnippet);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.gmail_integration, container, false);

        this.context = getActivity().getApplicationContext();

        mProgress = (ProgressBar) v.findViewById(R.id.progressBar);

        imageView = (ImageView) v.findViewById(R.id.i1);
        attachmentView = (ImageView)v.findViewById(R.id.attachment_symbol);
        Button mCallApiButton = (Button)v.findViewById(R.id.api_button);
        mOutputText = (TextView) v.findViewById(R.id.contact_name);
        mSnippetText = (TextView) v.findViewById(R.id.snippet);
        mDetailsText = (TextView) v.findViewById(R.id.details);
        mAttachmentText = (TextView) v.findViewById(R.id.attachment);

        mCredential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
        mCallApiButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getResultsFromApi();
            }
        });


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO onCLick it should open gmail app to view the email
                Intent gmailIntent = new Intent(Intent.ACTION_VIEW);
                gmailIntent.setType("message/rfc822");
                startActivity(gmailIntent);
            }
        });
        return v;
    }


    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {

        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText(getResources().getString(R.string.no_network));
        } else {
                if (flag) {
            new GmailIntegration.MakeRequestTask(mCredential).execute();
            //mCredential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
                    flag = false;
                }
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(context, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = this.getActivity().getPreferences(Context.MODE_PRIVATE).getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(mCredential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.needs_google_access), REQUEST_PERMISSION_GET_ACCOUNTS, Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    mOutputText.setText(getResources().getString(R.string.install_gps));
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings = (SharedPreferences) this.getActivity().getPreferences(Context.MODE_PRIVATE);
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

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
        private boolean isDeviceOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(getActivity() , connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Gmail API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.gmail.Gmail mService = null;
        private Exception mLastError = null;

        public MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.gmail.Gmail.Builder(transport, jsonFactory, credential)
                    .setApplicationName(getResources().getString(R.string.gmail_api_name))
                    .build();
        }

        /**
         * Background task to call Gmail API.
         * @param params no parameters needed for this task.
         */
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

        /**
         * Fetch a latest email from gmail to the specified account.
         * @return String of email.
         * @throws IOException
         */
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
            String snipetMessage=null;
            int arraySize = messages.size();
            Log.d(TAG,getResources().getString(R.string.array_size)+arraySize);
            for (Message message : messages) {
                //Log.d(TAG,"Message: "+message);
                count++;
                JSONObject jsonObject = new JSONObject(message.toString());
                String idValue = (String) jsonObject.get("id");

                if (count == 1) {
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
            List<MessagePart> parts = message.getPayload().getParts();
            for (MessagePart part : parts) {
                if (part.getFilename() != null && part.getFilename().length() > 0) {
                    countAttachment++;
                    String attId = part.getBody().getAttachmentId();
                    MessagePartBody attachPart = service.users().messages().attachments().get(userId, messageId, attId).execute();
                    String attachment = attachPart.toString();
                    Log.d(TAG,"AttachmentId " + attId);
                    Log.d(TAG,"messagePartBody " + attachment);
                }
            }
            List<MessagePartHeader>  headers = message.getPayload().getHeaders();
            for(MessagePartHeader messagePartHeader: headers){
                String name = messagePartHeader.getName();
                if("Subject".equals(name)){
                    subjectValue = messagePartHeader.getValue();
                }
                if("From".equals(name)){
                    fromValue = messagePartHeader.getValue();
                }
                if("To".equals(name)){
                    toValue = messagePartHeader.getValue();
                }
                if("Date".equals(name)){
                    dateValue = messagePartHeader.getValue();
                }

            }
            return message;
        }

         /*private  List<Message> getDataFromApi(Gmail service, String userId, List<String> labelIds) throws IOException {
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
        }*/


        @Override
        protected void onPreExecute() {
            mProgress.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            attachmentView.setVisibility(View.GONE);
            mOutputText.setText("");
            mAttachmentText.setText("");
            mDetailsText.setText("");
            mSnippetText.setText("");
            //mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {

            out = output;
            //mProgress.hide();
            if (output == null) {
                //mOutputText.setText("No results returned.");
            } else {
                //output.add(0, "Data retrieved using the Gmail API:");

                atatchmentValue = Integer.toString(countAttachment);

                mProgress.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                if(countAttachment>0)
                {
                    attachmentView.setVisibility(View.VISIBLE);
                    if(countAttachment==1)
                    mAttachmentText.setText(atatchmentValue + getResources().getString(R.string.attachment));
                    else
                        mAttachmentText.setText(atatchmentValue + getResources().getString(R.string.attachments));
                }
                mOutputText.setText(subjectValue);
                mDetailsText.setText(getResources().getString(R.string.from)+fromValue +"\n"+getResources().getString(R.string.to) +toValue +"\n"+getResources().getString(R.string.on)+dateValue);
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
                    mOutputText.setText(getResources().getString(R.string.error)+"\n" + mLastError.getMessage());
                }
            } else {
                mOutputText.setText(getResources().getString(R.string.request_cancelled));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {  //Saving the mail value of the item to use it when orientation changes
        super.onSaveInstanceState(outState);
        outState.putString("snippet",out);
        outState.putString("attachment",atatchmentValue);
        outState.putString("subject",subjectValue);
        outState.putString("from",fromValue);
        outState.putString("to",toValue);
        outState.putString("date",dateValue);
        outState.putInt("countAttachment",countAttachment);
    }
}
