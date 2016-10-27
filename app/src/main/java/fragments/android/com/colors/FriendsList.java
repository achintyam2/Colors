package fragments.android.com.colors;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class FriendsList extends Activity {

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



