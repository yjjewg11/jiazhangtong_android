package com.wj.kindergarten.ui.mine.photofamilypic.dbupdate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.wj.kindergarten.utils.CGLog;

import net.tsz.afinal.FinalDb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by tangt on 2016/3/15.
 */
public class UploadPathDbTwo implements FinalDb.DbUpdateListener {
    private Context context;

    public UploadPathDbTwo(Context context) {
        this.context = context;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if(i != i1){
        do{
            i++;
        if(i == 2){
            String sql = "ALTER TABLE already_save_path ADD COLUMN family_uuid char(45);";
            db.execSQL(sql);
        }
        if(i == 4){
            String sql = "ALTER TABLE already_save_path ADD COLUMN data_id char(45);";
            db.execSQL(sql);
        }
        if(i == 5){
            String sql = "ALTER TABLE already_save_path ADD COLUMN photo_time char(45);";
            db.execSQL(sql);
        }
        if(i == 6){
            String sql = "ALTER TABLE already_save_path ADD COLUMN progress Integer;";
            db.execSQL(sql);
            String sql2 = "ALTER TABLE already_save_path ADD COLUMN total Integer;";
            db.execSQL(sql2);
        }
        }while (i <= i1);

        }
    }
}
