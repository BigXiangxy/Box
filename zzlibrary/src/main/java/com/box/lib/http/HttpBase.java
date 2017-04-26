package com.box.lib.http;


import com.box.lib.BuildConfig;
import com.box.lib.http.gson.GsonConverterFactory;
import com.box.lib.http.interceptor.ProgressInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Box Administrator on 2016/7/18 0018.
 */
public class HttpBase {
    public static final String PROGRESS_DOWN_TAG = "progress_tag_down";
    public static final String PROGRESS_UP_TAG = "progress_tag_up";

    private static HttpBase httpBase;
    /**
     * 默认URL
     */
    private static String BASE_URL = "http://cdnq.duitang.com";//https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1492516121489&di=76e7fcc080a9e5cc7e98f5f6f4148837&imgtype=0&src=http%3A%2F%2Fimg.bbs.pchome.net%2Fdcbbs%2F263_1000%2F262894.jpg

    private Retrofit retrofit;

    /**
     * @param url
     */
    private HttpBase(String url) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .client(OkHttpBase.getInstance()
                            .addInterceptor(new ProgressInterceptor())
                            .buildOkHttpClient())
                    .baseUrl(url)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(new CallAdapter.Factory() {
                        @Override
                        public CallAdapter<String, Boolean> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
                            return new CallAdapter<String, Boolean>() {
                                @Override
                                public Type responseType() {
                                    return null;
                                }

                                @Override
                                public Boolean adapt(Call<String> call) {
                                    return null;
                                }
                            };
                        }
                    })
                    .build();
        }
    }

    /**
     * 统一请求类
     *
     * @return
     */
    public static HttpBase getInstance() {
        if (httpBase == null) {
            synchronized (HttpBase.class) {
                if (httpBase == null)
                    httpBase = new HttpBase(BASE_URL);
            }
        }
        return httpBase;
    }

    /**
     * 取得临时，给与临时某些外部地址使用方便,能不用就不用这个
     *
     * @param baseUrl
     * @return
     */
    public static HttpBase getTempInstance(String baseUrl) {
        if (baseUrl == BASE_URL) return getInstance();
        return new HttpBase(baseUrl);
    }

    /**
     * 取得非单例实体,主要用于调试时方便
     *
     * @param baseUrl
     * @return
     */
    public static HttpBase getDebugInstance(String baseUrl) {
        return BuildConfig.DEBUG ? null : new HttpBase(baseUrl);
    }

    /**
     * 创建API
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T createApi(final Class<T> service) {
        if (retrofit == null)
            throw new RuntimeException("Retrofit is null.");
        return retrofit.create(service);
    }
}
