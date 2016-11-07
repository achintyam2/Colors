package contacts.android.themeselector;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class ColorsDialogFragment extends DialogFragment implements View.OnClickListener {
    ImageView blue, green, red, orange,peach,yellow,cyan,violet,previousSelection;
    int setColor, switchVariable;
    int presentSelection=-1;
    private static final String TAG="ColorsDialogFragment";
    private Context context;
    private DialogFragmentClickHandler caller;
    Button cancel,accept;
    SparseArray<Integer> colors = new SparseArray<>();
    Boolean flag = true;
    DialogFragmentClickHandler call;

    public ColorsDialogFragment(){}

    public interface DialogFragmentClickHandler {         //Creating a interface to communicate between the fragments
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
        getDialog().setTitle("Theme Color");

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colors.put(blue.getId(), getResources().getColor(R.color.blue, context.getTheme()));
            colors.put(green.getId(), getResources().getColor(R.color.green, context.getTheme()));
            colors.put(red.getId(), getResources().getColor(R.color.red, context.getTheme()));
            colors.put(orange.getId(), getResources().getColor(R.color.orange, context.getTheme()));
            colors.put(peach.getId(), getResources().getColor(R.color.peach, context.getTheme()));
            colors.put(yellow.getId(), getResources().getColor(R.color.yellow, context.getTheme()));
            colors.put(cyan.getId(), getResources().getColor(R.color.cyan, context.getTheme()));
            colors.put(violet.getId(), getResources().getColor(R.color.violet, context.getTheme()));
        }
            else {
            colors.put(blue.getId(), getResources().getColor(R.color.blue));
            colors.put(green.getId(), getResources().getColor(R.color.green));
            colors.put(red.getId(), getResources().getColor(R.color.red));
            colors.put(orange.getId(), getResources().getColor(R.color.orange));
            colors.put(peach.getId(), getResources().getColor(R.color.peach));
            colors.put(yellow.getId(), getResources().getColor(R.color.yellow));
            colors.put(cyan.getId(), getResources().getColor(R.color.cyan));
            colors.put(violet.getId(), getResources().getColor(R.color.violet));
        }

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
    public void onClick (View v) {
        int id = v.getId();
        Log.d(TAG, "ID " + id);
        if (id >= R.id.blue && id <= R.id.violet)
            switchVariable = 0;
        else if (id == R.id.cancel)
            switchVariable = 1;
        else
            switchVariable = 2;
        switch (switchVariable) {
            case 0:
                if (previousSelection != null && v.getId() == previousSelection.getId()) {
                    ((ImageView) v).setImageResource(R.drawable.ic_done_black_24dp);
                } else {
                    ((ImageView) v).setImageResource(R.drawable.ic_done_black_24dp);
                    presentSelection = v.getId();
                    if (previousSelection != null && presentSelection != previousSelection.getId()) {
                        previousSelection.setImageResource(R.drawable.ic_done_blank_24dp);
                    }
                    previousSelection = (ImageView) v;
                    setColor = colors.get(id);
                    Log.d(TAG, "SetColor " + setColor);
                }
                break;
            case 1:
                dismiss();
                break;
            case 2:
                Log.d(TAG, "SetColor " + setColor);
                 call = (DialogFragmentClickHandler) getActivity();
                if(setColor!=0){
                call.onDialogFragmentClicked(setColor);
                dismiss();
                }
                else
                dismiss();
                break;
        }
    }

}
