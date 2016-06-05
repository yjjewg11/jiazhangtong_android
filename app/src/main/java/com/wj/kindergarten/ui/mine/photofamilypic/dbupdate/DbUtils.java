package com.wj.kindergarten.ui.mine.photofamilypic.dbupdate;

import com.wj.kindergarten.bean.AllPfAlbumSunObject;
import com.wj.kindergarten.utils.TimeUtil;

import net.tsz.afinal.FinalDb;

import java.util.List;

/**
 * Created by tangt on 2016/3/17.
 */
public class DbUtils {
    public static List<AllPfAlbumSunObject> getAllPic(FinalDb db){
       return db.findAll(AllPfAlbumSunObject.class);
    }
}
