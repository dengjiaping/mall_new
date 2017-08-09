package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;


/**
 * 显示联系人
 * Created by 101900 on 2017/8/9.
 */

public class ShowContactsActivity extends BaseActivity {

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, ShowContactsActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_contacts);
        baseLayout.setTitle("我的联系人");
    }

    @Override
    public void setData() {

    }
}
