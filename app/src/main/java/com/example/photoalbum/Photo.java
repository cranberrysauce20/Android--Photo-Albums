package com.example.photoalbum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;

/**
 * Implementing the class Photo
 * @author Sujay Sayini
 * @author Pauleene Jordan
 */

public class Photo implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = -6704250030777951238L;
    private String caption;
    private byte[] image_bytes;
    private ArrayList<Tag> tags;

    /**
     * This is a Constructor for Photo that has accepts 3 kinds of parameter in the following oder: String, Image and Calendar.
     * @param image Image of the photo
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public Photo(String caption, Bitmap image) {
        this.caption = caption;

        ByteArrayOutputStream str = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, str);
        image_bytes = str.toByteArray();

        this.tags = new ArrayList<Tag>();
    }

    /**
     * This is a method that returns the caption of the photos
     * @return captiion of the photo
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public String get_caption() {
        return caption;
    }

    /**
     * This is a method that changes the caption of the photo
     * @param caption The new caption of the photo
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public void set_caption(String caption) {
        this.caption = caption;
    }
    /**
     * This is a method that returns the photo
     * @return image of the photo
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(image_bytes, 0, image_bytes.length);
    }

    /**
     * This is a method that returns the tag of the photo
     * @return tag of the photo
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public ArrayList<Tag> get_tags() {
        return tags;
    }





}
