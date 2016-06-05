package com.wj.kindergarten.ui.imagescan;

import com.wj.kindergarten.bean.ScanImageAndTime;

import java.util.ArrayList;

/**
 * PhotoDirModel
 *
 * @author weiwu.song
 * @data: 2015/1/6 10:10
 * @version: v1.0
 */
public class PhotoDirTimeModel {
    //目录名
    private String dirName;
    //目录下图片路径集合
    private ArrayList<ScanImageAndTime> paths;

    public String getDirName() {
        return dirName;
    }

    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public void addPath(ScanImageAndTime scanImageAndTime) {
        paths.add(scanImageAndTime);
    }

    public ArrayList<ScanImageAndTime> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<ScanImageAndTime> paths) {
        this.paths = paths;
    }
}
