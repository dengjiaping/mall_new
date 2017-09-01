package com.giveu.shoppingmall.me.view.activity;


import android.content.Context;
import android.content.Intent;

import com.giveu.shoppingmall.base.web.BaseWebViewActivity;
import com.giveu.shoppingmall.utils.StringUtils;


/**
 * 跳转的网页页面
 * Created by 101900 on 2016/12/29.
 */

public class CustomWebViewActivity extends BaseWebViewActivity {
    public static final String customWebUrlKey = "customWebUrlKey";
    public static final String customWebTitleKey = "customWebTitleKey";
    public static final String needCloseImage = "needCloseImage";


    @Override
    public void afterInitView() {
        boolean needColse = getIntent().getBooleanExtra(needCloseImage, false);
        //是否需要关闭网页的按钮
        if (needColse) {
            showCloseImage();
        }
    }

    @Override
    public String getWebviewUrl() {
        return getIntent().getStringExtra(customWebUrlKey);
    }

    @Override
    public String getActivityTitle() {
        Intent intent = getIntent();
        String str = intent.getStringExtra(customWebTitleKey);
        return str;
    }

    public static void startIt(Context context, String url, String title) {
        startIt(context, url, title, false);
    }

    public static void startIt(Context context, String url, String title, boolean needClose) {
        Intent intent = new Intent(context, CustomWebViewActivity.class);
        intent.putExtra(customWebUrlKey, url);
        intent.putExtra(needCloseImage, needClose);
        if (StringUtils.isNotNull(title)) {
            intent.putExtra(customWebTitleKey, title);
        }
        context.startActivity(intent);
    }
}