package com.box.lib.http.interceptor;

import com.box.lib.http.model.HttpHeaders;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 全局公共请求头
 * Created by Administrator on 2017/4/18 0018.
 */

public class HeaderInterceptor implements Interceptor {

    private HttpHeaders mCommonHeaders;//全局公共请求头

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder request = chain.request().newBuilder();
        if (mCommonHeaders.headersMap.isEmpty())
            return chain.proceed(request.build());
        try {
            for (Map.Entry<String, String> entry : mCommonHeaders.headersMap.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
            //String headerValue = URLEncoder.encode(entry.getValue(), "UTF-8");//对头信息进行 utf-8 编码,防止头信息传中文,这里暂时不编码,可能出现未知问题,如有需要自行编码
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chain.proceed(request.build());
    }

    /**
     * 获取全局公共请求头
     */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /**
     * 添加全局公共请求参数
     *
     * @param commonHeaders
     * @return
     */
    public HeaderInterceptor addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }
}
