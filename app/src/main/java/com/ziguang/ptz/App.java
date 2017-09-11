package com.ziguang.ptz;

import android.app.Application;

import com.ziguang.ptz.rx.NoActionSubscriber;
import com.ziguang.ptz.utils.FileUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class App extends Application {

    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
        createDirs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new NoActionSubscriber());
    }

    public Observable createDirs() {
        // 创建文件夹系统
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                FileUtils.createDirs();
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
        });
    }
}
