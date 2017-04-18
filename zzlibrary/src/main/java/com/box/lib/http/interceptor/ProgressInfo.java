package com.box.lib.http.interceptor;

/**
 * 进度实体
 * Created by Administrator on 2016/7/19 0019.
 */
public class ProgressInfo {
    /**
     * 总共
     */
    public long total;
    /**
     * 已读
     */
    public long totalBytesRead;
    /**
     * 标志位
     */
    public String mark;

    public ProgressInfo() {
    }

    public ProgressInfo(String mark, long totalBytesRead, long total) {
        this.mark = mark;
        this.totalBytesRead = totalBytesRead;
        this.total = total;
    }


}
