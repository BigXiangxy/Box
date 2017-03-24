package com.box.lib.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

public class ToastUtil {
    private static ToastUtil mToast;

    public static void init(Context context) {
        if (mToast == null)
            mToast = new ToastUtil(context);
    }

    /**
     * 显示toast
     *
     * @param msg 默认底部显示
     */
    public static void showToast(String msg) {
        showToast(msg, Gravity.BOTTOM);
    }

    /**
     * 显示toast
     *
     * @param msg
     * @param gravity 显示的位置(Gravity.BOTTOM)
     */
    public static void showToast(String msg, int gravity) {
        mToast.show(msg, gravity);
    }

    public static void cancelToast() {
        mToast.cancel();
    }

    private Toast toast;

    @Inject
    public ToastUtil(Context context) {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    /**
     * 显示toast
     *
     * @param msg 默认底部显示
     */
    public void show(String msg) {
        show(msg, Gravity.BOTTOM);
    }

    /**
     * 显示toast
     *
     * @param msg
     * @param gravity 显示的位置(Gravity.BOTTOM)
     */
    public void show(String msg, int gravity) {
        synchronized (toast) {
            toast.setText(msg);
            toast.setGravity(gravity, 0, 100);
            toast.show();
        }
    }

    public void cancel() {
        synchronized (toast) {
            toast.cancel();
        }
    }
}
