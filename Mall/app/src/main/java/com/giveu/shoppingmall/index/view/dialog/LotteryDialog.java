package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;

/**
 * Created by 513419 on 2017/8/29.
 */

public class LotteryDialog {
    private CustomDialog mDialog;
    private Activity mActivity;
    private ImageView ivJoin;
    private ImageView ivClose;

    public LotteryDialog(final Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_lottery, null);
        ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
        ivJoin = (ImageView) convertView.findViewById(R.id.iv_join);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, true);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        if (window != null) {
            window.setWindowAnimations(R.style.dialogWindowCouponAnim); //设置窗口弹出动画
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.cancel();
                }
                dismissDialog();
            }
        });

        ivJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.join();
                }
            }
        });
    }

    public void showDialog() {
        if (mActivity != null && !mActivity.isFinishing()) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (mActivity != null && !mActivity.isFinishing() && mDialog != null) {
            mDialog.dismiss();
        }
    }

    private OnJoinListener listener;

    public void setOnJoinLitener(OnJoinListener listener) {
        this.listener = listener;
    }

    public interface OnJoinListener {
        void join();
        void cancel();
    }
}
