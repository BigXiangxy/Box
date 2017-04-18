package com.box.lib.http.interceptor;


import com.box.lib.http.HttpBase;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载进度
 */
public class DownloadProgressInterceptor implements Interceptor {

    public DownloadProgressInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String tag = request.header(HttpBase.PROGRESS_DOWN_TAG);
        if (null != tag)
            request.newBuilder().removeHeader(HttpBase.PROGRESS_DOWN_TAG);
        Response originalResponse = chain.proceed(request);
        if (null != tag)
            return originalResponse.newBuilder()
                    .body(new DownloadProgressResponseBody(originalResponse.body(), tag))
                    .build();
        return originalResponse;
    }
}