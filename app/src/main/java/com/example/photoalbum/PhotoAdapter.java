package com.example.photoalbum;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter to hold the photo view and its details.
 */
public class PhotoAdapter extends ArrayAdapter<Photo> {

    private Context context;

    /**
     * initializes the photoAdapter
     * @param context
     * @param resourceId
     * @param items
     */
    public PhotoAdapter(Context context, int resourceId, List<Photo> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /**
     * Returns the view as requested.
     * @param position
     * @param convertView
     * @param parent
     * @return view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Photo photo = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.photo, null);
            holder = new ViewHolder();
            holder.caption = (TextView) convertView.findViewById(R.id.caption);
            holder.photo = (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.caption.setText(photo.get_caption());
        holder.photo.setImageBitmap(photo.getBitmap());

        return convertView;
    }

    private class ViewHolder {
        ImageView photo;
        TextView caption;
    }
}