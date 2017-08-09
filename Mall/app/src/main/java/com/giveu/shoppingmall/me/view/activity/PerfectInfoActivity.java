package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.activity.PerfectContactsActivity;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;

import butterknife.OnClick;

/**
 * Created by 513419 on 2017/8/9.
 * 完善个人资料
 */

public class PerfectInfoActivity extends BaseActivity {

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, PerfectInfoActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfect_info);
        baseLayout.setTitle("完善个人资料");
    }

    @Override
    public void setData() {

    }

    @OnClick({R.id.ll_contact, R.id.ll_live_address})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_contact:
                if (LoginHelper.getInstance().hasExistOther()) {
                    //添加过,查看联系人
                    ShowContactsActivity.startIt(mBaseContext);
                } else {
                    //没有添加过，添加联系人
                    PerfectContactsActivity.startIt(mBaseContext, Const.PERSONCENTER);
                }

                break;

            case R.id.ll_live_address:
                LivingAddressActivity.startIt(mBaseContext);
                break;
        }
    }
}
