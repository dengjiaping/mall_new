package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingSearchActivity extends BaseActivity {

    private List<String> labels;
    @BindView(R.id.shopping_search_flowlayout)
    TagFlowLayout mFlowLayout;
    @BindView(R.id.shopping_search_refresh)
    TextView mRefreshView;

    private TagAdapter<String> mTagAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_search_layout);

        baseLayout.showCenterEditText();
        baseLayout.setRightText("搜索");

        labels = new ArrayList<>();
        mFlowLayout.setAdapter(mTagAdapter = new TagAdapter<String>(labels) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(mBaseContext).inflate(R.layout.search_label_tv, parent, false);
                tvTag.setText(s);
                return tvTag;
            }
        });

        refreshHotWorlds();
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingSearchActivity.class);
        context.startActivity(intent);
    }

    @OnClick({R.id.shopping_search_refresh})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.shopping_search_refresh:
                refreshHotWorlds();
                break;
            default:
                break;
        }
    }

    private void refreshHotWorlds() {
        ApiImpl.refreshHotWords(new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                JSONObject jsonObject = JSONObject.parseObject(response.originResultString);
                JSONArray datas = jsonObject.getJSONArray("data");
                if (datas != null) {
                    labels.clear();
                    labels.addAll(datas.toJavaList(String.class));
                    mTagAdapter.notifyDataChanged();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
            }
        });
    }
}
