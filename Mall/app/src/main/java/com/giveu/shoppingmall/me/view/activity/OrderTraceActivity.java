package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.me.relative.OrderState;
import com.giveu.shoppingmall.me.relative.OrderStatus;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.OrderTraceResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 101912 on 2017/8/29.
 */

public class OrderTraceActivity extends BaseActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_status)
    TextView tvStatus;
    @BindView(R.id.tv_orderNo)
    TextView tvOrderNo;
    @BindView(R.id.lv_trace)
    ListView lvTrace;
    @BindView(R.id.clv_empty)
    CommonLoadingView clvEmpty;
    @BindView(R.id.ll_listView)
    LinearLayout llListView;

    private ArrayList<OrderTraceResponse.LogisticsInfoBean> traceDatas;
    private LvCommonAdapter<OrderTraceResponse.LogisticsInfoBean> adapter;
    private String orderNo = "";
    private String src = "";

    public static void startIt(Activity activity, String orderNo, String src) {
        Intent intent = new Intent(activity, OrderTraceActivity.class);
        intent.putExtra("orderNo", orderNo);
        intent.putExtra("src", src);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_order_trace);
        baseLayout.setTitle("订单跟踪");
        baseLayout.ll_baselayout_content.setVisibility(View.GONE);
        traceDatas = new ArrayList<>();
        adapter = new LvCommonAdapter<OrderTraceResponse.LogisticsInfoBean>(mBaseContext, R.layout.lv_trace_item, traceDatas) {
            @Override
            protected void convert(ViewHolder viewHolder, OrderTraceResponse.LogisticsInfoBean item, int position) {
                viewHolder.setText(R.id.tv_content, item.content);
                viewHolder.setText(R.id.tv_time, item.msgTime);
                if (position == 0) {
                    viewHolder.setInvisible(R.id.view_up_line);
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
        lvTrace.setAdapter(adapter);
    }

    @Override
    public void setData() {
        orderNo = getIntent().getStringExtra("orderNo");
        src = getIntent().getStringExtra("src");
        ApiImpl.getOrderTrace(mBaseContext, OrderState.CHANNEL, "10056737", orderNo, src, new BaseRequestAgent.ResponseListener<OrderTraceResponse>() {
            @Override
            public void onSuccess(OrderTraceResponse response) {
                if (CommonUtils.isNotNullOrEmpty(response.data.logisticsInfo)) {
                    clvEmpty.setVisibility(View.GONE);
                    llListView.setVisibility(View.VISIBLE);
                    traceDatas.addAll(onTurnByTime(response.data.logisticsInfo));
                    adapter.notifyDataSetChanged();
                } else {
                    clvEmpty.setVisibility(View.VISIBLE);
                    llListView.setVisibility(View.GONE);
                    clvEmpty.showEmpty("抱歉，没有任何物流信息", "");
                }
                ImageUtils.loadImage(src, ivIcon);
                tvStatus.setText(OrderStatus.getOrderStatus(response.data.status));
                if (StringUtils.isNotNull(response.data.orderNo)) {
                    tvOrderNo.setText("订单编号：" + response.data.orderNo);
                }
                baseLayout.ll_baselayout_content.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(BaseBean errorBean) {
                baseLayout.disLoading();
                baseLayout.showError(errorBean);
            }
        });
    }


    /**
     * 默认返回的物流信息是以时间为正序的
     * 此方法取反序
     */
    private ArrayList<OrderTraceResponse.LogisticsInfoBean> onTurnByTime(List<OrderTraceResponse.LogisticsInfoBean> datas) {
        ArrayList<OrderTraceResponse.LogisticsInfoBean> mDatas = new ArrayList<>();
        for (int i = datas.size() - 1; i >= 0; i--) {
            mDatas.add(datas.get(i));
        }
        return mDatas;
    }
}
