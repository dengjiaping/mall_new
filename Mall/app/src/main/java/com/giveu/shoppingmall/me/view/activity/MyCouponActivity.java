package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CouponAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CouponListResponse;
import com.giveu.shoppingmall.model.bean.response.CouponBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;

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
    List<CouponBean> couponList;

    public static void startIt(Activity activity) {

        Intent intent = new Intent(activity, MyCouponActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_coupon);
        baseLayout.setTitle("我的优惠");
        Toast.makeText(mBaseContext, LoginHelper.getInstance().getIdPerson(), Toast.LENGTH_LONG).show();
        ApiImpl.receiveCoupon("1111", "2222", new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {

            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });

    }

    @Override
    public void setData() {
        couponList = new ArrayList<>();
        ApiImpl.getCouponList(LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<CouponListResponse>() {
            @Override
            public void onSuccess(CouponListResponse response) {
                //Toast.makeText(mBaseContext, response.data.size(), Toast.LENGTH_LONG).show();
                if (CommonUtils.isNotNullOrEmpty(response.data)) {
//                    for (response.data)
                }
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
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
