package fragments.android.com.colors;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.ArrayList;


class ImagesListCustomAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;
    private Context context;

    private ArrayList<Bitmap> list;


    ImagesListCustomAdapter(ThemeSelection themeSelection, ArrayList<Bitmap> imagesList)
    {
        context = themeSelection;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        list = imagesList;
    }

    @Override
    public int getCount() {return list.size();}

    @Override
    public Object getItem(int position) {return list.get(position);}

    @Override
    public long getItemId(int position) {return position;}

    private class Holder
    {
        ImageView backgroundImage,selected;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View view = inflater.inflate(R.layout.image_list_single_row, null);

        holder.backgroundImage = (ImageView) view.findViewById(R.id.background_image);
        holder.selected = (ImageView) view.findViewById(R.id.green_tick);

        holder.backgroundImage.setImageBitmap(list.get(position));

        if (position==0)
        holder.selected.setVisibility(View.VISIBLE);
        else
        holder.selected.setVisibility(View.GONE);

        return view;
    }


}
