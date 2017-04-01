package com.box.lib.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.box.lib.BuildConfig;
import com.box.lib.component.AppComponent;
import com.box.lib.component.DaggerAppComponent;
import com.box.lib.module.AppModule;
import com.box.lib.utils.AppActivityManager;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/3/10 0010.
 */

public class MainApp extends Application {
    private AppComponent appComponent;
    private static MainApp app;
    @Inject
    AppActivityManager activityManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initRouter();
        ARouter.getInstance().inject(this);
        app = this;
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(app)).build();
        appComponent.inject(this);
    }

    public static MainApp getApp() {
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

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void Exit() {
        try {
            activityManager.removeAll();
            ActivityManager activityMgr = (ActivityManager) app.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(app.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
