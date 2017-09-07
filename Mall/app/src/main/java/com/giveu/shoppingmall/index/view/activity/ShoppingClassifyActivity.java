package com.giveu.shoppingmall.index.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.rvadapter.RvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.ViewHolder;
import com.giveu.shoppingmall.index.adapter.ShopClassifyContentAdapter;
import com.giveu.shoppingmall.model.bean.response.ShoppingBean;
import com.giveu.shoppingmall.utils.ItemHeaderDecoration;
import com.giveu.shoppingmall.utils.RecyclerViewScrollHelper;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ShopTypesBean;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.explosionfield.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 524202 on 2017/9/1.
 * 商品分类管理界面
 */

public class ShoppingClassifyActivity extends BaseActivity implements ItemHeaderDecoration.CheckListener {
    @BindView(R.id.shopping_classify_left_recycler)
    RecyclerView lRecyclerView;
    @BindView(R.id.shopping_classify_right_recycler)
    RecyclerView rRecyclerView;

    private List<ShoppingBean> list1;
    private List<ShoppingBean> list2;

    private ItemHeaderDecoration mDecoration;
    private RvCommonAdapter titleAdapter;
    private ShopClassifyContentAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private int selectPos = 0;

    private RecyclerViewScrollHelper.RecyclerViewListener listener;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_classify_layout);
        baseLayout.setTitle("搜索")
                .setTitleTextBackground(R.drawable.shape_title_background)
                .setTitleWidth(Utils.dp2Px(260))
                .setTitleHeight(Utils.dp2Px(30))
                .setTitleTextSize(15)
                .setTitleTextColor(R.color.color_textcolor)
                .setTitleListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShoppingSearchActivity.startIt(mBaseContext);
                    }
                });
        baseLayout.setRightImage(R.drawable.classify_msg);

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        lRecyclerView.setAdapter(titleAdapter = new RvCommonAdapter<ShoppingBean>(this, R.layout.adapter_shopping_classify_label_item, list1) {

            @Override
            protected void convert(ViewHolder holder, ShoppingBean s, final int position) {
                if (selectPos == position) {
                    holder.setBackgroundColor(R.id.item_layout, Color.WHITE);
                    holder.setTextColor(R.id.item_label, Color.parseColor("#00bbc0"));
                    holder.setVisible(R.id.item_right_line, true);
                } else {
                    holder.setBackgroundColor(R.id.item_layout, Color.TRANSPARENT);
                    holder.setTextColor(R.id.item_label, Color.parseColor("#191919"));
                    holder.setVisible(R.id.item_right_line, false);
                }

                holder.setText(R.id.item_label, s.getTypesBean().getName());
                holder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //update left recyclerview
                        RecyclerViewScrollHelper.moveToCenter(lRecyclerView, position);
                        selectPos = position;
                        notifyDataSetChanged();

                        //update right recyclerview
                        mDecoration.setCurrentTag(selectPos);
                        int targetPos = calculateTargetPos(position);
                        RecyclerViewScrollHelper.smoothScrollToPosition(rRecyclerView, targetPos);

                        listener.setMove(true);
                        listener.setIndex(targetPos);
                    }
                });
            }
        });


        mDecoration = new ItemHeaderDecoration(list2);
        rRecyclerView.addItemDecoration(mDecoration);
        mDecoration.setCheckListener(this);
        rRecyclerView.setAdapter(mAdapter = new ShopClassifyContentAdapter(this, list2));

        lRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        gridLayoutManager = new GridLayoutManager(this, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (mAdapter.getItemViewType(position) == 0) {
                    return 3;
                }
                return 1;
            }
        });
        rRecyclerView.setLayoutManager(gridLayoutManager);
        listener = new RecyclerViewScrollHelper.RecyclerViewListener();
        rRecyclerView.setOnScrollListener(listener);
    }

    private int calculateTargetPos(int position) {
        int pos = 0;
        for (int i = 0; i < list2.size(); i++) {
            if (list2.get(i).getType() == 0) {
                pos++;
            }
            if (pos == (position + 1)) {
                pos = i;
                break;
            }
        }
        return pos;
    }

    @Override
    public void setData() {
        ApiImpl.getChildrenShopTypes(mBaseContext, 1, new BaseRequestAgent.ResponseListener<ShopTypesBean>() {
                    @Override
                    public void onSuccess(ShopTypesBean response) {
                        List<ShopTypesBean> results = response.data;
                        if (results != null) {
                            list1.clear();
                            list2.clear();
                            int tag = 0;
                            for (ShopTypesBean parent : results) {
                                list1.add(new ShoppingBean(0, tag, parent));
                                list2.add(new ShoppingBean(0, tag, parent));
                                if (parent.getChild() != null) {
                                    for (ShopTypesBean child : parent.getChild()) {
                                        list2.add(new ShoppingBean(1, tag, child));
                                    }
                                }
                                tag++;
                            }

                            titleAdapter.notifyDataSetChanged();
                            mAdapter.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        LogUtil.d("Error response = " + errorBean);
                    }
                }

        );
    }

    @Override
    public void check(int position, boolean isScroll) {
        if (listener.isMove()) {
            listener.setMove(false);
        } else {
            selectPos = position;
            titleAdapter.notifyDataSetChanged();
        }
        RecyclerViewScrollHelper.moveToCenter(lRecyclerView, position);
        mDecoration.setCurrentTag(selectPos);
    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingClassifyActivity.class);
        context.startActivity(intent);
    }

}
