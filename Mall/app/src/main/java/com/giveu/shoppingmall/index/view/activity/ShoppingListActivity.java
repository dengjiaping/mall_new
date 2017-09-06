package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.adapter.ShopListItemAdapter;
import com.giveu.shoppingmall.utils.explosionfield.Utils;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingListActivity extends BaseActivity {

    @BindView(R.id.shopping_list_listview)
    PullToRefreshListView mRefreshView;
    @BindView(R.id.shopping_list_radiogroup)
    RadioGroup mRadioGroup;
    @BindView(R.id.shopping_list_radio_all)
    RadioButton rbAll;
    @BindView(R.id.shopping_list_radio_sale)
    RadioButton rbSale;
    @BindView(R.id.shopping_list_radio_price)
    RadioButton rbPrice;

    private List<String> shoppingList;
    private ShopListItemAdapter itemAdapter;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_list);

        baseLayout.setTitle("搜索")
                .setTitleTextBackground(R.drawable.shape_title_background)
                .setTitleWidth(Utils.dp2Px(260))
                .setTitleHeight(Utils.dp2Px(30))
                .setTitleTextSize(15)
                .setTitleTextColor(R.color.color_textcolor)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingSearchActivity.startIt(mBaseContext);
                    }
                });


        shoppingList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            shoppingList.add("i");
        }

        itemAdapter = new ShopListItemAdapter(mBaseContext, R.layout.adapter_shopping_list_item, shoppingList);
        mRefreshView.setAdapter(itemAdapter);
        mRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.shopping_list_radio_all:
                        break;
                    case R.id.shopping_list_radio_sale:
                        break;
                    case R.id.shopping_list_radio_price:
                        break;
                    default:
                        break;
                }
            }
        });

        rbAll.setChecked(true);
        rbPrice.setTag(true);
        rbPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbPrice.isChecked()) {
                    boolean isUp = (boolean) rbPrice.getTag();
                    Drawable drawable;
                    if (isUp) {
                        drawable = getResources().getDrawable(R.drawable.sort_down);
                    } else {
                        drawable = getResources().getDrawable(R.drawable.sort_up);
                    }
                    Rect bounds = rbPrice.getCompoundDrawables()[0].getBounds();
                    drawable.setBounds(bounds);
                    rbPrice.setCompoundDrawables(drawable, null, null, null);
                    rbPrice.setTag(!isUp);
                }
            }
        });
    }

    @Override
    public void setData() {
    }


    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingListActivity.class);
        context.startActivity(intent);
    }
}
