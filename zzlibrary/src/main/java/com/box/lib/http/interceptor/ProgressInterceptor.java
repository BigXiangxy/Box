package com.box.lib.http.interceptor;


import android.util.Log;

import com.box.lib.http.HttpBase;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 下载进度
 */
public class ProgressInterceptor implements Interceptor {

    public ProgressInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String down_tag = request.header(HttpBase.PROGRESS_DOWN_TAG);
        String up_tag = request.header(HttpBase.PROGRESS_UP_TAG);
        if (null == up_tag && null == down_tag) return chain.proceed(request);

        Request.Builder requestBuilder = request.newBuilder();
        if (null != down_tag)
            requestBuilder.removeHeader(HttpBase.PROGRESS_DOWN_TAG);
        if (null != up_tag)
            requestBuilder.removeHeader(HttpBase.PROGRESS_UP_TAG);

        Request newRequest = request.body() == null ? requestBuilder.build() : requestBuilder.method(request.method(), new ProgressRequestBody(request.body(), progressListener)).build();

        Response originalResponse = chain.proceed(newRequest);
        if (null != down_tag)
            return originalResponse.newBuilder()
                    .body(new DownloadProgressResponseBody(originalResponse.body(), down_tag))
                    .build();
        return originalResponse;
    }


    private ProgressRequestBody.Listener progressListener = new ProgressRequestBody.Listener() {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, long networkSpeed) {
            Log.e("-", "bytesWritten=" + bytesWritten + "  contentLength=" + contentLength + "  networkSpeed" + networkSpeed);
        }
    };
}