package com.box.lib.component;

import android.content.Context;

import com.box.lib.app.LibApp;
import com.box.lib.inject.ActivityScope;
import com.box.lib.loction.LocationService;
import com.box.lib.module.LifecycleModule;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Component;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = {LifecycleModule.class})
public interface LifecycleComponent {
    LibApp provideApp();

    Context provideContext();

    LocationService provideLocationService();

    LifecycleProvider providerLifecycle();
}
