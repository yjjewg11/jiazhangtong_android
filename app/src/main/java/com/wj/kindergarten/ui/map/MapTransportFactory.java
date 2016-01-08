package com.wj.kindergarten.ui.map;

import android.graphics.Bitmap;

/**
 * Created by tangt on 2016/1/8.
 */
public abstract class MapTransportFactory {
    public static MapTransportObject createMapTransport(Object... objects){
        String mapPotin = (String) objects[0];
        String path = (String) objects[1];
        return new MapTransportObject(mapPotin,path);
    }
}
