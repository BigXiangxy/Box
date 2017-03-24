package com.box.lib.loction;


import com.baidu.location.BDLocation;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

/**
 * 定位事件总线（请只用于定位-用于其他将会有不可预知错误）
 */
public class RxLocBus {
    private static volatile RxLocBus defaultInstance;
    private final BehaviorSubject bus;// PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者

    private RxLocBus() {
        bus = BehaviorSubject.create();
    }

    /**
     * 取得事件总线
     *
     * @return
     */
    public static RxLocBus getDefault() {
        RxLocBus rxBus = defaultInstance;
        if (defaultInstance == null) {
            synchronized (RxLocBus.class) {
                rxBus = defaultInstance;
                if (defaultInstance == null) {
                    rxBus = new RxLocBus();
                    defaultInstance = rxBus;
                }
            }
        }
        return rxBus;
    }

    /**
     * 发射一个位置信息
     *
     * @param bdLocation
     */
    public void post(BDLocation bdLocation) {
        bus.onNext(bdLocation);
    }

    public void postError(Throwable e) {
        bus.onError(e);
    }

    /**
     * BDLocation
     *
     * @return
     */
    public Observable<BDLocation> toObserverable() {
        return bus;
    }
}