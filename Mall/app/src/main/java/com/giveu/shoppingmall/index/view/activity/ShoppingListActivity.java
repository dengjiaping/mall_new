package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.fragment.TitleBarFragment;
import com.giveu.shoppingmall.index.view.fragment.ShoppingListFragment;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingListActivity extends BaseActivity {

    private ShoppingListFragment contentFragment;
    private TitleBarFragment titleFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_list);

        baseLayout.setTitleBarAndStatusBar(false, true);
        baseLayout.setTopBarBackgroundColor(R.color.white);

        titleFragment = TitleBarFragment.newInstance(null);

        contentFragment = ShoppingListFragment.newInstance();
        contentFragment.initDataForFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_shopping_list_content, contentFragment)
                .replace(R.id.activity_shopping_list_title, titleFragment)
                .commit();
    }

    @Override
    public void setData() {
    }


    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        context.startActivity(intent);
    }
}
