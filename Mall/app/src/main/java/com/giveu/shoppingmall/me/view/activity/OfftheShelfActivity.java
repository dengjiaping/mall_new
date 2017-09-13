package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.activity.ShoppingClassifyActivity;
import com.giveu.shoppingmall.utils.CommonUtils;

import butterknife.BindView;

/**
 * 商品下架页面
 * Created by 101900 on 2017/9/13.
 */

public class OfftheShelfActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    TextView tvBack;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, OfftheShelfActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_off_the_shelf);
        baseLayout.setTitle("商品过期不存在");
    }

    @Override
    public void setListener() {
        super.setListener();
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtils.startActivity(mBaseContext, ShoppingClassifyActivity.class);
                finish();
            }
        });
    }

    @Override
    public void setData() {

    }

}
