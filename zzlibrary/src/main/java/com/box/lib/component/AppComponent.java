package com.box.lib.component;

import android.content.Context;

import com.box.lib.app.MainApp;
import com.box.lib.inject.AppScope;
import com.box.lib.loction.LocationService;
import com.box.lib.module.AppModule;
import com.box.lib.utils.AppActivityManager;

import dagger.Component;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

@AppScope
@Component(modules = {AppModule.class})
public interface AppComponent {

    MainApp provideApp();

    Context provideContext();

    LocationService provideLocationService();

    AppActivityManager provideActivityManager();

    void inject(MainApp app);
}
