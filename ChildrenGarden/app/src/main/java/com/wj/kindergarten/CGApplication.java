package com.wj.kindergarten;

import android.app.Application;

import com.wenjie.jiazhangtong.R;
import com.wj.kindergarten.bean.Login;
import com.wj.kindergarten.common.Constants;
import com.wj.kindergarten.net.RequestHttpUtil;
import com.wj.kindergarten.utils.ImageLoaderUtil;

import java.io.File;

/**
 * JJGApplication
 *
 * @author Wave
 * @data: 2015/5/20
 * @version: v1.0
 */
public class CGApplication extends Application {
    private static CGApplication context = null;
    private Login login = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        String cachePath = Constants.cachePath;
        ImageLoaderUtil.initImageLoader(this, R.drawable.main_item, cachePath, 10, 0);
        RequestHttpUtil.initClient();
        initDirs();
    }

    public static CGApplication getInstance() {
        return context;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
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
