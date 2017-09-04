package com.ziguang.ptz.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ziguang.ptz.R;
import com.ziguang.ptz.utils.PermissionUtils;
import com.ziguang.ptz.widget.PermissionDialog;

import java.util.ArrayList;

/**
 * Created by zhaoliangtai on 2017/8/18.
 */

public class BaseActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_APP_MANAGER = 1001;

    private View mDecorView;

    private PermissionDialog mPermissionTips;

    private PermissionUtils.PermissionCallback mPermissionCallback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDecorView = getWindow().getDecorView();
    }

    // This snippet hides the system bars.
    public void hideSystemUI() {
        if (mDecorView == null) {
            return;
        }
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void getPermissions(PermissionUtils.PermissionCallback callback) {
        mPermissionCallback = callback;
        String[] mustPermission = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        PermissionUtils.getInstance().setMustRequirePermission(mustPermission);
        PermissionUtils.getInstance().checkAndRequestAllPermissions(this, mPermissionCallback);
    }

    public void getPermissions() {
        String[] mustPermission = {
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        PermissionUtils.getInstance().setMustRequirePermission(mustPermission);
        PermissionUtils.getInstance().checkAllPermissions(this, getDefaultCallback());
    }


    private PermissionUtils.PermissionCallback getDefaultCallback() {
        return new PermissionUtils.PermissionCallback() {
            @Override
            public void onPermissionsGranted() {
                if (null != mPermissionTips) {
                    mPermissionTips.dismiss();
                }
            }

            @Override
            public void onPermissionsDeniedForever(String[] permissions) {
                restartApp();
            }
        };
    }

    public void showTipsDialog(String permissionNames, String permissionHaoToGetTexts) {
        StringBuilder tipsContent = new StringBuilder();
        tipsContent.append(getString(R.string.permission_describe_reason, permissionNames));
        StringBuilder tipsWay = new StringBuilder();
        tipsWay.append(getString(R.string.permission_way, getString(R.string.app_name), permissionHaoToGetTexts));
        mPermissionTips = new PermissionDialog(this, tipsContent.toString(), tipsWay.toString());
        mPermissionTips.setOnDialogDismissListener(new PermissionDialog.OnDialogDismissListener() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.MAIN");
                intent.setClassName("com.android.settings", "com.android.settings.ManageApplications");
                startActivityForResult(intent, REQUEST_CODE_APP_MANAGER);
            }

            @Override
            public void onCancel() {
                mPermissionTips.dismiss();
                BaseActivity.this.finish();
            }
        });
        mPermissionTips.show();
    }

    private void parsePermissionResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        ArrayList<String> deniedPermissions = new ArrayList<>();
        if (permissions != null && permissions.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    continue;
                } else if (!permissions[i].equals(Manifest.permission.READ_PHONE_STATE)
                        && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() > 0) {
                mPermissionCallback.onPermissionsDeniedForever(deniedPermissions.toArray(new String[deniedPermissions.size()]));
            } else {
                mPermissionCallback.onPermissionsGranted();
            }
        }
    }

    private void restartApp() {
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PermissionUtils.REQUEST_STATUS_CODE) {
            parsePermissionResult(permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_APP_MANAGER) {
            ArrayList<String> currentHandlerDenyArray = PermissionUtils.getInstance().getCurrentHandlerDenyArray();
            if (currentHandlerDenyArray != null && currentHandlerDenyArray.size() > 0) {
                for (String deniedPermission : currentHandlerDenyArray) {
                    if (!PermissionUtils.getInstance().isPermissionDenied(this, deniedPermission)) {
                        PermissionUtils.getInstance().checkAndRequestAllPermissions(this, mPermissionCallback);
                        break;
                    }
                }
            }
        }
    }
}
