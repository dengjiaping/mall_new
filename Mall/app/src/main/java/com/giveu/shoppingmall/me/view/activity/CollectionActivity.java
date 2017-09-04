package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.CollectionAdapter;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.dialog.CustomDialogUtil;
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
    List<CollectionResponse> list;
    boolean rightTextClick = true;//true 编辑 false 取消
    @BindView(R.id.ll_bottom_delete)
    LinearLayout llBottomDelete;
    @BindView(R.id.cb_choose)
    CheckBox cbChoose;
    @BindView(R.id.tv_delete_text)
    TextView tvDeleteText;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CollectionActivity.class);
        mActivity.startActivity(intent);
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collection);
        baseLayout.setTitle("我的收藏");
        baseLayout.setRightText("编辑");
        baseLayout.setRightTextListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightTextClick) {
                    baseLayout.setRightText("取消");
                    llBottomDelete.setVisibility(View.VISIBLE);
                    clearChoose();
                    cbChoose.setChecked(false);
                } else {
                    baseLayout.setRightText("编辑");
                    llBottomDelete.setVisibility(View.GONE);
                }
                for (CollectionResponse collectionResponse : collectionAdapter.getData()) {
                    collectionResponse.isShowCb = rightTextClick;
                }
                collectionAdapter.notifyDataSetChanged();
                rightTextClick = rightTextClick == true ? false : true;
            }
        });
        baseLayout.setRightTextColor(R.color.title_color);
    }

    @Override
    public void setListener() {
        super.setListener();
        cbChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbChoose.isChecked()) {
                    //全选
                    for (CollectionResponse collectionResponse : collectionAdapter.getData()) {
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
                showDeleteGoodsDialog(collectionAdapter, position);
                return false;
            }
        });

        tvDeleteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CollectionResponse> list = collectionAdapter.getData();
                List<CollectionResponse> removeList = new ArrayList<>();
                for (int i = 0; i < collectionAdapter.getData().size(); i++) {
                    Log.e("isCheck",i+"----"+list.get(i).isCheck);
                    if (true == list.get(i).isCheck) {
                        removeList.add(list.get(i));
                    }
                }
                collectionAdapter.getData().removeAll(removeList);
                collectionAdapter.notifyDataSetChanged();
                ToastUtils.showLongToast("删除成功");
            }
        });
    }

    @Override
    public void setData() {
        list = new ArrayList<>();
        CollectionResponse collectionResponse1 = new CollectionResponse(false, false, "1");
        CollectionResponse collectionResponse2 = new CollectionResponse(false, false, "2");
        CollectionResponse collectionResponse3 = new CollectionResponse(false, false, "3");
        CollectionResponse collectionResponse4 = new CollectionResponse(false, false, "4");
        CollectionResponse collectionResponse5 = new CollectionResponse(false, false, "5");
        CollectionResponse collectionResponse6 = new CollectionResponse(false, false, "6");
        CollectionResponse collectionResponse7 = new CollectionResponse(false, false, "7");
        list.add(collectionResponse1);
        list.add(collectionResponse2);
        list.add(collectionResponse3);
        list.add(collectionResponse4);
        list.add(collectionResponse5);
        list.add(collectionResponse6);
        list.add(collectionResponse7);

        collectionAdapter = new CollectionAdapter(mBaseContext, list, new CollectionAdapter.CbItemCheckListener() {
            @Override
            public void itemClick() {
                int count = 0;
                for (CollectionResponse collectionResponse : collectionAdapter.getData()) {
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

    /**
     * 清除全选
     */
    public void clearChoose() {
        for (CollectionResponse collectionResponse : collectionAdapter.getData()) {
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
     */
    public void showDeleteGoodsDialog(final CollectionAdapter collectionAdapter, final int position) {
        CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
        customDialogUtil.getDialogMode1("提示", "是否要删除该收藏商品？", "确定", "取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionAdapter.getData().remove(position - 1);
                collectionAdapter.notifyDataSetChanged();
                ToastUtils.showLongToast("删除成功");
            }
        }, null).show();
    }
}
