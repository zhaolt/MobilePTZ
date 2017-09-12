package com.ziguang.ptz.core.data;

/**
 * Created by zhaoliangtai on 2017/9/12.
 */

public class Head implements Span {

    private long date;

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    @Override
    public int getSpan() {
        return SPAN_HEAD;
    }
}
