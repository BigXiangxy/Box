package com.box.lib.http;


import com.box.lib.BuildConfig;
import com.box.lib.http.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Box Administrator on 2016/7/18 0018.
 */
public class HttpBase {
    private static final int DEFAULT_TIMEOUT = 5;//超时s
    /**
     * 默认URL
     */
    private static String BASE_URL = BuildConfig.DEBUG + "";

    private String urlHome;//URL地址
    private Retrofit retrofit;


    private OkHttpClient.Builder defaultOkHttpClientBuilder() {
        return defaultOkHttpClientBuilder(null);
    }

    private OkHttpClient.Builder defaultOkHttpClientBuilder(Interceptor[] interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        if (interceptors != null)
            for (Interceptor i : interceptors)
                if (i != null) okHttpClientBuilder.addInterceptor(i);
        return okHttpClientBuilder;
    }

    private void buildRetrofit(String url, OkHttpClient.Builder builder) {
        this.urlHome = url;
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .baseUrl(urlHome)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    private HttpBase(String url) {
        buildRetrofit(url, defaultOkHttpClientBuilder());
    }

    private HttpBase(String url, OkHttpClient.Builder builder) {
        buildRetrofit(url, builder);
    }

    private static class SingletonHolder {
        private static final HttpBase INSTANCE = new HttpBase(BASE_URL);
    }

    /**
     * 默认网络
     *
     * @return
     */
    public static HttpBase getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 取得临时，给与临时某些外部地址使用方便,能不用就不用这个
     *
     * @param baseUrl
     * @return
     */
    public static HttpBase getTempInstance(String baseUrl) {
        if (baseUrl == BASE_URL) return SingletonHolder.INSTANCE;
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
