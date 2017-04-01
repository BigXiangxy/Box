package com.box.lib.route_service;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * Created by Administrator on 2017/3/16 0016.
 */

public interface SayHelloService extends IProvider {
    String say(String name);
}
