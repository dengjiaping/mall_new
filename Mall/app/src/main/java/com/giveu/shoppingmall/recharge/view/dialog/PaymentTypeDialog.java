package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.List;

/**
 * 支付方式弹窗
 * Created by 101900 on 2017/6/28.
 */

public class PaymentTypeDialog {
    private CustomDialog mDialog;
    Activity mActivity;
    LvCommonAdapter<PaymentTypeListResponse.ListBean> paymentTypeAdapter;
    ListView lv_payment_type;
    ImageView iv_back;
    public String paymentType;//支付类型
    List<PaymentTypeListResponse.ListBean> paymentTypeList;

    public PaymentTypeDialog(Activity mActivity, String paymentType) {
        this.mActivity = mActivity;
        this.paymentType = paymentType;

        paymentTypeList = new ArrayList<>();
        PaymentTypeListResponse.ListBean bean1 = new PaymentTypeListResponse.ListBean("即有钱包", R.drawable.ic_wallet, true);
        PaymentTypeListResponse.ListBean bean2 = new PaymentTypeListResponse.ListBean("微信支付", R.drawable.ic_wechat, false);
        PaymentTypeListResponse.ListBean bean3 = new PaymentTypeListResponse.ListBean("支付宝", R.drawable.ic_zhifubao, false);
        paymentTypeList.add(bean1);
        paymentTypeList.add(bean2);
        paymentTypeList.add(bean3);
    }

    public void showDialog() {
        View contentView = View.inflate(mActivity, R.layout.dialog_payment_type, null);
        mDialog = new CustomDialog(mActivity, contentView, R.style.login_error_dialog_Style, Gravity.BOTTOM, true);
        initView(contentView);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(View contentView) {
        lv_payment_type = (ListView) contentView.findViewById(R.id.lv_payment_type);
        iv_back = (ImageView) contentView.findViewById(R.id.iv_back);
        paymentTypeAdapter = new LvCommonAdapter<PaymentTypeListResponse.ListBean>(mActivity, R.layout.lv_payment_type_item, paymentTypeList) {
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
                        onChoosePaymentType(position, paymentTypeAdapter);
                    }
                });

                viewHolder.getView(R.id.ll_pay_type).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onChoosePaymentType(position, paymentTypeAdapter);
                    }
                });
            }
        };
        setListener();
        lv_payment_type.setAdapter(paymentTypeAdapter);
    }

    /**
     * 支付弹窗关闭的监听
     *
     * @param dismissListener
     * @return
     */
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

    /**
     * 点击改变选中的支付类型，获取的支付类型名称，并关闭支付弹窗
     *
     * @param position           选中项
     * @param paymentTypeAdapter 支付方式adapter
     */
    public void onChoosePaymentType(int position, LvCommonAdapter<PaymentTypeListResponse.ListBean> paymentTypeAdapter) {
        for (int i = 0; i < paymentTypeList.size(); i++) {
            PaymentTypeListResponse.ListBean item = paymentTypeAdapter.getItem(i);
            if (i == position) {
                item.isChecked = true;
                paymentType = item.typeName;
            } else {
                item.isChecked = false;
            }
        }
        if (listener != null) {
            listener.onChooseType(position);
        }
        paymentTypeAdapter.notifyDataSetChanged();
        mDialog.dismiss();
    }

    public interface OnChoosePayTypeListener {
        //选中的支付方式，0为钱包，1为微信，2为支付宝
        void onChooseType(int type);
    }

    private OnChoosePayTypeListener listener;

    public void setOnChoosePayTypeListener(OnChoosePayTypeListener listener) {
        this.listener = listener;
    }
}
