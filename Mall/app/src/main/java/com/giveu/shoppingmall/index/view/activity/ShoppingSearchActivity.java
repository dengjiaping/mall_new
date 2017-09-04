package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.widget.LabelsFlowLayout;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingSearchActivity extends BaseActivity {

    private String[] labels = {"手机", "电脑", "电视", "家电", "路由", "智能", "电源", "耳机", "音响", "礼品", "生活", "其他", "其他", "其他", "其他"};
    @BindView(R.id.shopping_search_flowlayout)
    LabelsFlowLayout mFlowLayout;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_search_layout);

        baseLayout.setTitle("搜索商品")
                .setTitleTextBackground(R.drawable.shape_title_background)
                .setTitleWidth(800)
                .setTitleHeight(100)
                .setTitleTextSize(15)
                .setTitleTextColor(R.color.color_textcolor);
        baseLayout.setRightText("搜索");

        initFlowLayout();
    }

    /**
     * 将数据放入流式布局
     */
    private void initFlowLayout() {
        for (int i = 0; i < labels.length; i++) {
            TextView tv = (TextView) LayoutInflater.from(this).inflate(
                    R.layout.search_label_tv, mFlowLayout, false);
            tv.setText(labels[i]);
            final String str = tv.getText().toString();
            //点击事件
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            mFlowLayout.addView(tv);
        }
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingSearchActivity.class);
        context.startActivity(intent);
    }
}
