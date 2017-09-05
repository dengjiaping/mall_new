package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.adapter.ShopListItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShoppingListActivity extends BaseActivity {

    @BindView(R.id.shopping_list_recyclerview)
    RecyclerView mRecyclerView;
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
                .setTitleWidth(800)
                .setTitleHeight(100)
                .setTitleTextSize(15)
                .setTitleTextColor(R.color.color_textcolor);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            shoppingList.add("i");
        }

        itemAdapter = new ShopListItemAdapter(mBaseContext, R.layout.adapter_shopping_list_item, shoppingList);
        mRecyclerView.setAdapter(itemAdapter);

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
