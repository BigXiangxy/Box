package com.box.lib.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.box.lib.app.MainApp;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
public class ToastUtil {
    private static ToastUtil mToast;
    private Toast toast;

    private ToastUtil() {
        toast = Toast.makeText(MainApp.getApp(), "", Toast.LENGTH_SHORT);
    }

    public static ToastUtil getInstance() {
        if (mToast == null) {
            synchronized (ToastUtil.class) {
                if (mToast == null)
                    mToast = new ToastUtil();
            }
        }
        return mToast;
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
        toast.setText(msg);
        toast.setGravity(gravity, 0, 100);
        toast.show();
    }
}
