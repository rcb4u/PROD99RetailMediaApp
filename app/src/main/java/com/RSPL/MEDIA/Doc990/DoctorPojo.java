package com.RSPL.MEDIA.Doc990;

import java.util.ArrayList;

/**
 * Created by rspl-richa on 23/11/17.
 */

public class DoctorPojo {
    String title;
    String name;
    String Id;
    ArrayList<Hospital> hospitals;
    String date_save;


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public ArrayList<Hospital> getHospitals() {
        return hospitals;
    }

    public void setHospitals(ArrayList<Hospital> hospitals) {
        this.hospitals = hospitals;
    }


    public DoctorPojo(String name, String title, ArrayList<Hospital> hospitalPojos, String Id) {
        this.name = name;
        this.title = title;
        this.hospitals = hospitalPojos;
        this.Id = Id;
        this.date_save = "Select Date";


    }

    public static class Hospital {
        String name, id;
        ArrayList<Specialization> specialization;

        public Hospital(String name, String id, ArrayList<Specialization> specialization) {
            this.name = name;
            this.id = id;
            this.specialization = specialization;
        }

        public Hospital(String name, String id) {
            this.name = name;
            this.id = id;
        }

        public ArrayList<Specialization> getSpecialization() {
            return specialization;
        }


        public void setSpecialization(ArrayList<Specialization> specialization) {
            this.specialization = specialization;
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

        public static class Specialization {
            String name;
            String id;

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


            public Specialization(String name, String id) {
                this.name = name;
                this.id = id;
            }
        }

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
