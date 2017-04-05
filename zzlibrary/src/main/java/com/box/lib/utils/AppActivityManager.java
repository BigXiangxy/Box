
package com.box.lib.utils;

import android.app.Activity;

import com.box.lib.inject.AppScope;

import java.util.Stack;

import javax.inject.Inject;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
@AppScope
public class AppActivityManager {

    private Stack<Activity> activityStack;

    @Inject
    public AppActivityManager() {
        activityStack = new Stack<>();
    }

    /**
     * 添加Activity到堆栈
     *
     * @param activity
     */
    public void add(Activity activity) {
        if (activityStack == null) ;
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     *
     * @return
     */
    public Activity getTop() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void removeTop() {
        remove(activityStack.lastElement());
    }

    /**
     * 结束指定的Activity
     *
     * @param activity
     */
    public void remove(Activity activity) {
        if (activity != null)
            activityStack.remove(activity);
    }

    /**
     * 结束指定类名的Activity
     *
     * @param cls
     */
    public void remove(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                remove(activity);
                return;
            }
        }
    }

    /**
     * 清除栈中所有的页面，只在退出时调用
     */
    public void removeAll() {
        for (Activity a : activityStack) {
            if (a != null && !a.isFinishing()) {
                a.finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 判断activity是否在堆栈中
     *
     * @param cls
     * @return 存在则返回实例否则返回null
     */
    public <T extends Activity> T haveActivity(Class<T> cls) {
        for (Activity activity : activityStack)
            if (activity.getClass().equals(cls)) return (T) activity;
        return null;
    }
}
