package com.example.photoalbum;

import java.io.Serializable;

public class Tag implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 5830906502631257774L;
    private String name, value;

    /**
     * This is a constructor for the class Tag
     * @param name Name of the tag of the photo
     * @param value Value of the tag of the photo
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public Tag(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * This is a get method that returns the name of the tag
     * @return name of the tag
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public String get_name() {
        return name;
    }
    /**
     * This is a get method that returns the value of the tag
     * @return value of the tag
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public String get_value() {
        return value;
    }
    /**
     * This is a method that changes the name of the tag
     * @param name The new name of the tag
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public void set_name (String name) {
        this.name = name;
    }
    /**
     * This is a method that changes the value of the tag
     * @param value The new value of the tag
     *
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */
    public void set_value (String value) {
        this.value = value;
    }

    /**
     * This is a method that prints the name and the value of the tag
     * @return the name and the value of the tag
     * @author Sujay Sayini
     * @author Pauleene Jordan
     */

    public String toString() {
        return name + ", " + value;
    }




}
