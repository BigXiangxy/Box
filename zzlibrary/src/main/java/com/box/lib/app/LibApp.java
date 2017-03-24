package com.box.lib.app;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.BuildConfig;
import com.box.lib.component.AppComponent;
import com.box.lib.component.DaggerAppComponent;
import com.box.lib.module.AppModule;
import com.box.lib.utils.ToastUtil;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class LibApp extends Application {
    private AppComponent appComponent;
    private static LibApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);
        initRouter();
        ARouter.getInstance().inject(this);
        app = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(app)).build();
    }

    public static LibApp getApp() {
        return app;
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    private void initRouter() {
        if (BuildConfig.DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
