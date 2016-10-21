package fragments.android.com.colors;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class ColorsDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView blue, green, red, orange,peach,yellow,cyan,violet;
    int blueContext,greenContext,redContext,orangeContext,yellowContext,peachContext,cyanContext,violetContext;
    ImageView previousSelection;
    int presentSelection=-1;
    private static final String TAG="MyDialogFragment";
    private Context context;
    private DialogFragmentClickHandler caller;
    Button cancel,accept;

    public ColorsDialogFragment(){}

    public interface DialogFragmentClickHandler {
        void onDialogFragmentClicked(int colorValue);
    }

    public static ColorsDialogFragment newInstance() {
        ColorsDialogFragment fragment = new ColorsDialogFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        caller = (DialogFragmentClickHandler) context;
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.circles, container, false);

        blue = (ImageView) rootView.findViewById(R.id.blue);
        green = (ImageView) rootView.findViewById(R.id.green);
        red = (ImageView) rootView.findViewById(R.id.red);
        orange = (ImageView) rootView.findViewById(R.id.orange);
        peach = (ImageView) rootView.findViewById(R.id.peach);
        yellow = (ImageView) rootView.findViewById(R.id.yellow);
        cyan = (ImageView) rootView.findViewById(R.id.cyan);
        violet = (ImageView) rootView.findViewById(R.id.violet);
        cancel = (Button) rootView.findViewById(R.id.cancel);
        accept = (Button) rootView.findViewById(R.id.accept);
        blueContext = ContextCompat.getColor(context, R.color.blue);
        greenContext = ContextCompat.getColor(context, R.color.green);
        redContext = ContextCompat.getColor(context, R.color.red);
        orangeContext = ContextCompat.getColor(context, R.color.orange);
        peachContext = ContextCompat.getColor(context, R.color.peach);
        yellowContext = ContextCompat.getColor(context, R.color.yellow);
        cyanContext = ContextCompat.getColor(context, R.color.cyan);
        violetContext = ContextCompat.getColor(context, R.color.violet);
        getDialog().setTitle("Theme Color");
        blue.setOnClickListener(this);
        green.setOnClickListener(this);
        red.setOnClickListener(this);
        peach.setOnClickListener(this);
        yellow.setOnClickListener(this);
        cyan.setOnClickListener(this);
        violet.setOnClickListener(this);
        orange.setOnClickListener(this);
        cancel.setOnClickListener(this);
        accept.setOnClickListener(this);


        return rootView;
    }
    @Override
    public void onClick (View v)
    {

        int a=v.getId();
        Log.d(TAG,"ID "+a);
        if(a>= 2131427414 && a<=2131427421 ) {
            if (previousSelection != null && v.getId() == previousSelection.getId()) {
                ((ImageView) v).setImageResource(R.drawable.ic_done_black_24dp);
            } else {
                ((ImageView) v).setImageResource(R.drawable.ic_done_black_24dp);
                presentSelection = v.getId();
                if (previousSelection != null && presentSelection != previousSelection.getId()) {
                    previousSelection.setImageResource(R.drawable.ic_done_blank_24dp);
                }
                previousSelection = (ImageView) v;
            }
        }
        else if(a==2131427422)
        {
            dismiss();
        }
        else
        {
             //ColorDrawable drawable = (ColorDrawable) previousSelection.getBackground();
             //presentSelection=drawable.getColor();
            MainActivity call = (MainActivity) getActivity();
            call.onDialogFragmentClicked(presentSelection);
            dismiss();
        }

    }

}
