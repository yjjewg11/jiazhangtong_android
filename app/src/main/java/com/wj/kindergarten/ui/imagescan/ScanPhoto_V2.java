package com.wj.kindergarten.ui.imagescan;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;


import com.wj.kindergarten.bean.ScanImageAndTime;
import com.wj.kindergarten.utils.CGLog;
import com.wj.kindergarten.utils.TimeUtil;
import com.wj.kindergarten.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * ScanPhoto_V2
 *
 * @author weiwu.song
 * @data: 2015/1/4 16:15
 * @version: v1.0
 */
public class ScanPhoto_V2 {
    private final static String[] imagesFormat = new String[]{"image/jpeg", "image/png"};
    private final static String queryWhereOr = " or ";
    private final static String queryWhereEq = " =? ";

    private ScanPhoto_V2() {
    }

    /**
     * 扫描媒体库
     */
    public static void scanMediaDir(Context context, ArrayList<String> scanList, HashMap<String, List<String>> dirMap) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        //只查询jpeg和png的图片
        StringBuffer queryWhere = new StringBuffer(MediaStore.Images.Media.MIME_TYPE);
        queryWhere.append(queryWhereEq);
        queryWhere.append(queryWhereOr);
        queryWhere.append(MediaStore.Images.Media.MIME_TYPE);
        queryWhere.append(queryWhereEq);
        Cursor mCursor = mContentResolver.query(mImageUri, null, queryWhere.toString(),
                imagesFormat, MediaStore.Images.Media.DATE_MODIFIED);
        while (mCursor != null && mCursor.moveToNext()) {
            //获取图片的路径
            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

            if (Utils.stringIsNull(path)) {
                continue;
            }
            scanList.add(path);
            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();

            //根据父路径名将图片放入到mGroupMap中
            if (!dirMap.containsKey(parentName) || dirMap.get(parentName) == null) {
                List<String> childList = new ArrayList<>();
                childList.add(path);
                dirMap.put(parentName, childList);
            } else {
                dirMap.get(parentName).add(path);
            }
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }

    public static void scanMediaTime(Context context, ArrayList<ScanImageAndTime> scanList, HashMap<String, List<ScanImageAndTime>> dirMap) {
        Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();
        //只查询jpeg和png的图片
        StringBuffer queryWhere = new StringBuffer(MediaStore.Images.Media.MIME_TYPE);
        queryWhere.append(queryWhereEq);
        queryWhere.append(queryWhereOr);
        queryWhere.append(MediaStore.Images.Media.MIME_TYPE);
        queryWhere.append(queryWhereEq);
        Cursor mCursor = mContentResolver.query(mImageUri, null, queryWhere.toString(),
                imagesFormat, MediaStore.Images.Media.DATE_MODIFIED);
        while (mCursor != null && mCursor.moveToNext()) {
            //获取图片的路径

            String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
            String takeTime = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            if (Utils.stringIsNull(path)) {
                continue;
            }
            ScanImageAndTime obj = new ScanImageAndTime();
            obj.setPath(path);
            if(takeTime == null || TextUtils.isEmpty(takeTime))takeTime = String.valueOf(TimeUtil.getYMDnowTime());
            obj.setTime(TimeUtil.getDateToString(Long.valueOf(takeTime)));
            scanList.add(obj);
            //获取该图片的父路径名
            String parentName = new File(path).getParentFile().getName();
            CGLog.v("takeTime ； "+ TimeUtil.getDateToString(Long.valueOf(takeTime))+"  modlieTime : ");
            //根据父路径名将图片放入到mGroupMap中
            if (!dirMap.containsKey(parentName) || dirMap.get(parentName) == null) {
                List<ScanImageAndTime> childList = new ArrayList<>();
                childList.add(obj);
                dirMap.put(parentName, childList);
            } else {
                dirMap.get(parentName).add(obj);
            }
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
    }
}
