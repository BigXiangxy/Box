
package com.box.lib.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class AppActivityManager {

    private static Stack<Activity> activityStack;
    private static AppActivityManager instance;

    private AppActivityManager() {
    }

    /**
     * 单一实例
     */
    public static AppActivityManager getInstance() {
        if (instance == null) instance = new AppActivityManager();
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) activityStack = new Stack<Activity>();
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack)
            if (activity.getClass().equals(cls)) finishActivity(activity);
    }

    /**
     * 清除栈中所有的页面，只在退出时调用
     */
    public void finishAllActivity() {
        for (Activity a : activityStack)
            if (a != null && !a.isFinishing()) a.finish();
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            android.app.ActivityManager activityMgr = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断activity是否在堆栈中
     *
     * @param cls
     * @return
     */
    public static boolean isExsitMianActivity(Class<?> cls) {
        if (activityStack == null) return false;
        for (Activity activity : activityStack)
            if (activity.getClass().equals(cls)) return true;
        return false;
    }
}
