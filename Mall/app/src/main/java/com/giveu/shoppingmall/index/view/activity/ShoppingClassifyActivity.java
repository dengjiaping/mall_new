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
import com.giveu.shoppingmall.index.view.fragment.TitleBarFragment;
import com.giveu.shoppingmall.model.bean.response.ShopTypesResponse;
import com.giveu.shoppingmall.model.bean.response.ShoppingBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.ItemHeaderDecoration;
import com.giveu.shoppingmall.utils.RecyclerViewScrollHelper;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ShopTypesBean;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.explosionfield.Utils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

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

    private List<ShopTypesResponse> list1;//一级类目
    private List<ShoppingBean> list2;//二级类目
//    private List<>

    private ItemHeaderDecoration mDecoration;
    private RvCommonAdapter titleAdapter;
    private ShopClassifyContentAdapter mAdapter;
    private GridLayoutManager gridLayoutManager;
    private int selectPos = 0;

    private RecyclerViewScrollHelper.RecyclerViewListener listener;
    private TitleBarFragment titleBarFragment;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_shopping_classify_layout);

        baseLayout.setTitleBarAndStatusBar(false, true);
        baseLayout.setTopBarBackgroundColor(R.color.white);

        titleBarFragment = TitleBarFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.shopping_classify_title, titleBarFragment)
                .commit();

        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        lRecyclerView.setAdapter(titleAdapter = new RvCommonAdapter<ShopTypesResponse>(this, R.layout.adapter_shopping_classify_label_item, list1) {

            @Override
            protected void convert(ViewHolder holder, ShopTypesResponse s, final int position) {
                if (selectPos == position) {
                    holder.setBackgroundColor(R.id.item_layout, Color.WHITE);
                    holder.setTextColor(R.id.item_label, Color.parseColor("#00bbc0"));
                    holder.setVisible(R.id.item_right_line, true);
                } else {
                    holder.setBackgroundColor(R.id.item_layout, Color.TRANSPARENT);
                    holder.setTextColor(R.id.item_label, Color.parseColor("#191919"));
                    holder.setVisible(R.id.item_right_line, false);
                }

                holder.setText(R.id.item_label, s.name);
                holder.setOnClickListener(R.id.item_layout, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getChildrenShopTypes(list1.get(position).shopTypeId);
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
        ApiImpl.initGoodsShopTypes(mBaseContext, Const.CHANNEL, new BaseRequestAgent.ResponseListener<ShopTypesResponse>() {
            @Override
            public void onSuccess(ShopTypesResponse response) {
                if (CommonUtils.isNotNullOrEmpty(response.data)) {
                    list1.clear();
                    list1.addAll(response.data);
                    getChildrenShopTypes(list1.get(0).shopTypeId);
                }
                titleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }


    private void getChildrenShopTypes(int shopTypeId) {
        ApiImpl.getChildrenShopTypes(mBaseContext, shopTypeId, new BaseRequestAgent.ResponseListener<ShopTypesBean>() {
                    @Override
                    public void onSuccess(ShopTypesBean response) {
                        if (CommonUtils.isNotNullOrEmpty(response.data)) {
                            List<ShopTypesBean> results = response.data;
                            list2.clear();
                            int tag = 0;
                            for (ShopTypesBean parent : results) {
                                list2.add(new ShoppingBean(0, tag, parent));
                                if (parent.getChild() != null) {
                                    for (ShopTypesBean child : parent.getChild()) {
                                        list2.add(new ShoppingBean(1, tag, child));
                                    }
                                }
                                tag++;
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onError(BaseBean errorBean) {
                        CommonLoadingView.showErrorToast(errorBean);
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
        if (rRecyclerView.canScrollVertically(1) || rRecyclerView.canScrollVertically(-1)) {
            RecyclerViewScrollHelper.moveToCenter(lRecyclerView, position);
            mDecoration.setCurrentTag(selectPos);
        }
    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ShoppingClassifyActivity.class);
        context.startActivity(intent);
    }

}
