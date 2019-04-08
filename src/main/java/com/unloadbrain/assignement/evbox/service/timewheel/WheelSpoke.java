package com.unloadbrain.assignement.evbox.service.timewheel;

public class WheelSpoke {

    private SessionCount data;
    private WheelSpoke next;

    public WheelSpoke(SessionCount data) {
        this.data = data;
    }

    public SessionCount getData() {
        return data;
    }

    public WheelSpoke getNext() {
        return next;
    }

    public void setNext(WheelSpoke next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}