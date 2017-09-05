package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.OrderTraceResponse;

import java.util.ArrayList;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderTraceActivity extends BaseActivity {

    private ArrayList<OrderTraceResponse.LogisticsInfoBean> traceDatas;
    private LvCommonAdapter<OrderTraceResponse.LogisticsInfoBean> adapter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, OrderTraceActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_trace);
        baseLayout.setTitle("订单跟踪");
        traceDatas = new ArrayList<>();
        adapter = new LvCommonAdapter<OrderTraceResponse.LogisticsInfoBean>(mBaseContext, R.layout.lv_trace_item, traceDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, OrderTraceResponse.LogisticsInfoBean item, int position) {
                viewHolder.setText(R.id.tv_content, item.content);
                viewHolder.setText(R.id.tv_time, item.msgTime);
                if (position == traceDatas.size() - 1) {
                    viewHolder.setVisible(R.id.view_up_line, false);
                    if (traceDatas.size() == 1) {
                        viewHolder.setVisible(R.id.view_up_line, true);
                    }
                    viewHolder.setTextColor(R.id.tv_content, getResources().getColor(R.color.color_00bbc0));
                    viewHolder.setTextColor(R.id.tv_time, getResources().getColor(R.color.color_00bbc0));
                    viewHolder.setImageResource(R.id.iv_circle, R.drawable.ic_circle_blue);
                } else {
                    viewHolder.setImageResource(R.id.iv_circle, R.drawable.ic_circle_grey);
                    viewHolder.setTextColor(R.id.tv_content, getResources().getColor(R.color.color_999999));
                    viewHolder.setTextColor(R.id.tv_time, getResources().getColor(R.color.color_9c9c9c));
                    viewHolder.setVisible(R.id.view_up_line, true);
                }
            }
        };
    }

    @Override
    public void setData() {

    }
}
