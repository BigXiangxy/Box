package com.xxy.bx.view.presenter;

import android.util.Log;

import com.box.lib.inject.ActivityScope;
import com.box.lib.loction.LocationService;
import com.box.lib.loction.RxLocBus;
import com.box.lib.mvp.presenter.BasePresenter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.xxy.bx.view.iv.MView;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2017/3/9 0009.
 */
@ActivityScope
public class MainPresenter extends BasePresenter<MView> {
    private LocationService locationService;
    private Disposable disposable;

    @Inject
    public MainPresenter(LocationService locationService, LifecycleProvider lifecycleProvider) {
        super(lifecycleProvider);
        this.locationService = locationService;
    }

    public void start() {
        locationService.start();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
        disposable = RxLocBus.getDefault().toObserverable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> Log.e("-", location.getAddrStr()), Throwable::printStackTrace);
    }

    public void stop() {
        locationService.stop();
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }
}
