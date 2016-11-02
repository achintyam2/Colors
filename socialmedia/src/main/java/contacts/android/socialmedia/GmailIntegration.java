package contacts.android.socialmedia;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
    private String subjectValue,fromValue,toValue,dateValue,atatchmentValue;
    Context context;
    private ProgressBar mProgress;

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
                Intent gmailIntent = new Intent(Intent.ACTION_VIEW);
                gmailIntent.setType("message/rfc822");
                startActivity(gmailIntent);
            }
        });
        return v;
    }


    private void getResultsFromApi() {


        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            mOutputText.setText(getResources().getString(R.string.no_network));
        } else {
                //if (flag) {
            new GmailIntegration.MakeRequestTask(mCredential).execute();
            //mCredential = GoogleAccountCredential.usingOAuth2(context, Arrays.asList(SCOPES)).setBackOff(new ExponentialBackOff());
                    //flag = false;
               // }
        }
    }

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
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(getActivity() , connectionStatusCode, REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

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
                    //String filename = part.getFilename();
                    countAttachment++;
                    String attId = part.getBody().getAttachmentId();
                    MessagePartBody attachPart = service.users().messages().attachments().get(userId, messageId, attId).execute();
                    String attachment = attachPart.toString();
                    Log.d(TAG,"AttachmentId " + attId);
                    Log.d(TAG,"messagePartBody " + attachment);
                    /*Base64 base64Url = new Base64(true);
                    byte[] fileByteArray = base64url.decodeBase64(attachPart.getData());
                    FileOutputStream fileOutFile =
                            new FileOutputStream("directory_to_store_attachments" + filename);
                    fileOutFile.write(fileByteArray);
                    fileOutFile.close();*/
                }
            }
            List<MessagePartHeader>  headers = message.getPayload().getHeaders();
            for(MessagePartHeader messagePartHeader: headers){
                String name = messagePartHeader.getName();
                //Log.d(TAG,"nameINgetMessage : " + name);

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


}
