package com.wj.kindergarten.ui.map;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/6.
 */
public class MapTransportObject implements Serializable {
    private String map_point;
    private String path;
    private String name;
    private String address;

    public MapTransportObject(String map_point, String path, String name, String address) {
        this.map_point = map_point;
        this.path = path;
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MapTransportObject(String map_point, String path) {
        this.map_point = map_point;
        this.path = path;
    }

    public MapTransportObject() {
    }

    public MapTransportObject(String map_point) {
        this.map_point = map_point;
    }

    public String getMap_point() {
        return map_point;
    }

    public void setMap_point(String map_point) {
        this.map_point = map_point;
    }

    @Override
    public String toString() {
        return "MapTransportObject{" +
                "map_point='" + map_point + '\'' +
                ", path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
