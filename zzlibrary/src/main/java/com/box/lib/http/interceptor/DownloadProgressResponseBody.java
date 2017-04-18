package com.box.lib.http.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by Administrator on 2016/7/18 0018.
 */
public class DownloadProgressResponseBody extends ResponseBody {

    private String progressKey;
    private final ResponseBody responseBody;
    private BufferedSource bufferedSource;

    public DownloadProgressResponseBody(ResponseBody responseBody, String progressKey) {
        this.responseBody = responseBody;
        this.progressKey = progressKey;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
//                RxBus.getDefault().post(new ProgressInfo(progressKey, totalBytesRead, responseBody.contentLength()));
                Log.e("Download", "totalBytesRead=" + totalBytesRead + "  contentLength=" + responseBody.contentLength());
                return bytesRead;
            }
        };
    }
}
