package com.wj.kindergarten.ui.mine.photofamilypic.dbupdate;

import android.database.sqlite.SQLiteDatabase;

import net.tsz.afinal.FinalDb;

/**
 * Created by tangt on 2016/3/16.
 */
public class UploadPathFive implements FinalDb.DbUpdateListener  {
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if(i != i1){
            String sql = "ALTER TABLE already_save_path ADD COLUMN photo_time char(45);";
            db.execSQL(sql);
        }
    }
}
