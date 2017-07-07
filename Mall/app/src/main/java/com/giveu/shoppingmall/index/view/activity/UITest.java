package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.DebugConfig;

/**
 * Created by 508632 on 2016/12/22.
 */

public class UITest {
    public static void test(Activity mContext) {
        if (!DebugConfig.isDev) {
            return;
        }
      //  startActivity(WalletActivationFirstActivity.class);
    }


    public static void startActivity(Class clzz) {
        Context appContext = BaseApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(appContext, clzz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        appContext.startActivity(intent);
    }


}


