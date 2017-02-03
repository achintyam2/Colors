package fragments.android.com.colors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class ImagesListCustomAdapter extends BaseAdapter {



    private static LayoutInflater inflater = null;
    Context context;

    Bitmap[] images;

    public ImagesListCustomAdapter(ThemeSelection themeSelection,Bitmap[] image)
    {
        context = themeSelection;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        images = image;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    private class Holder
    {
        ImageView backgroundImage,selected;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        View view = inflater.inflate(R.layout.image_list_single_row, null);

        holder.backgroundImage = (ImageView) view.findViewById(R.id.background_image);
        holder.selected = (ImageView) view.findViewById(R.id.green_tick);

        holder.backgroundImage.setImageBitmap(images[position]);
        if (position==0)
        holder.selected.setVisibility(View.VISIBLE);
        else
        holder.selected.setVisibility(View.GONE);



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            int pos = position;
                if (position == images.length-1) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                }

            }
        });

        return view;
    }

}
