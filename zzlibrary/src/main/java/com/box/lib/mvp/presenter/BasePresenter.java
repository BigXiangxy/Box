package com.box.lib.mvp.presenter;

import com.box.lib.app.MainApp;
import com.box.lib.mvp.view.IView;
import com.box.lib.utils.NetUtil;
import com.box.lib.utils.ToastUtil;
import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/3/9 0009.
 */

public abstract class BasePresenter<V extends IView> {
    private LifecycleProvider lifecycleProvider;
    protected V view;

    public BasePresenter(LifecycleProvider lifecycleProvider) {
        this.lifecycleProvider = lifecycleProvider;
    }

    public void setView(V view) {
        this.view = view;
    }

    /**
     * 是否包含了网络操作
     *
     * @param isNetWork
     * @param <T>
     * @return
     */
    public <T> ObservableTransformer<T, T> applySchedulers(final boolean isNetWork) {
        return new ObservableTransformer<T, T>() {

            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                if (lifecycleProvider != null) {
                    upstream.compose(lifecycleProvider.bindToLifecycle());
                }
                return upstream.doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        if (isNetWork && !disposable.isDisposed() && !NetUtil.isNetworkAvailable(MainApp.getApp().getApplicationContext())) {
                            ToastUtil.showToast("当前网络不可用，请检查您的网络设置");
                            disposable.dispose();
                        }
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
