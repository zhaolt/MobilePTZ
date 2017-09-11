package com.ziguang.ptz.rx;

import rx.Subscriber;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public abstract class NCSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {
        doOnError(e);
    }

    @Override
    public void onNext(T t) {
        doOnNext(t);
    }

    public abstract void doOnNext(T t);

    public abstract void doOnError(Throwable e);
}
