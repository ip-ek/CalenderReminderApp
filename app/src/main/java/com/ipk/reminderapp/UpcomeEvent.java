package com.ipk.reminderapp;

import androidx.annotation.NonNull;

public class UpcomeEvent {
    //model
    private String label, content, startDate, startTime, endDate, endTime, remindTime, address, alarm;
    private int eventID, enventFreq, type, parent, counter;

    public UpcomeEvent() {
    }

    public UpcomeEvent(int eventID, int type, String label, String content, String startDate, String startTime, String endDate, String endTime, String remindTime, int enventFreq, String address, int parent, int counter, String alarm) {
        this.eventID = eventID;
        this.type = type;
        this.label = label;
        this.content = content;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.remindTime = remindTime;
        this.enventFreq = enventFreq;
        this.address = address;
        this.parent=parent;
        this.counter=counter;
        this.alarm=alarm;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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

    public int getEnventFreq() {
        return enventFreq;
    }

    public void setEnventFreq(int enventFreq) {
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

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getAlarm() {
        return alarm;
    }

    public void setAlarm(String alarm) {
        this.alarm = alarm;
    }

    @NonNull
    @Override
    public String toString() {
        //TODO: istersen etkinlik türünü de gönder
        String str= "\nEtkinlik Adı: "+label;
        if(!this.content.equals("")){
            str=str+"\nKonusu: "+content;
        }
        str=str+"\nBaşlangıç: "+this.startDate+" - " + this.startTime+"\nBitiş: "+ this.endDate+" - "+this.endTime;
        if(!this.address.equals("")){
            str=str+"\nAdres: "+this.address;
        }
        return str;
    }
}
