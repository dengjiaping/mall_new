package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
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
    private int shopTypeId;
    private String code;
    private boolean isFromShoppingClassify = false;//是否从商品类目页传过来的

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_list);

        baseLayout.setTitleBarAndStatusBar(false, true);
        baseLayout.setTopBarBackgroundColor(R.color.white);
        shopTypeId = getIntent().getIntExtra("shopTypeId", 1);
//        isFromShoppingClassify = getIntent().getBooleanExtra("isFromShoppingClassify", false);
        code = getIntent().getStringExtra("code");
        titleFragment = TitleBarFragment.newInstance(null);


        contentFragment = ShoppingListFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putInt("shopTypeId", shopTypeId);
        bundle.putString("code", code);
        contentFragment.setArguments(bundle);
        contentFragment.initDataForFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_shopping_list_content, contentFragment)
                .replace(R.id.activity_shopping_list_title, titleFragment)
                .commit();
    }

    @Override
    public void setData() {
    }


    public static void startIt(Context context, String code) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    //从商品类目页传过来的
    public static void startItFromShoppingClassify(Activity activity, int shopTypeId) {
        Intent intent = new Intent(activity, ShoppingListActivity.class);
        intent.putExtra("shopTypeId", shopTypeId);
//        intent.putExtra("isFromShoppingClassify", true);
        activity.startActivity(intent);
    }
}
