package com.yyz.ard.cactus.uiaf;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @className: ActivitySupervise
 * @classDescription:
 * @author: yyz
 * @createTime: 2019/3/6
 */
public class ActivitySupervise {

    private List<Activity> sActivityList;
    private static ActivitySupervise sSupervise = null;

    private ActivitySupervise() {
        sActivityList = new ArrayList<>();
    }

    public static ActivitySupervise getSupervise() {
        if (sSupervise == null) {
            synchronized (ActivitySupervise.class) {
                if (sSupervise == null) {
                    sSupervise = new ActivitySupervise();
                }
            }
        }
        return sSupervise;
    }

    public Activity getcurrentActivity() {
        if (sActivityList.size() > 0) {
            return sActivityList.get(sActivityList.size() - 1);
        }
        return null;
    }

    public void pushActivity(Activity activity) {
        synchronized (ActivitySupervise.class) {
            if (!sActivityList.contains(activity)) {
                sActivityList.add(activity);
            }
        }
    }

    public void popActivity(Activity target) {
        synchronized (ActivitySupervise.class) {
            if (!sActivityList.contains(target)) {
                sActivityList.remove(target);
            }
        }
    }

    public Activity popActivity(Class<? extends Activity> clx) {
        Activity target = null;
        synchronized (ActivitySupervise.class) {
            for (Activity activity : sActivityList) {
                if (activity.getClass().equals(clx)) {
                    target = activity;
                }
            }
            sActivityList.remove(target);
        }
        return target;
    }

    public void popAll() {
        synchronized (ActivitySupervise.class) {
            sActivityList.clear();
        }
    }

    public void finshAll() {
        synchronized (ActivitySupervise.class) {
            for (Activity activity : sActivityList) {
                activity.finish();
            }
            sActivityList.clear();
        }
    }
}
