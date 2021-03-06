package fragments.android.com.colors;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;



public class DialerFragment extends Fragment {

    LinearLayout dialer;
    ImageView keypadHide;
    ImageButton keypadShow;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState){
        View v = inflater.inflate(R.layout.dialer_fragment, container, false);
        dialer = (LinearLayout)v.findViewById(R.id.fragment);
        keypadHide = (ImageView) v.findViewById(R.id.keypadHide);

         keypadShow = (ImageButton) v.findViewById(R.id.keypadShow);
        keypadShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View vi) {
                dialer.setVisibility(View.VISIBLE);
                keypadShow.setVisibility(View.GONE);
                keypadHide.setVisibility(View.VISIBLE);
            }
        });
        keypadHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialer.setVisibility(View.GONE);
                keypadShow.setVisibility(View.VISIBLE);
                keypadHide.setVisibility(View.GONE);
            }
        });
        return v;
    }
}
