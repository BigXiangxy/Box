package com.box.lib.mvp.presenter;

import com.box.lib.mvp.view.IView;
import com.box.lib.utils.NetUtil;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.LifecycleTransformer;

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
     * 绑定生命周期
     *
     * @param <T>
     * @return
     */
    protected final <T> LifecycleTransformer<T> bindToLifecycle() {
        return lifecycleProvider.bindToLifecycle();
    }

    /**
     * 检查网络(使用了 doOnSubscribe()方法)
     * 添加了基本线程调度
     * 绑定了生命周期
     *
     * @param <T>
     * @return
     */
    protected final <T> ObservableTransformer<T, T> checkNetWork() {
        return checkNetWork(null);
    }

    /**
     * 检查网络(使用了 doOnSubscribe()方法)
     * 添加了基本线程调度
     * 绑定了生命周期
     *
     * @param <T>
     * @return
     */
    protected final <T> ObservableTransformer<T, T> checkNetWork(Consumer<Disposable> consumer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable upstream) {
                return upstream.doOnSubscribe(new NetUtil.CheckNetWorkConsumer(consumer))
                        .subscribeOn(Schedulers.io())
                        .compose(bindToLifecycle())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


}
