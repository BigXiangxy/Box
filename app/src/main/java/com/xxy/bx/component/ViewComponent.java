package com.xxy.bx.component;

import com.box.lib.component.LifecycleComponent;
import com.box.lib.inject.ActivityScope;
import com.xxy.bx.view.MainActivity;

import dagger.Component;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
@ActivityScope
@Component(dependencies = {LifecycleComponent.class})
public interface ViewComponent {
    void inject(MainActivity mainActivity);
}
