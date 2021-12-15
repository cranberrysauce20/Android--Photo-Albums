package com.example.photoalbum;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class saveAndWriteData {

    /**
     * This is a method that reads the data for the Login Controller class
     *
     * @return returns the user
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */

    @SuppressWarnings("unchecked")
    public static ArrayList<Album> readApp(String path) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        ArrayList<Album> my_albums = (ArrayList<Album>) ois.readObject();
        ois.close();

        return my_albums;
    }

    /**
     * This is a method that saves the data for the Login Controller class
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public static void writeApp(ArrayList<Album> my_albums, String path) throws IOException {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(my_albums);
            oos.close();
        } catch (Exception e) {

        }
    }
}