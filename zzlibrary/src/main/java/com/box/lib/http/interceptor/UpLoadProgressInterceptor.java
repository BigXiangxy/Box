package com.box.lib.http.interceptor;

import android.util.Log;

import com.box.lib.http.HttpBase;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 上传进度拦截器
 */
public class UpLoadProgressInterceptor implements Interceptor {

    public UpLoadProgressInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String tag = request.header(HttpBase.PROGRESS_UP_TAG);
        if (null == tag || null == request.body())
            return chain.proceed(request);
        Request progressRequest = request.newBuilder()
                .removeHeader(HttpBase.PROGRESS_UP_TAG)
                .method(request.method(), new ProgressRequestBody(request.body(), progressListener))
                .build();
        return chain.proceed(progressRequest);
    }


    private ProgressRequestBody.Listener progressListener = new ProgressRequestBody.Listener() {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, long networkSpeed) {
            Log.e("-", "bytesWritten=" + bytesWritten + "  contentLength=" + contentLength + "  networkSpeed" + networkSpeed);
        }
    };
}