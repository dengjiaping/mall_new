package com.giveu.shoppingmall.me.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.index.view.activity.WalletActivationFirstActivity;
import com.giveu.shoppingmall.utils.CommonUtils;


/**
 * 钱包未激活的弹窗
 */
public class NotActiveDialog {
    private CustomDialog mDialog;
    Activity mActivity;
    TextView tvLeft, tvRight;

    public NotActiveDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_not_active, null);
        initView(contentView);
        setListener();
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
    }

    public void showDialog() {
        mDialog.show();
    }

    private void initView(View contentView) {

        tvLeft = (TextView) contentView.findViewById(R.id.tv_left);
        tvRight = (TextView) contentView.findViewById(R.id.tv_right);

    }



    public void setListener() {

        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mDialog.dismiss();
            }
        });

        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CommonUtils.isFastDoubleClick(R.id.tv_right_pwd)) {//防止重复点击
                    return;
                }
                WalletActivationFirstActivity.startIt(mActivity);
                mDialog.dismiss();
            }
        });
    }

    //监听dialog的关闭
    public NotActiveDialog setdismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDialog.setOnDismissListener(dismissListener);
        return this;
    }
}
