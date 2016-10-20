package fragments.android.com.colors;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Achintya on 20-10-2016.
 */
public class ColorsDialogFragment extends DialogFragment implements View.OnClickListener  {

    ImageView blue, green, red, orange,peach,yellow,cyan,violet;
    private static final String TAG="MyDialogFragment";
    private Context context;

    public static ColorsDialogFragment newInstance() {
        ColorsDialogFragment frag = new ColorsDialogFragment();
        return frag;
    }

    @Override
    public void onAttach(Context context){
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
        getDialog().setTitle("Theme Color");
        return rootView;
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        /*ImageView selectedView = (ImageView) findViewById(id);
        selectedView.setDrawingCacheEnabled(true);
        int selectedColor  = selectedView.getSolidColor();
        selectedView.setImageResource(R.drawable.ic_done_black_24dp);
        Log.d(TAG,"Blue: "+selectedColor);*/

        switch (id) {
            case R.id.blue:
                int blueContext = ContextCompat.getColor(context,R.color.blue);
                blue.setImageResource(R.drawable.ic_done_black_24dp);
                Log.d(TAG,"Blue: "+blueContext);
                break;
            case R.id.green:
                int greenContext =ContextCompat.getColor(context,R.color.green);
                Log.d(TAG,"Green: "+greenContext);
                break;
            case R.id.red:
                int redContext =ContextCompat.getColor(context,R.color.red);
                Log.d(TAG,"Red: "+redContext);
                break;
            case R.id.orange:
                int orangeContext =ContextCompat.getColor(context,R.color.orange);
                Log.d(TAG,"Orange: "+orangeContext);
                break;
            case R.id.peach:
                int peachContext =ContextCompat.getColor(context,R.color.peach);
                Log.d(TAG,"Peach: "+peachContext);
                break;
            case R.id.yellow:
                int yellowContext =ContextCompat.getColor(context,R.color.yellow);
                Log.d(TAG,"Yellow: "+yellowContext);
                break;
            case R.id.cyan:
                int cyanContext =ContextCompat.getColor(context,R.color.cyan);
                Log.d(TAG,"Cyan: "+cyanContext);
                break;
            case R.id.violet:
                int violetContext =ContextCompat.getColor(context,R.color.violet);
                Log.d(TAG,"Violet: "+violetContext);
                break;
            case R.id.cancel:

                break;
            case R.id.accept:

                break;
        }
    }

}
