package com.wj.kindergarten.ui.map;

import java.io.Serializable;

/**
 * Created by tangt on 2016/1/6.
 */
public class MapTransportObject implements Serializable {
    private String map_point;

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
                '}';
    }
}
