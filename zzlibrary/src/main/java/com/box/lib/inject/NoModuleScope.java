package com.box.lib.inject;

import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**子
 * Created by Administrator on 2017/3/14 0014.
 */
@Scope
@Retention(RUNTIME)
public @interface NoModuleScope {
}
