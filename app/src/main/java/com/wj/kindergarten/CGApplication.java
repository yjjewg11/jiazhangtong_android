package com.wj.kindergarten;

import android.app.Application;
import android.graphics.Bitmap;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.ChildInfo;
import com.wj.kindergarten.bean.Group;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * JJGApplication
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public class CGApplication extends Application {
    public static CGApplication context = null;
    private Login login = null;
    private Map<String, Group> groupMap = new HashMap<>();
    private Map<String, ChildInfo> childInfoMap = new HashMap<>();
    public static DisplayImageOptions options = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        String cachePath = Constants.cachePath;
        ImageLoaderUtil.initImageLoader(this, R.drawable.touxiang, cachePath, 10, 0);
        RequestHttpUtil.initClient();
//        initDirs();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.friends_sends_pictures_no)
                .showImageForEmptyUri(R.drawable.friends_sends_pictures_no)
                .showImageOnFail(R.drawable.friends_sends_pictures_no)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .displayer(new RoundedBitmapDisplayer(0)).build();
    }

    public static CGApplication getInstance() {
        return context;
    }

    public Login getLogin() {
        return login;
    }

    public Map<String, Group> getGroupMap() {
        return groupMap;
    }

    public Map<String, ChildInfo> getChildInfoMap() {
        return childInfoMap;
    }

    public void setLogin(Login login) {
        this.login = login;

        groupMap.clear();
        childInfoMap.clear();
        if (login != null) {
            for (Group group : login.getGroup_list()) {
                if (group != null) {
                    groupMap.put(group.getUuid(), group);
                }
            }

            for (ChildInfo childInfo : login.getList()) {
                if (childInfo != null) {
                    childInfoMap.put(childInfo.getClassuuid(), childInfo);
                }
            }
        }
    }

    private void initDirs() {
        File parentDir = new File(Constants.parentPath);
        if (!parentDir.exists()) {
            parentDir.mkdir();
        }

        File cacheDir = new File(Constants.cachePath);
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
    }
}
