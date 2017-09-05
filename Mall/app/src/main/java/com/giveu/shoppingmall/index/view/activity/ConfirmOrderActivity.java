package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.dialog.ChooseCouponDialog;
import com.giveu.shoppingmall.index.widget.StableEditText;
import com.giveu.shoppingmall.recharge.view.dialog.PaymentTypeDialog;
import com.giveu.shoppingmall.widget.DetailView;
import com.giveu.shoppingmall.widget.dialog.CustomListDialog;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 101900 on 2017/9/1.
 */

public class ConfirmOrderActivity extends BaseActivity {
    //选择付款方式
    @BindView(R.id.confirm_order_pay_type)
    DetailView dvPayView;
    //买家留言
    @BindView(R.id.confirm_order_msg_edit)
    StableEditText msgEditText;
    //优惠券
    @BindView(R.id.confirm_order_coupon)
    DetailView dvCouponView;
    //首付
    @BindView(R.id.confirm_order_first_pay)
    DetailView dvFirstPayView;
    //分期数
    @BindView(R.id.confirm_order_month)
    DetailView dvMonthView;

    private ChooseCouponDialog couponDialog;
    private PaymentTypeDialog paymentTypeDialog;
    private CustomListDialog firstPayDialog;
    private CustomListDialog monthDialog;

    private int payType = 0;
    private int couponType = 0;

    private List<CharSequence> firstPayList;
    private List<CharSequence> monthList;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_confirm_order);
        baseLayout.setTitle("订单信息确认");

        paymentTypeDialog = new PaymentTypeDialog(this);
        paymentTypeDialog.setOnChoosePayTypeListener(new PaymentTypeDialog.OnChoosePayTypeListener() {
            @Override
            public void onChooseType(int type, String paymentTypeStr) {
                dvPayView.setRightText(paymentTypeStr);
                payType = type;
            }
        });

        msgEditText.setStableText("买家留言(选填)：");
        msgEditText.setText("提示信息");

        couponDialog = new ChooseCouponDialog(this, Arrays.asList(new CharSequence[]{"满600减30元全品类", "不使用优惠券"}), new ChooseCouponDialog.OnChooseTypeListener() {
            @Override
            public void onChooseType(int type, String typeMsg) {
                couponType = type;
                dvCouponView.setRightText(typeMsg);
            }
        });

        firstPayDialog = new CustomListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dvFirstPayView.setRightText(firstPayList.get(position).toString());
            }
        });
        firstPayDialog.setData(firstPayList = Arrays.asList(new CharSequence[]{"零首付", "首付5%", "首付15%", "首付25%", "首付35%"}));

        monthDialog = new CustomListDialog(this, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dvMonthView.setRightText(monthList.get(position).toString());
            }
        });
        monthDialog.setData(monthList = Arrays.asList(new CharSequence[]{"3个月", "6个月", "9个月", "12个月", "18个月", "24个月",}));

    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmOrderActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.confirm_order_pay_type, R.id.confirm_order_coupon,
            R.id.confirm_order_first_pay, R.id.confirm_order_month})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.confirm_order_pay_type:
                paymentTypeDialog.showDialog(payType);
                break;
            case R.id.confirm_order_coupon:
                couponDialog.show(couponType);
                break;
            case R.id.confirm_order_first_pay:
                firstPayDialog.show();
                break;
            case R.id.confirm_order_month:
                monthDialog.show();
                break;
        }
    }


}
