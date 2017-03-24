package com.box.lib.module;

import android.content.Context;

import com.box.lib.app.LibApp;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
@Module
public class AppModule {
    private LibApp libApp;

    public AppModule(LibApp libApp) {
        this.libApp = libApp;
    }

    @Singleton
    @Provides
    LibApp provideApp() {
        return libApp;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return libApp.getApplicationContext();
    }
}
