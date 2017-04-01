package com.box.lib.component;

import android.content.Context;

import com.box.lib.app.MainApp;
import com.box.lib.inject.LifecycleScope;
import com.box.lib.loction.LocationService;
import com.box.lib.module.LifecycleModule;
import com.box.lib.mvp.view.BaseActivity;
import com.box.lib.utils.AppActivityManager;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Component;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
@LifecycleScope
@Component(dependencies = {AppComponent.class}, modules = {LifecycleModule.class})
public interface LifecycleComponent {
    MainApp provideApp();

    Context provideContext();

    LocationService provideLocationService();

    LifecycleProvider providerLifecycle();

    AppActivityManager providerActivityManager();

    void inject(BaseActivity baseActivity);
}
