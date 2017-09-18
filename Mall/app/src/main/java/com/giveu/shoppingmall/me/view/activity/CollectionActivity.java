package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;
import com.giveu.shoppingmall.me.adapter.CollectionAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 我的收藏
 * Created by 101900 on 2017/9/4.
 */

public class CollectionActivity extends BaseActivity {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    CollectionAdapter collectionAdapter;
    boolean rightTextClick = true;//true 编辑 false 取消
    @BindView(R.id.ll_bottom_delete)
    LinearLayout llBottomDelete;
    @BindView(R.id.cb_choose)
    CheckBox cbChoose;
    @BindView(R.id.tv_delete_text)
    TextView tvDeleteText;

    private int pageIndex = 1;
    private final int pageSize = 10;
    String type;//DELETEMORE 全选删除多项; DELETEONE 长按删除某一项
    List<CollectionResponse.ResultListBean> goodsList;
    private static final String DELETEONE = "1";//长按删除某一项
    private static final String DELETEMORE = "2";//全选删除多项
    List<Integer> itemNotClickList;//单选未中项

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CollectionActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        baseLayout.setTitle("我的收藏");
        baseLayout.setRightTextColor(R.color.title_color);
        goodsList = new ArrayList<>();
        baseLayout.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectionAdapter == null || CommonUtils.isNullOrEmpty(collectionAdapter.getData())) {
                    return;
                }
                if (rightTextClick) {
                    baseLayout.setRightText("取消");
                    llBottomDelete.setVisibility(View.VISIBLE);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT - DensityUtils.dip2px(57));
                    ptrlv.setLayoutParams(layoutParams);
                    clearChoose();
                    cbChoose.setChecked(false);
                } else {
                    baseLayout.setRightText("编辑");
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    ptrlv.setLayoutParams(layoutParams);
                    llBottomDelete.setVisibility(View.GONE);
                }
                for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
                    collectionResponse.isShowCb = rightTextClick;
                }
                collectionAdapter.notifyDataSetChanged();
                rightTextClick = !rightTextClick;
            }
        });
        collectionAdapter = new CollectionAdapter(mBaseContext, goodsList, new CollectionAdapter.CbItemCheckListener() {
            @Override
            public void itemClick() {
                itemNotClickList = new ArrayList<>();
                int count = 0;
                if (collectionAdapter == null || CommonUtils.isNullOrEmpty(collectionAdapter.getData())) {
                    return;
                }
                for (int i = 0; i < collectionAdapter.getData().size(); i++) {
                    if (collectionAdapter.getData().get(i) == null) {
                        return;
                    }
                    if (collectionAdapter.getData().get(i).isCheck) {
                        //记录选中项
                        count++;
                    } else {
                        itemNotClickList.add(i);
                    }
                }
                deleteColorAndCanClick(count);
            }
        });
        ptrlv.setAdapter(collectionAdapter);
        deleteColorAndCanClick(0);
        ptrlv.getFooter().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        ptrlv.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
        ptrlv.setPullLoadEnable(false);
        ptrlv.setPullRefreshEnable(false);
    }

    @Override
    public void setListener() {
        super.setListener();
        cbChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbChoose.isChecked()) {
                    //全选
                    for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
                        collectionResponse.isCheck = true;
                    }
                    deleteColorAndCanClick(collectionAdapter.getCount());
                } else {
                    //全不选
                    clearChoose();
                }
                collectionAdapter.notifyDataSetChanged();
            }
        });

        ptrlv.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //PullToRefreshListView从1开始
                showDeleteGoodsDialog(collectionAdapter, position - 1, DELETEONE);
                return true;
            }
        });

        tvDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteGoodsDialog(collectionAdapter, -1, DELETEMORE);
            }
        });
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
            }
        });
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (collectionAdapter != null && position > 0 && position <= collectionAdapter.getCount()) {
                    if (collectionAdapter.getItem(position - 1) != null) {
                        CollectionResponse.ResultListBean item = collectionAdapter.getItem(position - 1);
                        if (item.hasInvalid()) {
                            //失效显示已下架
                            OfftheShelfActivity.startIt(mBaseContext);
                        } else {
                            //跳转商品介绍
                            CommodityDetailActivity.startItForResult(mBaseContext, item.isInstallments == 1, item.skuCode, Const.COLLECTION, true);
                        }
                    }
                }

            }
        });

    }

    @Override
    public void setData() {

        ApiImpl.getCollectionList(mBaseContext, LoginHelper.getInstance().getIdPerson(), pageIndex, pageSize, new BaseRequestAgent.ResponseListener<CollectionResponse>() {
            @Override
            public void onSuccess(CollectionResponse response) {
                if (pageIndex == 1) {
                    ptrlv.onRefreshComplete();
                }
                if (response != null && response.data != null) {
                    CollectionResponse collectionResponse = response.data;

                    if (CommonUtils.isNotNullOrEmpty(collectionResponse.resultList)) {
                        baseLayout.setRightText("编辑");
                        if (pageIndex == 1) {
                            goodsList.clear();
                            if (collectionResponse.resultList.size() >= pageSize) {
                                ptrlv.setPullLoadEnable(true);
                            } else {
                                ptrlv.setPullLoadEnable(false);
                                ptrlv.showEnd("没有更多数据");
                            }
                            baseLayout.hideEmpty();
                        }
                        goodsList.addAll(collectionResponse.resultList);
                        if (llBottomDelete.getVisibility() == View.VISIBLE) {
                            if (cbChoose.isChecked()) {
                                for (int i = 0; i < goodsList.size(); i++) {
                                    goodsList.get(i).isShowCb = true;
                                    if (itemNotClickList.contains(i)) {
                                        goodsList.get(i).isCheck = false;
                                    } else {
                                        goodsList.get(i).isCheck = true;
                                    }
                                }
                            } else {
                                for (CollectionResponse.ResultListBean collectionResponse1 : goodsList) {
                                    collectionResponse1.isShowCb = true;
                                    collectionResponse1.isCheck = false;
                                }
                            }

                        }
                        collectionAdapter.notifyDataSetChanged();
                        pageIndex++;
                    } else {
                        baseLayout.setRightText("");
                        if (pageIndex == 1) {
                            goodsList.clear();
                            collectionAdapter.notifyDataSetChanged();
                            baseLayout.showEmpty("暂无收藏");
                            ptrlv.setPullLoadEnable(false);
                            baseLayout.setRightText("");
                        } else {
                            ptrlv.setPullLoadEnable(false);
                            ptrlv.showEnd("没有更多数据");
                        }
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                if (pageIndex == 1) {
                    baseLayout.showEmpty("暂无收藏");
                    ptrlv.onRefreshComplete();
                }
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    /**
     * 清除全选
     */
    public void clearChoose() {
        if (collectionAdapter == null) {
            return;
        }
        for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
            collectionResponse.isCheck = false;
        }
        collectionAdapter.notifyDataSetChanged();
        deleteColorAndCanClick(0);
    }

    /**
     * 设置删除按钮颜色和是否可以点击
     *
     * @param count
     */
    public void deleteColorAndCanClick(int count) {
        if (0 == count) {//数目为0，不可点击置灰
            tvDeleteText.setText("删除(0)");
            tvDeleteText.setBackgroundColor(getResources().getColor(R.color.color_edittext));
            tvDeleteText.setEnabled(false);
        } else {
            tvDeleteText.setText("删除(" + count + ")");
            tvDeleteText.setBackgroundColor(getResources().getColor(R.color.title_color));
            tvDeleteText.setEnabled(true);
        }
    }

    /**
     * 显示删除收藏商品Dialog
     *
     * @param collectionAdapter
     * @param position
     * @param type
     */
    public void showDeleteGoodsDialog(final CollectionAdapter collectionAdapter, final int position, final String type) {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogModeOneHint("是否要删除该收藏商品？", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectionAdapter != null && !CommonUtils.isNullOrEmpty(collectionAdapter.getData())) {
                    //满足条件
                    List<String> skuCodes = new ArrayList<>();
                    if (DELETEONE.equals(type)) {
                        if (position >= 0 && position < collectionAdapter.getCount() && (collectionAdapter.getItem(position) != null)) {
                            //长按删除
                            skuCodes.add(collectionAdapter.getItem(position).skuCode);
                            deleteGoods(skuCodes, position, null,type);
                        }
                    } else {
                        //全选或选择删除
                        List<CollectionResponse.ResultListBean> list = collectionAdapter.getData();
                        List<CollectionResponse.ResultListBean> removeList = new ArrayList<>();
                        for (int i = 0; i < collectionAdapter.getData().size(); i++) {
                            if (list.get(i).isCheck) {
                                removeList.add(list.get(i));
                                skuCodes.add(collectionAdapter.getItem(i).skuCode);
                            }
                        }
                        deleteGoods(skuCodes, -1, removeList,type);

                    }
                }
            }
        }).show();
    }

    /**
     * 调用删除接口删除商品
     *
     * @param skuCodes
     * @param position
     * @param removeList 1 为删除
     */
    public void deleteGoods(final List<String> skuCodes, final int position, final List<CollectionResponse.ResultListBean> removeList,final String type) {
        ApiImpl.deleteCollection(mBaseContext, LoginHelper.getInstance().getIdPerson(), skuCodes, 1, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (DELETEONE.equals(type)) {
                    if (position < collectionAdapter.getCount()) {
                        collectionAdapter.getData().remove(position);
                    }
                } else {
                    //全选或选择删除
                    if (CommonUtils.isNullOrEmpty(removeList)) {
                        return;
                    }
                    collectionAdapter.getData().removeAll(removeList);
                }
                collectionAdapter.notifyDataSetChanged();
                if (CommonUtils.isNullOrEmpty(collectionAdapter.getData())) {
                    baseLayout.showEmpty("暂无收藏");
                    ptrlv.setVisibility(View.GONE);
                } else {
                    baseLayout.hideEmpty();
                    ptrlv.setVisibility(View.VISIBLE);
                }
                ToastUtils.showShortToast("删除成功");
                deleteReset(collectionAdapter.getData());
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    /**
     * 删除后还原状态，隐藏下部删除控件，全选清除
     *
     * @param list
     */
    public void deleteReset(List<CollectionResponse.ResultListBean> list) {
        clearChoose();
        if (CommonUtils.isNullOrEmpty(list)) {
            //全部都已经删除
            baseLayout.setRightText("");
        } else {
            baseLayout.setRightText("编辑");
        }
        for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
            collectionResponse.isShowCb = false;
        }
        collectionAdapter.notifyDataSetChanged();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ptrlv.setLayoutParams(layoutParams);
        llBottomDelete.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Const.COLLECTION == requestCode && RESULT_OK == resultCode) {
            pageIndex = 1;
            setData();
        }
    }
}
