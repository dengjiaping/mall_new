package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CouponAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CouponListResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 我的优惠券主页
 * Created by 101912 on 2017/8/15.
 */

public class MyCouponActivity extends BaseActivity {

    @BindView(R.id.lv_my_coupon)
    ListView lvMyCoupon;
    CouponAdapter couponAdapter;
    ArrayList<CouponListResponse> couponList;
    int index = -1;


    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, MyCouponActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_my_coupon);
        baseLayout.setTitle("我的优惠");
        couponList = new ArrayList<>();
        initAdapter();
    }

    @Override
    public void setData() {
        ApiImpl.getCouponList(mBaseContext, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<CouponListResponse>() {
            @Override
            public void onSuccess(CouponListResponse response) {
            }

            @Override
            public void onError(BaseBean errorBean) {
                String originalStr = errorBean == null ? "" : errorBean.originResultString;
                //后台返回空字符串，直接返回
                if (StringUtils.isNull(originalStr)) {
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        Gson gson = new Gson();
                        CouponListResponse response = gson.fromJson(originalStr, CouponListResponse.class);
                        if (CommonUtils.isNotNullOrEmpty(response.data)) {
                            for (int i = 0; i < response.data.size(); i++) {
                                CouponListResponse couponResponse = response.data.get(i);
                                if (("2".equals(couponResponse.status) || "3".equals(couponResponse.status) && index == -1)) {
                                    CouponListResponse divderResponse = new CouponListResponse();
                                    divderResponse.isDivider = true;
                                    couponList.add(divderResponse);
                                    couponList.add(couponResponse);
                                    index = i;
                                } else {
                                    couponList.add(couponResponse);
                                }
                            }
                            couponAdapter.notifyDataSetChanged();
                        }else {
                            baseLayout.showEmpty("暂无优惠券");
                        }
                    } else {
                        CommonLoadingView.showErrorToast(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
