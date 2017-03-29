package com.box.lib.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/3/28 0028.
 */

public class WindowUtil {

    public static void getwm(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();//定义DisplayMetrics 对象
        wm.getDefaultDisplay().getMetrics(dm);//取得窗口属性
        int screenWidth = dm.widthPixels;//窗口的宽度
        int screenHeight = dm.heightPixels;//窗口高度
    }
}
