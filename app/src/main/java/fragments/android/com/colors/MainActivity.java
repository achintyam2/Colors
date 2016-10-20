package fragments.android.com.colors;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Achintya on 20-10-2016.
 */
public class MainActivity extends Activity implements View.OnClickListener {

    ImageView blue, green, red, orange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circles);
        blue = (ImageView) findViewById(R.id.blue);
        green = (ImageView) findViewById(R.id.green);
        red = (ImageView) findViewById(R.id.red);
        orange = (ImageView) findViewById(R.id.orange);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.blue:

            break;
            case R.id.green:

            break;
            case R.id.red:

            break;
            case R.id.orange:

            break;
        }
    }
}
