package com.RSPL.MEDIA.Doc990;

import android.os.Parcel;
import android.os.Parcelable;

public class Specializations implements Parcelable {

    private String name;
    private String isFavorite;
    private String hospital;

    public String getDrname() {
        return drname;
    }

    public void setDrname(String drname) {
        this.drname = drname;
    }

    String drname;

    public String getDrID() {
        return drID;
    }

    public void setDrID(String drID) {
        this.drID = drID;
    }

    String drID;

    public void setName(String name) {
        this.name = name;
    }

    public Specializations(String hospital, String name, String isFavorite, String hospitalId, String specializationId, String drnames, String drID) {
        this.name = name;
        this.isFavorite = isFavorite;
        this.hospital = hospital;
        this.specializationId = specializationId;
        this.hospitalId = hospitalId;
        this.drname = drnames;
        this.drID = drID;
    }

    private String hospitalId;

    public String getHospitalId() {
        return hospitalId;
    }


    public String getSpecializationId() {
        return specializationId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public void setSpecializationId(String specializationId) {
        this.specializationId = specializationId;
    }

    private String specializationId;


    public String getHospital() {
        return hospital;
    }


    protected Specializations(Parcel in) {
        name = in.readString();
        hospital = in.readString();
    }

    public String getName() {
        return name;
    }

    public String isFavorite() {
        return isFavorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Specializations)) return false;

        Specializations artist = (Specializations) o;

        if (isFavorite() != artist.isFavorite()) return false;
        return getName() != null ? getName().equals(artist.getName()) : artist.getName() == null;

    }

/*  @Override
  public int hashCode() {
    int result = getName() != null ? getName().hashCode() : 0;
    result = 31 * result + (isFavorite() ? 1 : 0);
    return result;
  }*/

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Specializations> CREATOR = new Creator<Specializations>() {
        @Override
        public Specializations createFromParcel(Parcel in) {
            return new Specializations(in);
        }

        @Override
        public Specializations[] newArray(int size) {
            return new Specializations[size];
        }
    };
}

