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
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.explosionfield.Utils;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingSearchActivity extends BaseActivity {

    private String[] labels = {"iphone 7 plus", "vivo", "华为", "oppo r9", "笔记本电脑", "智能", "电源", "耳机", "华为荣耀", "红米note4", "生活", "小米6", "其他", "其他", "其他"};
    @BindView(R.id.shopping_search_flowlayout)
    LabelsFlowLayout mFlowLayout;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_search_layout);

        baseLayout.showCenterEditText();
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
                    ToastUtils.showShortToast(str);
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
