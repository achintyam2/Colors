package fragments.android.com.colors;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Achintya on 20-10-2016.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    ImageView blue, green, red, orange,peach,yellow,cyan,voilet;
    private static final String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circles);
        blue = (ImageView) findViewById(R.id.blue);
        green = (ImageView) findViewById(R.id.green);
        red = (ImageView) findViewById(R.id.red);
        orange = (ImageView) findViewById(R.id.orange);
        peach = (ImageView) findViewById(R.id.peach);
        yellow = (ImageView) findViewById(R.id.yellow);
        cyan = (ImageView) findViewById(R.id.cyan);
        voilet = (ImageView) findViewById(R.id.voilet);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        ImageView selectedView = (ImageView) findViewById(id);
        selectedView.setDrawingCacheEnabled(true);
        int selectedColor  = selectedView.getSolidColor();
        selectedView.setImageResource(R.drawable.ic_done_black_24dp);
        Log.d(TAG,"Blue: "+selectedColor);

        /*switch (id) {
            case R.id.blue:
                int blueContext =ContextCompat.getColor(getApplicationContext(),R.color.blue);
                blue.setImageResource(R.drawable.ic_done_black_24dp);
                Log.d(TAG,"Blue: "+blueContext);
                break;
            case R.id.green:
                int greenContext =ContextCompat.getColor(getApplicationContext(),R.color.green);
                Log.d(TAG,"Green: "+greenContext);
                break;
            case R.id.red:
                int redContext =ContextCompat.getColor(getApplicationContext(),R.color.red);
                Log.d(TAG,"Red: "+redContext);
                break;
            case R.id.orange:
                int orangeContext =ContextCompat.getColor(getApplicationContext(),R.color.orange);
                Log.d(TAG,"Orange: "+orangeContext);
                break;
            case R.id.peach:
                int peachContext =ContextCompat.getColor(getApplicationContext(),R.color.peach);
                Log.d(TAG,"Peach: "+peachContext);
                break;
            case R.id.yellow:
                int yellowContext =ContextCompat.getColor(getApplicationContext(),R.color.yellow);
                Log.d(TAG,"Yellow: "+yellowContext);
                break;
            case R.id.cyan:
                int cyanContext =ContextCompat.getColor(getApplicationContext(),R.color.cyan);
                Log.d(TAG,"Cyan: "+cyanContext);
                break;
            case R.id.voilet:
                int voiletContext =ContextCompat.getColor(getApplicationContext(),R.color.voilet);
                Log.d(TAG,"Voilet: "+voiletContext);
                break;
            case R.id.cancel:

                break;
            case R.id.accept:

                break;
        }*/
    }
}
