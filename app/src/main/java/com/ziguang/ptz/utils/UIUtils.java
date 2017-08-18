package com.ziguang.ptz.utils;

import android.content.Context;
import android.view.View;

import com.ziguang.ptz.App;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class UIUtils {

    public static View inflate(int vId) {
        return View.inflate(getContext(), vId, null);
    }

    public static Context getContext() {
        return App.INSTANCE;
    }
}
