package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.Const;

/**
 * 完善个人信息-联系人
 * Created by 101900 on 2017/8/9.
 */

public class PerfectContactsActivity extends BaseActivity {

    public static void startIt(Activity mActivity, String flag) {
        Intent intent = new Intent(mActivity, PerfectContactsActivity.class);
        intent.putExtra("flag",flag);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfect_contacts);
    }

    @Override
    public void setData() {
        String flag = getIntent().getStringExtra("flag");
        switch (flag){
            case Const.CASH:
                //取现跳转过来
                baseLayout.setTitle("请完善资料");
                break;
            case Const.PERSONCENTER:
                //个人中心跳转过来
                baseLayout.setTitle("我的联系人");
                break;
            case Const.RECHARGE:
                //充值跳转过来
                break;
        }
    }
}
