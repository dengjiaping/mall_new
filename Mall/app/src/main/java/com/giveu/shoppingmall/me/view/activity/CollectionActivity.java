package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CollectionAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.R.attr.type;


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
    List<CollectionResponse.ResultListBean> goodsList;
    private static final String DELETEONE = "1";//长按删除某一项
    private static final String DELETEMORE = "2";//全选删除多项

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CollectionActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);

        baseLayout.setTitle("我的收藏");
        baseLayout.setRightText("编辑");
        baseLayout.setRightTextColor(R.color.title_color);
        baseLayout.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectionAdapter == null || CommonUtils.isNullOrEmpty(collectionAdapter.getData())){
                    return;
                }
                if (rightTextClick) {
                    baseLayout.setRightText("取消");
                    llBottomDelete.setVisibility(View.VISIBLE);
                    clearChoose();
                    cbChoose.setChecked(false);
                } else {
                    baseLayout.setRightText("编辑");
                    llBottomDelete.setVisibility(View.GONE);
                }
                for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
                    collectionResponse.isShowCb = rightTextClick;
                }
                rightTextClick = rightTextClick == true ? false : true;
            }
        });
        goodsList = new ArrayList<>();
        collectionAdapter = new CollectionAdapter(mBaseContext, goodsList, new CollectionAdapter.CbItemCheckListener() {
            @Override
            public void itemClick() {
                int count = 0;
                if(collectionAdapter == null || CommonUtils.isNullOrEmpty(collectionAdapter.getData())){
                    return;
                }
                for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
                    if (true == collectionResponse.isCheck) {
                        count++;
                    }
                }
                deleteColorAndCanClick(count);
            }
        });
        ptrlv.setAdapter(collectionAdapter);


        deleteColorAndCanClick(0);
        ptrlv.setPullLoadEnable(false);
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
                //从1开始
                showDeleteGoodsDialog(collectionAdapter, position, DELETEONE);
                return false;
            }
        });
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullRefreshEnable(false);
            }
        });
        tvDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteGoodsDialog(collectionAdapter, -1, DELETEMORE);
            }
        });
    }

    @Override
    public void setData() {
        ApiImpl.getCollectionList(mBaseContext,"123456", 1, 10, new BaseRequestAgent.ResponseListener<CollectionResponse>() {
            @Override
            public void onSuccess(CollectionResponse response) {
                if (response != null && response.data != null) {
                    goodsList.addAll(response.data.resultList);
                    collectionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }

    /**
     * 清除全选
     */
    public void clearChoose() {
        if(collectionAdapter == null){
            return;
        }
        for (CollectionResponse.ResultListBean collectionResponse : collectionAdapter.getData()) {
            collectionResponse.isCheck = false;
        }
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
        customDialogUtil.getDialogMode1("提示", "是否要删除该收藏商品？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collectionAdapter != null && !CommonUtils.isNullOrEmpty(collectionAdapter.getData())) {
                    //满足条件
                    List<String> skuCodes = new ArrayList<>();
                    if (DELETEONE.equals(type)) {
                        if (position > 0 && (collectionAdapter.getItem(position - 1) != null)) {
                            //长按删除
                            skuCodes.add(collectionAdapter.getItem(position - 1).skuCode);
                            deleteGoods(skuCodes, position, null);
                        }
                    } else {
                        //全选或选择删除
                        List<CollectionResponse.ResultListBean> list = collectionAdapter.getData();
                        List<CollectionResponse.ResultListBean> removeList = new ArrayList<>();
                        for (int i = 0; i < collectionAdapter.getData().size(); i++) {
                            if (true == list.get(i).isCheck) {
                                removeList.add(list.get(i));
                                skuCodes.add(collectionAdapter.getItem(i).skuCode);
                            }
                        }
                        deleteGoods(skuCodes, -1, removeList);

                    }
                }
            }
        }, null).show();
    }

    /**
     * 调用删除接口删除商品
     *
     * @param skuCodes
     * @param position
     * @param removeList
     */
    public void deleteGoods(final List<String> skuCodes, final int position, final List<CollectionResponse.ResultListBean> removeList) {
        ApiImpl.deleteCollection(mBaseContext,"123456", skuCodes, 0, new BaseRequestAgent.ResponseListener<BaseBean>() {
            @Override
            public void onSuccess(BaseBean response) {
                if (DELETEONE.equals(type)) {
                    collectionAdapter.getData().remove(position - 1);
                } else {
                    //全选或选择删除
                    collectionAdapter.getData().removeAll(removeList);
                }
                collectionAdapter.notifyDataSetChanged();
                ToastUtils.showShortToast("删除成功");
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
