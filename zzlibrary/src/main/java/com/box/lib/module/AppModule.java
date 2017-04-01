package com.box.lib.module;

import android.content.Context;

import com.box.lib.app.MainApp;
import com.box.lib.inject.AppScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/3/13 0013.
 */
@Module
public class AppModule {
    private MainApp mainApp;

    public AppModule(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @AppScope
    @Provides
    MainApp provideApp() {
        return mainApp;
    }

    @AppScope
    @Provides
    Context provideContext() {
        return mainApp.getApplicationContext();
    }
}
