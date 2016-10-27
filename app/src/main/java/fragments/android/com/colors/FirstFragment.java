package fragments.android.com.colors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.FacebookSdk;


/**
 * Created by Achintya on 26-10-2016.
 */
public class FirstFragment extends Fragment {
    TextView text;
    Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_fragment, container, false);
        text = (TextView)v.findViewById(R.id.text);
        button = (Button)v.findViewById(R.id.button) ;

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ColorsDialogFragment newFragment = ColorsDialogFragment.newInstance();
                newFragment.show(ft, "title");
            }
        });
        return v;
    }

}
