package com.wj.kindergarten.bean;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/12.
 */
public class MapRouteData implements Serializable {
    private String address,time,distance;

    public MapRouteData() {
    }

    public MapRouteData(String address, String time, String distance) {
        this.address = address;
        this.time = time;
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "MapRouteData{" +
                "address='" + address + '\'' +
                ", time='" + time + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}
