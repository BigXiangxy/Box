package com.box.lib.loction;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.box.lib.inject.AppScope;

import javax.inject.Inject;

/**
 * @author baidu
 */
@AppScope
public class LocationService implements BDLocationListener {
    private Object objLock = new Object();
    private LocationClient client = null;

    @Inject
    public LocationService(Context context) {
        synchronized (objLock) {
            if (client == null) {
                client = new LocationClient(context);
                setLocationOption(OptionBuilder.create().build());
            }
        }
    }

    /**
     * 需要重新调用start()方法
     *
     * @param option
     * @return
     */
    public void setLocationOption(LocationClientOption option) {
        synchronized (objLock) {
            if (option == null) throw new RuntimeException("配置定位参数失败");
            if (client.isStarted()) client.stop();
            client.setLocOption(option);
        }
    }

    /**
     * 启动定位
     */
    public void start() {
        synchronized (objLock) {
            if (client != null && !client.isStarted()) {
                registerListener(this);
                client.start();
            }
        }
    }

    public void stop() {
        synchronized (objLock) {
            if (client != null && client.isStarted()) {
                unregisterListener(this);
                client.stop();
            }
        }
    }

    /***
     * @param listener
     * @return
     */

    public boolean registerListener(BDLocationListener listener) {
        boolean isSuccess = false;
        if (listener != null) {
            client.registerLocationListener(listener);
            isSuccess = true;
        }
        return isSuccess;
    }

    public void unregisterListener(BDLocationListener listener) {
        if (listener != null) {
            client.unRegisterLocationListener(listener);
        }
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (null == bdLocation) return;
        switch (bdLocation.getLocType()) {
            case BDLocation.TypeGpsLocation:// 61 GPS定位结果
            case BDLocation.TypeOffLineLocation:// 66离线定位结果(没有具体位置只有经纬度)
            case BDLocation.TypeNetWorkLocation://161网络定位结果
                if (bdLocation.getCity() != null)
                    RxLocBus.getDefault().post(bdLocation);
                else Log.e("RxLocBus", "bdLocation.getCity() is null,so no post() !");
                break;
            case BDLocation.TypeServerError://167服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因
                RxLocBus.getDefault().postError(new RuntimeException("服务端网络定位失败"));
                break;
            case BDLocation.TypeNetWorkException://63网络不同导致定位失败，请检查网络是否通畅
                RxLocBus.getDefault().postError(new RuntimeException("定位失败,请检查网络是否通畅"));
                break;
            case BDLocation.TypeCriteriaException://62无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机
                RxLocBus.getDefault().postError(new RuntimeException("定位失败,无法获取有效定位依据"));
                break;
            default:
                Log.e("LocationService", "[" + bdLocation.getLocType() + "] 定位失败!");
                break;
        }
    }
}