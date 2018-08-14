package com.RSPL.MEDIA.Doc990;

/**
 * Created by rspl-richa on 29/11/17.
 */

public class SessionPojo {
    String id;
    String sessionAt;
    String activePatients;
    String status;
    String canBook;
    String publicNote;
    String nextPatient;
    String maxPatientCount;
    String totalLocalCharge;
    String totalOverseasCharge;

    public SessionPojo(String canbook, String status, String time, String nextpatient, String id) {
        this.status = status;
        this.sessionAt = time;
        this.nextPatient = nextpatient;
        this.id = id;
        this.canBook = canbook;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessionAt() {
        return sessionAt;
    }

    public void setSessionAt(String sessionAt) {
        this.sessionAt = sessionAt;
    }

    public String getActivePatients() {
        return activePatients;
    }

    public void setActivePatients(String activePatients) {
        this.activePatients = activePatients;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getcanBook() {
        return canBook;
    }

    public void setcanBook(String scanBook) {
        canBook = scanBook;
    }

    public String getPublicNote() {
        return publicNote;
    }

    public void setPublicNote(String publicNote) {
        this.publicNote = publicNote;
    }

    public String getNextPatient() {
        return nextPatient;
    }

    public void setNextPatient(String nextPatient) {
        this.nextPatient = nextPatient;
    }

    public String getMaxPatientCount() {
        return maxPatientCount;
    }

    public void setMaxPatientCount(String maxPatientCount) {
        this.maxPatientCount = maxPatientCount;
    }

    public String getTotalLocalCharge() {
        return totalLocalCharge;
    }

    public void setTotalLocalCharge(String totalLocalCharge) {
        this.totalLocalCharge = totalLocalCharge;
    }

    public String getTotalOverseasCharge() {
        return totalOverseasCharge;
    }

    public void setTotalOverseasCharge(String totalOverseasCharge) {
        this.totalOverseasCharge = totalOverseasCharge;
    }

    public SessionPojo(String id, String sessionAt, String activePatients, String status, String canBook, String publicNote, String nextPatient, String maxPatientCount, String totalLocalCharge, String totalOverseasCharge) {
        this.id = id;
        this.sessionAt = sessionAt;
        this.activePatients = activePatients;
        this.status = status;
        this.canBook = canBook;
        this.publicNote = publicNote;
        this.nextPatient = nextPatient;
        this.maxPatientCount = maxPatientCount;
        this.totalLocalCharge = totalLocalCharge;
        this.totalOverseasCharge = totalOverseasCharge;


    }
}
