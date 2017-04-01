package com.box.lib.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;

import com.box.lib.app.MainApp;
import com.box.lib.component.DaggerLifecycleComponent;
import com.box.lib.component.LifecycleComponent;
import com.box.lib.module.LifecycleModule;
import com.box.lib.utils.AppActivityManager;
import com.box.lib.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import javax.inject.Inject;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public class BaseActivity extends RxAppCompatActivity {
    private LifecycleComponent lifecycleComponent;
    @Inject
    AppActivityManager activityManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (lifecycleComponent == null)
            lifecycleComponent = DaggerLifecycleComponent.builder().appComponent(((MainApp) getApplication()).getAppComponent()).lifecycleModule(new LifecycleModule(this)).build();
        lifecycleComponent.inject(this);
        activityManager.add(this);
    }

    @Override
    protected void onDestroy() {
        activityManager.remove(this);
        super.onDestroy();
    }

    protected <T extends Activity> T getActivity() {
        return (T) this;
    }

    protected LifecycleComponent getLifecycleComponet() {
        return lifecycleComponent;
    }

    /**
     * 显示toast
     *
     * @param msg ,
     *            默认底部显示
     */
    public void showToast(String msg) {
        showToast(msg, Gravity.BOTTOM);
    }

    /**
     * 显示toast
     *
     * @param msg
     * @param gravity 显示的位置(Gravity.BOTTOM)
     */
    public void showToast(String msg, int gravity) {
        ToastUtil.getInstance().show(msg, gravity);
    }
}
