package fragments.android.com.colors;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;


public class ThemeSelection extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    LinearLayout listLayout;
    ListView list_images;
    RelativeLayout full_screen_layout;
    int count =0;
    int[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_selection);

        Intent intent = getIntent();

        toolbar = (Toolbar) findViewById(R.id.my_toolbar_black);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        full_screen_layout = (RelativeLayout) findViewById(R.id.full_screen_layout);
        list_images = (ListView) findViewById(R.id.list_images);

//        images = new int[]{R.drawable.one, R.drawable.two, R.drawable.three,R.drawable.four};

        Bitmap[] images = {getDRawable(R.drawable.one),getDRawable(R.drawable.two),
                getDRawable(R.drawable.three),getDRawable(R.drawable.four),getDRawable(R.drawable.last)};

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        final ImagesListCustomAdapter imagesListCustomAdapter = new ImagesListCustomAdapter(this,images);
        list_images.setAdapter(imagesListCustomAdapter);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        full_screen_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count == 0) {
                    toolbar.setVisibility(View.GONE);
                    listLayout.setVisibility(View.GONE);
                    count =1;
                }
                else if (count == 1) {
                    toolbar.setVisibility(View.VISIBLE);
                    listLayout.setVisibility(View.VISIBLE);
                    count = 0;
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {                   //Creating Menu Options
        getMenuInflater().inflate(R.menu.menu_theme_selection, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_images:
                //TODO delete action
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public Bitmap getDRawable(int fileName)
    {
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext().getResources(), fileName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        Bitmap resized = ThumbnailUtils.extractThumbnail(icon, 80, 120);
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return resized;
    }
}
