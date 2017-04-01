package com.box.mapmodule;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.box.lib.route_service.SayHelloService;
import com.box.lib.utils.ToastUtil;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
@Route(path = "/service/sayhello")
public class SayNameService implements SayHelloService {
    @Override
    public String say(String name) {
        ToastUtil.showToast(name);
        Log.e("SayNameService", "namne---------------------!");
        return "say: " + name;
    }

    @Override
    public void init(Context context) {
        Log.e("SayNameService", "SayNameService  init!!!");
    }
}
