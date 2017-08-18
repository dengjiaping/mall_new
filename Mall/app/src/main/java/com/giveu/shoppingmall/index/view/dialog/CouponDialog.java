package com.giveu.shoppingmall.index.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;


/**
 * Created by 101900 on 2017/3/6.
 */

public class CouponDialog {
    private CustomDialog mDialog;
    private Activity mActivity;

    public CouponDialog(Activity mActivity) {
        this.mActivity = mActivity;
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.activity_receive_coupon, null);
        ImageView ivClose = (ImageView) convertView.findViewById(R.id.iv_close);
        ImageView ivReceive = (ImageView) convertView.findViewById(R.id.iv_receive);
        initView(convertView);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, true);
        mDialog.setCancelable(false);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
        ivReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
                //调接口
            }
        });
    }

    public void showDialog() {
        mDialog.show();
    }

    public void initView(View view){

    }
}
