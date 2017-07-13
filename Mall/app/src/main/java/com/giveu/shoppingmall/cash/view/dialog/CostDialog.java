package com.giveu.shoppingmall.cash.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;


/**
 * Created by 101900 on 2017/7/13.
 */

public class CostDialog {
    private CustomDialog mDialog;
    private Activity mActivity;

    public CostDialog(Activity activity) {
        mActivity = activity;

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dialog_cost, null);
        initView(convertView);
        mDialog = new CustomDialog(mActivity, convertView, R.style.login_error_dialog_Style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
    }

    public void showDialog() {
        mDialog.show();
    }

    public void initView(View convertView){

        ClickEnabledTextView tvEnsure = (ClickEnabledTextView) convertView.findViewById(R.id.tv_ensure);
        ImageView ivCostBg = (ImageView) convertView.findViewById(R.id.iv_cost_bg);

        tvEnsure.setBackgroundResource(R.drawable.selector_login);
        int width = ivCostBg.getWidth();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivCostBg.getLayoutParams();
        layoutParams.height = (638 * width / 700);
        ivCostBg.setLayoutParams(layoutParams);

        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }
}