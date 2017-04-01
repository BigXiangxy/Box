package com.box.lib.app;

import com.box.lib.utils.LastingUtil;

import java.io.Serializable;

/**
 * 用户
 * Created by xxy on 2016/7/21.
 */
public final class UserBean implements Serializable {

    private String realName;
    private String id;


    private static final String user_key = "user_key";
    private static UserBean _install;

    /**
     * 获取实例,如果有缓存系统将获取缓存中的对象
     *
     * @return
     */
    public static UserBean getInstall() {
        if (_install == null)
            _install = LastingUtil.readObject(MainApp.getApp().getApplicationContext(), user_key);
        return _install;
    }

    /**
     * 保存当前用户信息
     */
    public boolean saveInfo() {
        if (LastingUtil.saveObject(MainApp.getApp().getApplicationContext(), this, user_key)) {
            _install = this;
            return true;
        }
        return false;
    }

    /**
     * 清空存储
     *
     * @return
     */
    public boolean delete() {
        if (LastingUtil.delete(MainApp.getApp().getApplicationContext(), user_key)) {
            _install = null;
            return true;
        }
        return false;
    }
}

