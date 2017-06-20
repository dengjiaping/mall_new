package com.giveu.shoppingmall.view.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.me.view.activity.CreateGestureActivity;
import com.giveu.shoppingmall.utils.CommonUtils;

/**
 * Created by zhengfeilong on 16/5/16.
 */
public class PatternLockSetTipDialog extends CustomDialog {

    public PatternLockSetTipDialog(Activity context) {
        super(context, R.layout.pattern_lock_set_tip_dialog, R.style.login_error_dialog_Style, Gravity.CENTER, false);

    }

    @Override
    protected void initView(View contentView) {
        TextView tv_set = (TextView) contentView.findViewById(R.id.tv_set);
        TextView tv_not_set = (TextView) contentView.findViewById(R.id.tv_not_set);
        tv_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                CommonUtils.startActivity(mAttachActivity, CreateGestureActivity.class);
            }
        });
        tv_not_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.setCancelable(true);
        this.setCanceledOnTouchOutside(false);
    }


}
