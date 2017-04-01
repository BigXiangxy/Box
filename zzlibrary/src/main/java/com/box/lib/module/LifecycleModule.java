package com.box.lib.module;

import com.box.lib.inject.LifecycleScope;
import com.trello.rxlifecycle2.LifecycleProvider;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Administrator on 2017/3/21 0021.
 */

@Module
public class LifecycleModule {
    private LifecycleProvider mLifecycleProvider;

    public LifecycleModule(LifecycleProvider lifecycleProvider) {
        mLifecycleProvider = lifecycleProvider;
    }

    @LifecycleScope
    @Provides
    LifecycleProvider providerLifecycle() {
        return mLifecycleProvider;
    }
}
