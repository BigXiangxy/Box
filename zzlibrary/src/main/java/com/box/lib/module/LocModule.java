package com.box.lib.module;

import android.content.Context;

import com.box.lib.loction.LocationService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/3/14 0014.
 */
@Module
public class LocModule {
    @Singleton
    @Provides
    public LocationService provideLoctionService(Context context) {
        return new LocationService(context);
    }
}
