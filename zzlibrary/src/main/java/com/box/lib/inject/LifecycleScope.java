package com.box.lib.inject;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Administrator on 2017/3/14 0014.
 * 生命与@ActivityScope相同
 */
@Scope
@Retention(RUNTIME)
public @interface LifecycleScope {
}
