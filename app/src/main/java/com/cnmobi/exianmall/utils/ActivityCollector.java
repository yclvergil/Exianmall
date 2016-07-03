package com.cnmobi.exianmall.utils;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.util.LogUtils;

/**
 * Created by peng24 on 2015/12/24.
 */
public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
//        LogUtils.i("添加了："+activity.getClass().getSimpleName());
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
    
    public static void logAllActivityName() {
//    	LogUtils.i("执行了？"+activities.size());
        for (Activity activity : activities) {
        	 Log.i("activityName:",activity.getClass().getSimpleName());
        }
    }
    
    
}
