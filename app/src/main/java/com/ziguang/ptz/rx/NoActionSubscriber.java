package com.ziguang.ptz.rx;

import rx.Subscriber;

/**
 * Created by zhaoliangtai on 2017/9/11.
 */

public class NoActionSubscriber extends Subscriber {

    @Override
    public void onCompleted() {}

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onNext(Object o) {}

}
