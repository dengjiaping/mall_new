package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CouponAdapter;
import com.giveu.shoppingmall.model.bean.response.CouponResponse;
import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 我的优惠券主页
 * Created by 101912 on 2017/8/15.
 */

public class MyCouponActivity extends BaseActivity {

    @BindView(R.id.lv_my_coupon)
    ListView lvMyCoupon;

    CouponAdapter couponAdapter;
    List<CouponResponse> couponList;

    public static void startIt(Activity activity) {

        Intent intent = new Intent(activity, MyCouponActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_coupon);
        baseLayout.setTitle("我的优惠");
    }

    @Override
    public void setData() {
        couponList = new ArrayList<>();
        CouponResponse response = new CouponResponse("kjl", true);
        CouponResponse response1 = new CouponResponse("kjl", true);
        CouponResponse response2 = new CouponResponse("kjl", true);
        CouponResponse response3 = new CouponResponse("kjl", true);
        CouponResponse response5 = new CouponResponse("kjl", false);
        couponList.add(response);
        couponList.add(response1);
        couponList.add(response2);
        couponList.add(response5);
        couponList.add(response3);
//        if (CommonUtils.isNotNullOrEmpty(couponList))
//        Toast.makeText(mBaseContext, couponList.size(), Toast.LENGTH_LONG).show();
            initAdapter();
    }

    private void initAdapter() {
        couponAdapter = new CouponAdapter(mBaseContext, couponList);
        lvMyCoupon.setAdapter(couponAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
    }
}
