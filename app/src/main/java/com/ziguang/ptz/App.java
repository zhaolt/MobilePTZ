package com.ziguang.ptz;

import android.app.Application;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class App extends Application {

    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
