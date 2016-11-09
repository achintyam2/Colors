package fragments.android.com.colors;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


public class DialerFragment extends Fragment {

    LinearLayout dialer;
    ImageView image;


    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        View v = inflater.inflate(R.layout.dialer_fragment, container, false);
        dialer = (LinearLayout)v.findViewById(R.id.fragment);
        image = (ImageView) v.findViewById(R.id.keypadHide);

        final FloatingActionButton myFab = (FloatingActionButton) v.findViewById(R.id.keypadShow);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vi) {
                dialer.setVisibility(View.VISIBLE);
                myFab.setVisibility(View.GONE);
                image.setVisibility(View.VISIBLE);
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialer.setVisibility(View.GONE);
                myFab.setVisibility(View.VISIBLE);
                image.setVisibility(View.GONE);
            }
        });

        return v;
    }
}
