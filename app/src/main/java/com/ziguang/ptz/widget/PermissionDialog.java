package com.ziguang.ptz.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ziguang.ptz.R;


/**
 * Created by gongzhixing on 2016/7/19.
 */
public class PermissionDialog extends CustomBaseDialog implements View.OnClickListener {

    private final TextView confirmBtn;
    private final TextView cancelBtn;
    private final TextView describeGetWay;
    private final TextView describeReason;

    public interface OnDialogDismissListener {
        void onConfirm();
        void onCancel();
    }
    private OnDialogDismissListener onDialogDismissListener;

    public PermissionDialog(Context context, String permissionDescribe, String haoToSetText) {
        super(context, Gravity.CENTER, 0.0f, AnimationDirection.VERTICLE);
        setContentView(R.layout.layout_dialig_permission);
        setCanceledOnTouchOutside(false);
        cancelBtn = (TextView) findViewById(R.id.btn_cancel);
        confirmBtn = (TextView) findViewById(R.id.btn_confirm);
        describeGetWay = (TextView) findViewById(R.id.describe_get_way);
        describeReason = (TextView) findViewById(R.id.describe_reason);
        confirmBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        describeReason.setText(permissionDescribe);
        describeGetWay.setText(haoToSetText);

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_cancel:
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onCancel();
                }
                break;
            case R.id.btn_confirm:
                if (onDialogDismissListener != null) {
                    onDialogDismissListener.onConfirm();
                }
                break;
        }
    }

    public OnDialogDismissListener getOnDialogDismissListener() {
        return onDialogDismissListener;
    }

    public void setOnDialogDismissListener(OnDialogDismissListener onDialogDismissListener) {
        this.onDialogDismissListener = onDialogDismissListener;
    }
}
