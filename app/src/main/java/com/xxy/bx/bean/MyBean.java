package com.xxy.bx.bean;

import android.util.Log;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class MyBean {

    @Inject
    public MyBean() {
    }

    public void L(){
        Log.e("--","------");
    }
}
