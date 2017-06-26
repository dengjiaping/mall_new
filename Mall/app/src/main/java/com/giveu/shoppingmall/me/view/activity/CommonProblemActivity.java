package com.giveu.shoppingmall.me.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CommontProblemResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 常见问题页面
 * Created by 101900 on 2017/6/23.
 */

public class CommonProblemActivity extends BaseActivity {
    LvCommonAdapter<CommontProblemResponse.ListBean> commonAdapter;
    @BindView(R.id.lv_problem_list)
    ListView lvProblemList;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_common_problem);
        baseLayout.setTitle("常见问题");
    }

    @Override
    public void setListener() {
        super.setListener();
        lvProblemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!commonAdapter.getItem(i).clickFlag) {
                    commonAdapter.getItem(i).clickFlag = true;
                } else {
                    commonAdapter.getItem(i).clickFlag = false;
                }
                commonAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void setData() {

        CommontProblemResponse problemResponse = new CommontProblemResponse();
        problemResponse.list = new ArrayList<>();
        CommontProblemResponse.ListBean b1 = new CommontProblemResponse.ListBean("1", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b2 = new CommontProblemResponse.ListBean("1", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b3 = new CommontProblemResponse.ListBean("1", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b4 = new CommontProblemResponse.ListBean("1", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b5 = new CommontProblemResponse.ListBean("2", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b6 = new CommontProblemResponse.ListBean("2", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b7 = new CommontProblemResponse.ListBean("2", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b8 = new CommontProblemResponse.ListBean("2", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        CommontProblemResponse.ListBean b9 = new CommontProblemResponse.ListBean("2", "忘记密码", "服务。经过2年的发展，即有分期业务已覆盖全国近200座城市，合作商户超过4万家，累计服务了超过300万的用户，并荣获2016年金贝奖“最具价值消费金融品牌”。");
        problemResponse.list.add(b1);
        problemResponse.list.add(b2);
        problemResponse.list.add(b3);
        problemResponse.list.add(b4);
        problemResponse.list.add(b5);
        problemResponse.list.add(b6);
        problemResponse.list.add(b7);
        problemResponse.list.add(b8);
        problemResponse.list.add(b9);

        commonAdapter = new LvCommonAdapter<CommontProblemResponse.ListBean>(mBaseContext, R.layout.common_problem_item, problemResponse.list) {


            @Override
            protected void convert(ViewHolder viewHolder, CommontProblemResponse.ListBean item, int position) {
                TextView tv_title = viewHolder.getView(R.id.tv_title);
                LinearLayout ll_answer = viewHolder.getView(R.id.ll_answer);
                ImageView iv_arrow = viewHolder.getView(R.id.iv_arrow);

                if(item.problemFlag.equals("1")){
                    tv_title.setText("1.账单问题");

                }else{
                    tv_title.setVisibility(View.VISIBLE);
                    tv_title.setText("2.还款问题");
                }
              if(position == 0 || position == 4){
                  tv_title.setVisibility(View.VISIBLE);
              }else{
                  tv_title.setVisibility(View.GONE);
              }

                if (!item.clickFlag) {
                    ll_answer.setVisibility(View.GONE);
                    iv_arrow.setImageResource(R.drawable.ic_right);
                } else {
                    ll_answer.setVisibility(View.VISIBLE);
                    iv_arrow.setImageResource(R.drawable.ic_down);

                }

            }
        };
        lvProblemList.setAdapter(commonAdapter);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
