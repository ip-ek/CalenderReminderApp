package com.ipk.reminderapp;

public class UpcomeEvent {
    //model
    private String type, label, date, time;
    private int  eventID;

    public UpcomeEvent() {
    }

    public UpcomeEvent(int eventID, String type, String label, String date, String time) {
        this.eventID = eventID;
        this.type = type;
        this.label = label;
        this.date = date;
        this.time = time;
    }

    public int getEventID() {
        return eventID;
    }

    /*public void setEventID(int eventID) {
        this.eventID = eventID;
    }*/

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
