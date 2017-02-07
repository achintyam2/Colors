package fragments.android.com.colors;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class ThemeSelection extends AppCompatActivity {

    Toolbar toolbar;
    ActionBar actionBar;
    LinearLayout listLayout;
    ListView list_images;
    RelativeLayout full_screen_layout;
    int count =0;
    ArrayList<Bitmap> listImages;
    ImagesListCustomAdapter imagesListCustomAdapter;
    int height,width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.theme_selection);

        Intent intent = getIntent();


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        height = displaymetrics.heightPixels;
        width = displaymetrics.widthPixels;

        toolbar = (Toolbar) findViewById(R.id.my_toolbar_black);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        full_screen_layout = (RelativeLayout) findViewById(R.id.full_screen_layout);
        list_images = (ListView) findViewById(R.id.list_images);

//       int[] images = new int[]{R.drawable.one, R.drawable.two, R.drawable.three,R.drawable.four};

        listImages  = new ArrayList<>();
        listImages.add(getDRawable(R.drawable.one));
        listImages.add(getDRawable(R.drawable.two));
        listImages.add(getDRawable(R.drawable.three));
        listImages.add(getDRawable(R.drawable.four));
        listImages.add(getDRawable(R.drawable.last));

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        imagesListCustomAdapter = new ImagesListCustomAdapter(this,listImages);
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

        list_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == listImages.size()-1) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    imagesListCustomAdapter.notifyDataSetChanged();
                }

                if (position != listImages.size()-1) {
                    Drawable d = new BitmapDrawable(getResources(), listImages.get(position));
                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN)
                        full_screen_layout.setBackgroundDrawable(d);
                    else
                        full_screen_layout.setBackground(d);
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
        Bitmap resized = ThumbnailUtils.extractThumbnail(icon, (25*width)/100, (25*height)/100);
        resized.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return resized;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 1)
            {
                Bitmap bitmapPhoto = null;
                Uri selectedImageUri = data.getData();
                try {
                    bitmapPhoto = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                listImages.add(listImages.size()-1,bitmapPhoto);
                imagesListCustomAdapter.notifyDataSetChanged();
            }
        }
    }

}
