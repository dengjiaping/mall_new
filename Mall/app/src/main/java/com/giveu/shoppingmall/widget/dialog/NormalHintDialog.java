package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;




/**
 *  通用的提示弹窗（一行字和确定按钮）
 * Created by 101900 on 2017/2/17.
 */

public class NormalHintDialog {

    private CustomDialog mDialog;
    Activity mActivity;
    String hintText;

    public NormalHintDialog(Activity activity, String hintText) {
        this.mActivity = activity;
        this.hintText = hintText;

    }

    public void showDialog() {

        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View contentView = inflater.inflate(R.layout.dialog_nnormal_hint, null);
        initView(contentView);
        mDialog = new CustomDialog(mActivity, contentView, R.style.date_dialog_style, Gravity.CENTER, false);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(View contentView) {
        TextView tvHintText = (TextView) contentView.findViewById(R.id.tv_hint_text);
        TextView tvEnsure = (TextView) contentView.findViewById(R.id.tv_ensure);
        tvHintText.setText(hintText);
        //确定按钮
        tvEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog.dismiss();
            }
        });
    }
}
