package com.ziguang.ptz.utils;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by zhaoliangtai on 2017/9/4.
 */

public class ActivityUtils {
    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment);
        transaction.commitAllowingStateLoss();
    }

    public static void addFragmentToActivity (@NonNull FragmentManager fragmentManager,
                                              @NonNull Fragment fragment, int frameId, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(frameId, fragment, tag);
        transaction.commitAllowingStateLoss();
    }
}
