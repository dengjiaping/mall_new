package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.PaymentTypeListResponse;

import java.util.List;

/**
 * 支付方式弹窗
 * Created by 101900 on 2017/6/28.
 */

public class PaymentTypeDialog {
    private CustomDialog mDialog;
    Activity mActivity;
    LvCommonAdapter<PaymentTypeListResponse.ListBean> paymentStyleAdapter;
    ListView lv_payment_style;
    ImageView iv_back;
    public String paymentType;//支付类型
    List<PaymentTypeListResponse.ListBean> list;

    public PaymentTypeDialog(Activity mActivity, List<PaymentTypeListResponse.ListBean> list, String paymentType) {
        this.mActivity = mActivity;
        this.list = list;
        this.paymentType = paymentType;
    }

    public void showDialog() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.dialog_payment_style, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        initView(contentView);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(View contentView) {
        lv_payment_style = (ListView) contentView.findViewById(R.id.lv_payment_style);

        iv_back = (ImageView) contentView.findViewById(R.id.iv_back);

        paymentStyleAdapter = new LvCommonAdapter<PaymentTypeListResponse.ListBean>(mActivity, R.layout.lv_payment_type_item, list) {
            @Override
            protected void convert(ViewHolder viewHolder, final PaymentTypeListResponse.ListBean item, final int position) {
                ImageView iv_payment_icon = viewHolder.getView(R.id.iv_payment_icon);
                TextView tv_payment_type = viewHolder.getView(R.id.tv_payment_type);
                final CheckBox cb_check = viewHolder.getView(R.id.cb_check);
                iv_payment_icon.setImageResource(item.Icon);
                tv_payment_type.setText(item.typeName);
                if (item.isChecked) {
                    cb_check.setChecked(true);
                } else {
                    cb_check.setChecked(false);
                }
                cb_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                paymentStyleAdapter.getItem(position).isChecked = true;
                                paymentType = item.typeName;
                            } else {
                                paymentStyleAdapter.getItem(i).isChecked = false;
                            }
                        }
                        paymentStyleAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
                viewHolder.getView(R.id.ll_pay_type).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i < list.size(); i++) {
                            if (i == position) {
                                paymentStyleAdapter.getItem(position).isChecked = true;
                                paymentType = item.typeName;
                            } else {
                                paymentStyleAdapter.getItem(i).isChecked = false;
                            }
                        }
                        paymentStyleAdapter.notifyDataSetChanged();
                        mDialog.dismiss();
                    }
                });
            }
        };
        setListener();
        lv_payment_style.setAdapter(paymentStyleAdapter);
//        lv_payment_style.setSelection(0);
//        paymentStyleAdapter.getItem(0).isChecked = true;
    }

    public PaymentTypeDialog setdismissListener(DialogInterface.OnDismissListener dismissListener) {
        mDialog.setOnDismissListener(dismissListener);
        return this;
    }

    private void setListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
    }
}
