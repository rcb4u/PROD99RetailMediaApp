package com.RSPL.MEDIA.Doc990;

/**
 * Created by rspl-richa on 20/12/17.
 */

public class Titlepojo {
    String id;
    String name;

    public Titlepojo(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
