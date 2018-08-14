package com.RSPL.MEDIA.Doc990;

/**
 * Created by rspl-richa on 24/11/17.
 */

public class Hospitalpojo {
    String name;
    String id;


    public Hospitalpojo(String name, String id) {
        this.name = name;
        this.id = id;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }


}
