package com.box.lib.component;

import android.content.Context;

import com.box.lib.module.AppModule;
import com.box.lib.app.LibApp;
import com.box.lib.module.LocModule;
import com.box.lib.loction.LocationService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Administrator on 2017/3/13 0013.
 */

@Singleton
@Component(modules = {AppModule.class, LocModule.class})
public interface AppComponent {

    LibApp provideApp();

    Context provideContext();

    LocationService provideLocationService();
}
