package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

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
                    if (errorBean != null) {
                        ToastUtils.showShortToast(errorBean.message);
                    }
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(originalStr);
                    if ("success".equals(jsonObject.getString("status"))) {
                        Gson gson = new Gson();
                        CouponListResponse response = gson.fromJson(originalStr, CouponListResponse.class);
                        if (CommonUtils.isNotNullOrEmpty(response.data)) {
                            sortByStatus(response.data);
                            couponAdapter.notifyDataSetChanged();
                        } else {
                            baseLayout.ll_baselayout_content.setVisibility(View.GONE);
                            baseLayout.showEmpty("您暂时还没有优惠\n" +
                                    "敬请关注即有钱包其他活动");
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


    //接口返回的优惠券是乱序的，需根据status进行排序
    private void sortByStatus(ArrayList<CouponListResponse> mDatas) {
        int index = -1;
        //使用TreeMap进行排序
        TreeMap<String, CouponListResponse> map = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.equals(o2)) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        for (CouponListResponse response : mDatas) {
            map.put(response.status, response);
        }
        for (Map.Entry<String, CouponListResponse> entry : map.entrySet()) {
            CouponListResponse response = entry.getValue();
            if (("2".equals(response.status) || "3".equals(response.status)) && index == -1) {
                CouponListResponse dividerResponse = new CouponListResponse();
                dividerResponse.isDivider = true;
                couponList.add(dividerResponse);
                index = 0;
            }
            couponList.add(response);
        }
    }

    @Override
    public void setListener() {
        super.setListener();
    }
}
