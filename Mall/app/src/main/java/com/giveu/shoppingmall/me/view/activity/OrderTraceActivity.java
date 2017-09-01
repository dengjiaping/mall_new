package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;

import java.util.ArrayList;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderTraceActivity extends BaseActivity {

    private ArrayList<String> traceDatas;
    private LvCommonAdapter<String> adapter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, OrderTraceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_trace);
        baseLayout.setTitle("订单跟踪");
    }

    @Override
    public void setData() {
        traceDatas = new ArrayList<>();
        adapter = new LvCommonAdapter<String>(mBaseContext, R.layout.lv_trace_item, traceDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {

            }
        };
    }
}
