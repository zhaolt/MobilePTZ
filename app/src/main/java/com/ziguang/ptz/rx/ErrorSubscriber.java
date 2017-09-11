package com.ziguang.ptz.rx;

import rx.Subscriber;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public abstract class ErrorSubscriber extends Subscriber {
    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {
        doOnError(e);
    }

    @Override
    public void onNext(Object o) {}

    public abstract void doOnError(Throwable e);
}
