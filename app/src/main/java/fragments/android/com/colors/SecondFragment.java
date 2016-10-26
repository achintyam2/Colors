package fragments.android.com.colors;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Achintya on 26-10-2016.
 */
public class SecondFragment extends Fragment {
    Button button;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_fragment, container, false);
        button = (Button)v.findViewById(R.id.button);

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
