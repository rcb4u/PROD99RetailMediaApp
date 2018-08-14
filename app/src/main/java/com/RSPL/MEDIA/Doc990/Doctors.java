package com.RSPL.MEDIA.Doc990;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Doctors extends ExpandableGroup<Specializations> {

    private String getId;
    private String Titleint;


    public Doctors(String title, List<Specializations> items, String iconResId) {
        super(title, items);
        this.getId = iconResId;
        this.Titleint = title;

    }

    public String getGetId() {
        return getId;
    }


    public String getTitleint() {
        return Titleint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctors)) return false;

        Doctors genre = (Doctors) o;

        return getGetId() == genre.getGetId();
    }

    @Override
    public int hashCode() {
        return Integer.parseInt(getGetId());
    }
}

