package com.box.lib.http;

import com.box.lib.BuildConfig;
import com.box.lib.http.interceptor.DownloadProgressInterceptor;
import com.box.lib.http.interceptor.HeaderInterceptor;
import com.box.lib.http.interceptor.UpLoadProgressInterceptor;
import com.box.lib.http.model.HttpHeaders;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Administrator on 2017/4/18 0018.
 */

public class OkHttpBase {
    private static OkHttpBase okHttpBase;
    public static final int DEFAULT_MILLISECONDS = 60000;       //默认的超时时间
    public static int REFRESH_TIME = 100;                       //进度回调刷新时间（单位ms）

    private OkHttpClient okHttpClient;                          //ok请求的客户端
    private OkHttpClient.Builder okHttpClientBuilder;           //ok请求的客户端
    private HeaderInterceptor headerInterceptor;
    private int mRetryCount = 3;                                //全局超时重试次数

//    private CacheMode mCacheMode;                               //全局缓存模式
//    private long mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;   //全局缓存过期时间,默认永不过期
//    private CookieJarImpl cookieJar;                            //全局 Cookie 实例
//    private HttpParams mCommonParams;                           //全局公共请求参数

    private OkHttpBase() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder
                .hostnameVerifier(HttpsUtils.UnSafeHostnameVerifier)//https的自定义域名访问规则
                .connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS)
                .addInterceptor(new UpLoadProgressInterceptor())
                .addInterceptor(new DownloadProgressInterceptor());
        if (BuildConfig.DEBUG)
            okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        headerInterceptor = new HeaderInterceptor();
    }

    public static OkHttpBase getInstance() {
        if (okHttpBase == null) {
            synchronized (OkHttpBase.class) {
                if (okHttpBase == null)
                    okHttpBase = new OkHttpBase();
            }
        }
        return okHttpBase;
    }

    public OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = okHttpClientBuilder.build();
            okHttpClientBuilder.addInterceptor(new HeaderInterceptor());
        }
        return okHttpClient;
    }

    /**
     * 获取全局公共请求头
     */
    public HeaderInterceptor getHeaderInterceptor() {
        return headerInterceptor;
    }

    /**
     * 添加全局公共请求参数
     *
     * @param commonHeaders
     * @return
     */
    public OkHttpBase addCommonHeaders(HttpHeaders commonHeaders) {
        headerInterceptor.addCommonHeaders(commonHeaders);
        return this;
    }

    /**
     * 添加全局拦截器
     */
    public OkHttpBase addInterceptor(Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    //----------------------------- https 证书/域名解析 -----------------------------------------------------------

    /**
     * https的自定义域名访问规则
     */
    public OkHttpBase setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    /**
     * https单向认证
     * 用含有服务端公钥的证书校验服务端证书
     *
     * @param certificates
     * @return
     */
    public OkHttpBase setCertificates(InputStream... certificates) {
        setCertificates(null, null, certificates);
        return this;
    }

    /**
     * https单向认证
     * 可以额外配置信任服务端的证书策略，否则默认是按CA证书去验证的，若不是CA可信任的证书，则无法通过验证
     *
     * @param trustManager
     * @return
     */
    public OkHttpBase setCertificates(X509TrustManager trustManager) {
        setCertificates(null, null, trustManager);
        return this;
    }

    /**
     * https双向认证
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     * certificates -> 用含有服务端公钥的证书校验服务端证书
     */
    public OkHttpBase setCertificates(InputStream bksFile, String password, InputStream... certificates) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(null, bksFile, password, certificates);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }

    /**
     * https双向认证
     * bksFile 和 password -> 客户端使用bks证书校验服务端证书
     * X509TrustManager -> 如果需要自己校验，那么可以自己实现相关校验，如果不需要自己校验，那么传null即可
     */
    public OkHttpBase setCertificates(InputStream bksFile, String password, X509TrustManager trustManager) {
        HttpsUtils.SSLParams sslParams = HttpsUtils.getSslSocketFactory(trustManager, bksFile, password, null);
        okHttpClientBuilder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
        return this;
    }
}
