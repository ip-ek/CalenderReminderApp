package com.ipk.reminderapp;

import androidx.annotation.NonNull;

public class UpcomeEvent {
    //model
    private String type, label, content, startDate, startTime, endDate, endTime, remindTime, enventFreq, address;
    private int eventID;

    public UpcomeEvent() {
    }

    public UpcomeEvent(int eventID, String type, String label, String content, String startDate, String startTime, String endDate, String endTime, String remindTime, String enventFreq, String address) {
        this.type = type;
        this.label = label;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.remindTime = remindTime;
        this.enventFreq = enventFreq;
        this.address = address;
        this.eventID = eventID;
        this.content=content;
    }

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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getEnventFreq() {
        return enventFreq;
    }

    public void setEnventFreq(String enventFreq) {
        this.enventFreq = enventFreq;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        String str= type+"\nEtkinlik Adı: "+label;
        if(this.content!=""){
            str=str+"\nKonusu: "+content;
        }
        str=str+"\nBaşlangıç: "+this.startDate+" - " + this.startTime+"\nBitiş: "+ this.endDate+" - "+this.endTime;
        if(this.address!=""){
            str=str+"\nAdres: "+this.address;
        }
        return str;
    }
}
