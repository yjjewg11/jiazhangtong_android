package com.wj.kindergarten;

import android.app.Activity;
import android.app.Application;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * ActivityManger
 *
 * @Description:xxx
 * @Author: pengqiang.zou
 * @CreateDate: 2015-08-14 15:14
 */
public class ActivityManger extends Application {
    private List<Activity> activities = null;
    private static ActivityManger instance = new ActivityManger();

    private ActivityManger() {
        this.activities = new LinkedList();
    }

    public static ActivityManger getInstance() {
        return instance;
    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public void exit() {
        if (this.activities != null && this.activities.size() > 0) {
            Iterator var1 = this.activities.iterator();

            while (var1.hasNext()) {
                Activity activity = (Activity) var1.next();
                activity.finish();
            }
        }

       // System.exit(0);
    }
}
