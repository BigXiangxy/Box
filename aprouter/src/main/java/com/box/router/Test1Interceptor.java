package com.box.router;

import android.content.Context;
import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;

/**
 * 比较经典的应用就是在跳转过程中处理登陆事件，这样就不需要在目标页重复做登陆检查
 * 拦截器会在跳转之间执行，多个拦截器会按优先级顺序依次执行
 */
@Interceptor(priority = 7)
public class Test1Interceptor implements IInterceptor {
    Context mContext;

    /**
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if ((1 << 0 & postcard.getExtra()) == 1 << 0) {
            Log.e("--", "---->" + 0);
        }
        if ((1 << 2 & postcard.getExtra()) == 1 << 2) {
            Log.e("--", "---->" + 2);
        }
        if ((1 << 4 & postcard.getExtra()) == 1 << 4) {
            Log.e("--", "---->" + 4);
        }
        if ((1 << 28 & postcard.getExtra()) == 1 << 28) {
            Log.e("--", "---->" + 28);
        }
        if ((1 << 31 & postcard.getExtra()) == 1 << 31) {
            Log.e("--", "---->" + 31);
        }
        if ((1 << 30 & postcard.getExtra()) == 1 << 30) {
            Log.e("--", "---->" + 30);
        }
        if ((1 << 29 & postcard.getExtra()) == 1 << 29) {
            Log.e("--", "---->" + 29);
        }
        if ("/more/main".equals(postcard.getPath())) {
//            callback.onInterrupt(new RuntimeException("我觉得有点异常"));//取消跳转
            postcard.withString("extra", "我是在拦截器中附加的参数");
            callback.onContinue(postcard);//继续跳转
            Log.e("extrs", "------>" + postcard.getExtra());
//            ARouter.getInstance().build("/map/main").navigation();//也能跳转其他界面
        } else {
            Log.e("Test1Interceptor", postcard.getPath());
            callback.onContinue(postcard);
        }
    }

    /**
     * 会在sdk初始化的时候调用该方法，仅会调用一次
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {
        mContext = context;
        Log.e("testService", Test1Interceptor.class.getName() + " has init.");
    }
}
