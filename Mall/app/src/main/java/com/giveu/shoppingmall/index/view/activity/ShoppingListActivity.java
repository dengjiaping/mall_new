package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.fragment.TitleBarFragment;
import com.giveu.shoppingmall.index.view.fragment.ShoppingListFragment;
import com.giveu.shoppingmall.utils.StringUtils;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingListActivity extends BaseActivity {

    private ShoppingListFragment contentFragment;
    private TitleBarFragment titleFragment;
    private int shopTypeId;
    private String code;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_list);

        baseLayout.setTitleBarAndStatusBar(false, true);
        baseLayout.setTopBarBackgroundColor(R.color.white);
        shopTypeId = getIntent().getIntExtra("shopTypeId", 0);
        code = getIntent().getStringExtra("code");
        titleFragment = TitleBarFragment.newInstance(null);

        contentFragment = ShoppingListFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_shopping_list_content, contentFragment)
                .replace(R.id.activity_shopping_list_title, titleFragment)
                .commit();

        //首页过来的商品列表是需要code的
        if (StringUtils.isNotNull(code)) {
            contentFragment.searchByCode(code);
        }else {
            contentFragment.searchByShopTypeId(shopTypeId);
        }
    }

    @Override
    public void setData() {
    }


    //从主页过来,需要传入code
    public static void startIt(Context context, String code) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        intent.putExtra("code", code);
        context.startActivity(intent);
    }

    //从商品类目页传过来的,需要传入shopTypeId
    public static void startIt(Context context, int shopTypeId) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        intent.putExtra("shopTypeId", shopTypeId);
        context.startActivity(intent);
    }
}
