package com.example.photoalbum;

import java.util.ArrayList;
import java.io.Serializable;

/**
 * Implementing the class Album
 * @author Sujay Sayini
 * @author Pauleene Jordan
 */

public class Album implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3425239674061597130L;
    private String name;
    private ArrayList<Photo> album_photos;

    /**
     * Constructor for Album
     * @param name the name of the album
     * @param album_photos array list of all the photos in the album
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public Album(String name) {
        this.name = name;
        this.album_photos = new ArrayList<Photo>();
    }

    /**
     * This is a method that prints the name of the album
     * @return name of the album
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */

    public String toString() {
        return this.name;
    }


    /**
     * This is a method that changes the name of the user
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This is a method that returns the photos from the album
     * @return photos of the album
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public ArrayList<Photo> get_photos() {
        return this.album_photos;
    }



}
