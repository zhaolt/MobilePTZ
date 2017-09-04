package com.ziguang.ptz.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by zhaoliangtai on 17/3/16.
 */

public class PermissionUtils {

    private volatile static PermissionUtils instance;
    public static final int REQUEST_STATUS_CODE = 10086;
    //必须要申请的权限,可通过setMustRequirePermission 改变
    public String[] PERMISSIONS_GROUP_SORT = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    //    private PermissionCallbacks callbacks;
    private ArrayList<String> currentHandlerDenyArray;

    private PermissionUtils() {
    }

    public static PermissionUtils getInstance() {
        if (null == instance) {
            synchronized (PermissionUtils.class) {
                if (null == instance)
                    instance = new PermissionUtils();
            }
        }
        return instance;
    }

    /**
     * 1.先设置必须的权限组,这些权限必须要在AndroidManifest.xml中声明
     *
     * @param mustPermissionsArray
     */
    public void setMustRequirePermission(String[] mustPermissionsArray) {
        PERMISSIONS_GROUP_SORT = mustPermissionsArray;
    }

    /**
     * 2. 获取所有必须权限中还未被允许的权限
     *
     * @param activity
     * @param callback
     */
    public void checkAndRequestAllPermissions(final Activity activity, PermissionCallback callback) {
        if (activity == null || callback == null)
            return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> deniedArray = new ArrayList<>();
            for (String permission : PERMISSIONS_GROUP_SORT) {
                if (isPermissionDenied(activity, permission)) {
                    Log.d("permission_deny", permission);
                    deniedArray.add(permission);
                }
            }
            if (deniedArray.size() > 0) {
                currentHandlerDenyArray = new ArrayList<>();
                currentHandlerDenyArray.addAll(deniedArray);
                String[] deniedPermissions = currentHandlerDenyArray.toArray(new String[currentHandlerDenyArray.size()]);
                requestPermissions(activity, deniedPermissions);
            } else {
                callback.onPermissionsGranted();
            }
        } else {
            callback.onPermissionsGranted();
        }
    }

    public void checkAllPermissions(Activity activity, PermissionCallback callbacks) {
        if (null == activity || null == callbacks)
            return;
        ArrayList<String> denidArray = new ArrayList<>();
        for (String permission : PERMISSIONS_GROUP_SORT) {
            if (isPermissionDenied(activity, permission)) {
                denidArray.add(permission);
            }
        }
        if (denidArray.size() > 0) {
            callbacks.onPermissionsDeniedForever(denidArray.toArray(new String[denidArray.size()]));
        } else {
            callbacks.onPermissionsGranted();
        }
    }



    public interface PermissionCallback {

        /**
         * 所有要求的权限都被允许
         */
        void onPermissionsGranted();

        /**
         * 监测到有权限还没被允许
         *
         * @param permissions
         */
        void onPermissionsDeniedForever(String[] permissions);

    }

    /**
     * 获取当前处理的权限
     *
     * @return
     */
    public ArrayList<String> getCurrentHandlerDenyArray() {
        return currentHandlerDenyArray;
    }

    public boolean isPermissionDenied(Activity activity, String permission) {
        int grantCode = ActivityCompat.checkSelfPermission(activity, permission);
        return grantCode == PackageManager.PERMISSION_DENIED;
    }

    private void requestPermissions(Activity activity, String[] permissions) {
        Log.d("permission_deny_handler", permissions[0]);
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_STATUS_CODE);
    }

    public boolean isAppFirstRun(Activity activity) {
        SharedPreferences sp = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        if (sp.getBoolean("first_run", true)) {
            return true;
        }
        return false;
    }

    public void setAppFirstRun(Activity activity, boolean a) {
        SharedPreferences sp = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("first_run", a);
        editor.commit();
    }

}
