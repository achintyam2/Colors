package com.android.sms;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class CustomBodyListAdapter extends CursorAdapter implements BodyOnClickDialogFragment.MessageOptionsDialogClickHandler{

    private Context con;
    private String bodyCopied,nameCopied;
    int msgId;
    Cursor cursorToPass;

     CustomBodyListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);

        cursorToPass = cursor;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.inbox_list_single_row, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        con = context;
        RelativeLayout smsSent = (RelativeLayout) view.findViewById(R.id.sms_Sent);
        TextView smsDateSent = (TextView) view.findViewById(R.id.smsDateSent);
        TextView smsBodySent = (TextView) view.findViewById(R.id.smsBodySent);
        TextView smsTimeSent = (TextView) view.findViewById(R.id.smsTimeSent);
        TextView smsStatusSent = (TextView) view.findViewById(R.id.smsStatusSent);

        RelativeLayout smsReceived = (RelativeLayout) view.findViewById(R.id.sms_Received);
        TextView smsDateReceived = (TextView) view.findViewById(R.id.smsDateReceived);
        TextView smsBodyReceived = (TextView) view.findViewById(R.id.smsBodyReceived);
        TextView smsTimeReceived = (TextView) view.findViewById(R.id.smsTimeReceived);
        TextView smsStatusReceived = (TextView) view.findViewById(R.id.smsStatusReceived);

        RelativeLayout mmsSent = (RelativeLayout) view.findViewById(R.id.mms_Sent);
        TextView mmsDateSent = (TextView) view.findViewById(R.id.mmsDateSent);
        TextView mmsTextSent = (TextView) view.findViewById(R.id.mmsTextSent);
        ImageView mmsPictureSent = (ImageView) view.findViewById(R.id.mmsPictureSent);
        TextView mmsTimeSent = (TextView) view.findViewById(R.id.mmsTimeSent);
        TextView mmsStatusSent = (TextView) view.findViewById(R.id.mmsStatusSent);

        RelativeLayout mmsReceived = (RelativeLayout) view.findViewById(R.id.mms_Received);
        TextView mmsDateReceived = (TextView) view.findViewById(R.id.mmsDateReceived);
        TextView mmsTextReceived = (TextView) view.findViewById(R.id.mmsTextReceived);
        ImageView mmsPictureReceived = (ImageView) view.findViewById(R.id.mmsPictureReceived);
        TextView mmsTimeReceived = (TextView) view.findViewById(R.id.mmsTimeReceived);
        TextView mmsStatusReceived = (TextView) view.findViewById(R.id.mmsStatusReceived);

        final String name  = cursor.getString(cursor.getColumnIndex("address"));
        int type = cursor.getInt(cursor.getColumnIndex("type"));
        final String msg_id = cursor.getString(cursor.getColumnIndex("_id"));
        String mms = cursor.getString(cursor.getColumnIndex("ct_t"));
        long seconds = cursor.getLong(cursor.getColumnIndex("date"));
        long date_sent = cursor.getLong(cursor.getColumnIndex("date_sent"));
        String dateReceived = getDate(seconds);
        String dateSent = getDate(date_sent);
        String timeReceived = getTime(seconds);
        String timeSent = getTime(date_sent);
        final String body = cursor.getString(cursor.getColumnIndex("body"));
        int msg_box = cursor.getInt(cursor.getColumnIndex("msg_box"));


        if (!"application/vnd.wap.multipart.related".equals(mms)) {
            if (type == 1) {
                smsReceived.setVisibility(View.VISIBLE);
                smsSent.setVisibility(View.GONE);
                mmsReceived.setVisibility(View.GONE);
                mmsSent.setVisibility(View.GONE);
                smsBodyReceived.setText(body);
                smsTimeReceived.setText(timeSent);
                smsTimeReceived.setTextColor(Color.YELLOW);
                smsStatusReceived.setText(context.getResources().getString(R.string.received));
                smsStatusReceived.setTextColor(Color.GREEN);
                smsDateReceived.setText(dateSent);
                smsReceived.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = Integer.parseInt(msg_id);
                        showMessageOptionsDialog(name,body,id);
                        return false;
                    }
                });
            } else if (type == 2) {
                smsSent.setVisibility(View.VISIBLE);
                smsReceived.setVisibility(View.GONE);
                mmsReceived.setVisibility(View.GONE);
                mmsSent.setVisibility(View.GONE);
                smsBodySent.setText(body);
                smsTimeSent.setText(timeReceived);
                smsTimeSent.setTextColor(Color.YELLOW);
                smsStatusSent.setTextColor(Color.GREEN);
                smsStatusSent.setText(context.getResources().getString(R.string.sent));
                smsDateSent.setText(dateReceived);
                smsSent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = Integer.parseInt(msg_id);
                        showMessageOptionsDialog(name,body,id);
                        return false;
                    }
                });
            } else if (type==5)
            {
                smsSent.setVisibility(View.VISIBLE);
                smsReceived.setVisibility(View.GONE);
                mmsReceived.setVisibility(View.GONE);
                mmsSent.setVisibility(View.GONE);
                smsBodySent.setText(body);
                smsTimeSent.setText(timeReceived);
                smsTimeSent.setTextColor(Color.YELLOW);
                smsStatusSent.setTextColor(Color.RED);
                smsStatusSent.setText(context.getResources().getString(R.string.failed));
                smsDateSent.setText(dateReceived);
                smsSent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = Integer.parseInt(msg_id);
                        showMessageOptionsDialog(name,body,id);
                        return false;
                    }
                });
            }
            else if (type==3)
            {
                smsSent.setVisibility(View.VISIBLE);
                smsReceived.setVisibility(View.GONE);
                mmsReceived.setVisibility(View.GONE);
                mmsSent.setVisibility(View.GONE);
                smsBodySent.setText(body);
                smsTimeSent.setText(timeReceived);
                smsTimeSent.setTextColor(Color.YELLOW);
                smsStatusSent.setTextColor(Color.BLUE);
                smsStatusSent.setText(context.getResources().getString(R.string.draft));
                smsDateSent.setText(dateReceived);
                smsSent.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int id = Integer.parseInt(msg_id);
                        showMessageOptionsDialog(name,body,id);
                        return false;
                    }
                });
            }
        } else {
            if (date_sent == 0) {
                //sent
                mmsSent.setVisibility(View.VISIBLE);
                mmsReceived.setVisibility(View.GONE);
                smsReceived.setVisibility(View.GONE);
                smsSent.setVisibility(View.GONE);
                if (msg_box == 4)
                {
                    mmsStatusSent.setText(context.getResources().getString(R.string.failed));
                    mmsStatusSent.setTextColor(Color.RED);
                }
                else if (msg_box == 2)
                {
                    mmsStatusSent.setText(context.getResources().getString(R.string.sent));
                    mmsStatusSent.setTextColor(Color.GREEN);
                }
                else
                {
                    mmsStatusSent.setText(context.getResources().getString(R.string.draft));
                    mmsStatusSent.setTextColor(Color.BLUE);
                }
                long second = seconds * 1000;
                String newDate = getDate(second);
                String newTime = getTime(second);
                readImage(msg_id, mmsPictureSent);
                mmsDateSent.setText(newDate);
                mmsTimeSent.setText(newTime);
                mmsTimeSent.setTextColor(Color.YELLOW);
                readText(msg_id, mmsTextSent);
            } else {
                //received
                mmsSent.setVisibility(View.GONE);
                mmsReceived.setVisibility(View.VISIBLE);
                smsReceived.setVisibility(View.GONE);
                smsSent.setVisibility(View.GONE);
                mmsStatusReceived.setText(context.getResources().getString(R.string.received));
                readImage(msg_id, mmsPictureReceived);
                mmsDateReceived.setText(dateSent);
                mmsTimeReceived.setText(timeSent);
                readText(msg_id, mmsTextReceived);
            }
        }
    }

    private void readImage(final String id, final ImageView pic) {

        Log.d("aa", "msg id " + id);
        String selectionPart = "mid=" + id;
        String[] project = {"*"};
        final Uri uriM = Uri.parse("content://mms/part");
        Cursor cPart = con.getContentResolver().query(uriM, project, selectionPart, null, null);
        cPart.moveToFirst();
        do {
            String partId = cPart.getString(cPart.getColumnIndex("_id"));
            final String type = cPart.getString(cPart.getColumnIndex("ct"));
            if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                    "image/gif".equals(type) || "image/jpg".equals(type) ||
                    "image/png".equals(type)) {
                final Bitmap bitmap = getMmsImage(partId);
                pic.setImageBitmap(bitmap);
                pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*String fileFolder = "/Pictures/Screenshots";
                        String fileName = "/Screenshot_20161020-113320";
                        File filePath = new File(Environment.getExternalStorageDirectory()+fileFolder+fileName+".png");
                        Uri fileUri = Uri.fromFile(filePath);
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(fileUri,type);
                        con.startActivity(intent);*/
                    }
                });
                Log.d("aa", "bitmap " + bitmap);
            }
        } while (cPart.moveToNext());


    }


    private void readText(String id, TextView text) {
        String selectionPart = "mid=" + id;
        Uri uri = Uri.parse("content://mms/part");
        String[] project = {"*"};
        Cursor cursor = con.getContentResolver().query(uri, project, selectionPart, null, null);
        cursor.moveToFirst();
        do {
            String partId = cursor.getString(cursor.getColumnIndex("_id"));
            String type = cursor.getString(cursor.getColumnIndex("ct"));
            if ("text/plain".equals(type)) {
                String data = cursor.getString(cursor.getColumnIndex("_data"));
                String body;
                if (data != null) {
                    // implementation of this method below
                    body = getMmsText(partId);
                    text.setText(body);
                } else {
                    body = cursor.getString(cursor.getColumnIndex("text"));
                    text.setText(body);
                }
            }
        } while (cursor.moveToNext());

    }

    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = con.getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return sb.toString();
    }

    private String getDate(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("MMMM d,yyyy");
        return formatter.format(date);
    }

    private String getTime(long timeStamp) {
        Date date = new Date(timeStamp);
        DateFormat formatter = new SimpleDateFormat("h:mm a");
        return formatter.format(date);
    }


    private Bitmap getMmsImage(String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = con.getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return bitmap;
    }

    @Override
    public void onForwardClcked() {
        Toast.makeText(con,"Forward Data : "+bodyCopied,Toast.LENGTH_SHORT).show();
        forwardMessage(bodyCopied);
    }

    @Override
    public void onCopyClicked() {
        Toast.makeText(con,"Copied Data",Toast.LENGTH_SHORT).show();
        copytoClipboard(bodyCopied);
    }

    @Override
    public void onDeleteClicked() {
//        Toast.makeText(con,"To delete : "+bodyCopied,Toast.LENGTH_SHORT).show();
        deleteMessage(con,msgId,cursorToPass);
    }

    private void showMessageOptionsDialog(String name,String body,int sms_id)
    {
        msgId = sms_id;
        nameCopied = name;
        bodyCopied = body;
        FragmentTransaction fragmentTransaction = ((FragmentActivity)con).getSupportFragmentManager().beginTransaction();
        BodyOnClickDialogFragment bodyOnClickDialogFragment = new BodyOnClickDialogFragment().newInstance();
        bodyOnClickDialogFragment.setListener(CustomBodyListAdapter.this);
        bodyOnClickDialogFragment.show(fragmentTransaction,"messageoptions");
    }

    private void deleteMessage(Context context,int SmsId,Cursor cursor) {
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            if (!Telephony.Sms.getDefaultSmsPackage(con).equals("com.android.sms")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this.con);
                builder.setMessage("This app is not set as your default messaging app. Do you want to set it as default?")
                        .setCancelable(false)
                        .setTitle("Alert!")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @TargetApi(19)
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, con.getPackageName());
                                con.startActivity(intent);
                            }
                        });
                builder.show();
            }
        }*/
        Cursor c = context.getContentResolver().query(Uri.parse("content://sms/"),new String[] {"_id", "thread_id", "address", "body" }, null, null, null);
        cursor.moveToFirst();
        do {
            try {
                String pid = cursor.getString(cursor.getColumnIndex("_id")); // Get id;
                String threadID = cursor.getString(cursor.getColumnIndex("thread_id"));
                String smsMessage = cursor.getString(3);
                String msgId = Integer.toString(SmsId);
                Log.d("aa","SMSID "+SmsId);
                Log.d("aa","pid "+pid);
                if (pid.equals(msgId))
                {
                    Log.d("aa","message "+smsMessage);
                    String uri = "content://sms/"+pid;
                    Uri mUri = Uri.parse("content://sms/" + pid);
//                    int rows = context.getContentResolver().delete(Uri.parse("content://sms"),"_id=?",new String[]{pid});
                    int rows = context.getContentResolver().delete(Uri.parse(uri),null,null/* Telephony.Sms._ID + "!=?", new String[]{pid}*/);
                    Toast.makeText(context, rows+" Message Deleted", Toast.LENGTH_LONG).show();
                    notifyDataSetChanged();
                    break;
                }
            } catch (Exception e) {
                Log.d("exception",  "occurred"+e.getMessage());
            }
        }while (cursor.moveToNext());
    }

    private void forwardMessage(String body)
    {
        //TODO set the body to the EditText
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setType("vnd.android-dir/mms-sms");
//        smsIntent.putExtra("address", "12125551212");
        smsIntent.addCategory(Intent.CATEGORY_DEFAULT);
        smsIntent.putExtra("sms_body",body);
        con.startActivity(smsIntent);
    }
    private void copytoClipboard(String value) {
        ClipboardManager clipboard = (ClipboardManager) con.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied address", value);
        clipboard.setPrimaryClip(clip);
    }
}

