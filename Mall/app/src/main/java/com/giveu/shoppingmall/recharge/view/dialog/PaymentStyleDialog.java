package com.giveu.shoppingmall.recharge.view.dialog;

import android.app.Activity;
import android.content.Context;
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
import com.giveu.shoppingmall.model.bean.response.PaymentStyleListResponse;

import java.util.ArrayList;

/**
 * 支付方式弹窗
 * Created by 101900 on 2017/6/28.
 */

public class PaymentStyleDialog {
    private CustomDialog mDialog;
    Activity mActivity;
    LvCommonAdapter<PaymentStyleListResponse.ListBean> paymentStyleAdapter;
    ListView lv_payment_style;
    public PaymentStyleDialog(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void showDialog() {
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         View contentView = inflater.inflate(R.layout.dialog_payment_style, null);
        mDialog = new CustomDialog(mActivity,contentView,R.style.login_error_dialog_Style, Gravity.BOTTOM,true);
        initView(contentView);
        mDialog.setCancelable(false);
        mDialog.show();
    }

    private void initView(View contentView) {
         lv_payment_style = (ListView) contentView.findViewById(R.id.lv_payment_style);
        final PaymentStyleListResponse paymentStyleListResponse = new PaymentStyleListResponse();
        paymentStyleListResponse.list = new ArrayList<>();
        PaymentStyleListResponse.ListBean bean1 = new PaymentStyleListResponse.ListBean("即有钱包",R.drawable.ic_wechat, false);
        PaymentStyleListResponse.ListBean bean2 = new PaymentStyleListResponse.ListBean("微信支付",R.drawable.ic_wechat,false);
        PaymentStyleListResponse.ListBean bean3 = new PaymentStyleListResponse.ListBean("支付宝支付",R.drawable.ic_zhifubao,false);
        paymentStyleListResponse.list.add(bean1);
        paymentStyleListResponse.list.add(bean2);
        paymentStyleListResponse.list.add(bean3);
        paymentStyleAdapter = new LvCommonAdapter<PaymentStyleListResponse.ListBean>(mActivity,R.layout.lv_payment_style_item,paymentStyleListResponse.list) {
            @Override
            protected void convert(ViewHolder viewHolder, final PaymentStyleListResponse.ListBean item, final int position) {
                ImageView iv_payment_icon = viewHolder.getView(R.id.iv_payment_icon);
                TextView tv_payment_style = viewHolder.getView(R.id.tv_payment_style);
                final CheckBox cb_check = viewHolder.getView(R.id.cb_check);
                iv_payment_icon.setImageResource(item.Icon);
                tv_payment_style.setText(item.styleName);
                if(item.isChecked){
                    cb_check.setChecked(true);
                }else{
                    cb_check.setChecked(false);
                }
                cb_check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i <paymentStyleListResponse.list.size() ; i++) {
                            if(i == position){
                                paymentStyleAdapter.getItem(position).isChecked = true;
                            }else{
                                paymentStyleAdapter.getItem(i).isChecked = false;
                            }
                        }
                        paymentStyleAdapter.notifyDataSetChanged();
                    }
                });
                viewHolder.getView(R.id.ll_pay_type).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (int i = 0; i <paymentStyleListResponse.list.size() ; i++) {
                            if(i == position){
                                paymentStyleAdapter.getItem(position).isChecked = true;
                            }else{
                                paymentStyleAdapter.getItem(i).isChecked = false;
                            }
                        }
                        paymentStyleAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        lv_payment_style.setAdapter(paymentStyleAdapter);
        setListener();
        lv_payment_style.setSelection(0);
    }

    private void setListener(){

/*        lv_payment_style.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checkId, long id) {

                for (int i = 0; i < 3; i++) {
                    if(i == checkId){
                        paymentStyleAdapter.getItem(checkId).isChecked = true;

                    }else{
                        paymentStyleAdapter.getItem(i).isChecked = false;
                    }
                }
                paymentStyleAdapter.notifyDataSetChanged();
//                switch (checkId){
//                    case 0:
//                        //即有钱包支付
//
//                        break;
//                    case 1:
//                        //微信支付
//                        break;
//                    case 2:
//                        //支付宝支付
//                        break;
//                }
            }*/
//        });
    }
}
