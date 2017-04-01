package com.box.lib.mvp.view;

import android.view.Gravity;

import com.box.lib.app.MainApp;
import com.box.lib.component.DaggerLifecycleComponent;
import com.box.lib.component.LifecycleComponent;
import com.box.lib.module.LifecycleModule;
import com.box.lib.utils.ToastUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public abstract class BaseFragment extends RxFragment {
    private LifecycleComponent lifecycleComponent;

    protected LifecycleComponent getLifecycleComponet() {
        if (lifecycleComponent == null)
            lifecycleComponent = DaggerLifecycleComponent.builder().appComponent(((MainApp) getContext()).getAppComponent()).lifecycleModule(new LifecycleModule(this)).build();
        return lifecycleComponent;
    }

    /**
     * 显示toast
     *
     * @param msg ,
     *            默认底部显示
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
        ToastUtil.getInstance().show(msg, gravity);
    }
}
