package com.giveu.shoppingmall.me.view.activity;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.TransactionAdapter;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.view.dialog.DateSelectDialog;
import com.giveu.shoppingmall.view.flowlayout.FlowLayout;
import com.giveu.shoppingmall.view.flowlayout.TagAdapter;
import com.giveu.shoppingmall.view.flowlayout.TagFlowLayout;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.view.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionSearchActivity extends BaseActivity {
    @BindView(R.id.tf_category)
    TagFlowLayout tfCategory;
    @BindView(R.id.tf_state)
    TagFlowLayout tfState;
    @BindView(R.id.tv_choose_type)
    TextView tvChooseType;
    @BindView(R.id.tv_choose_date)
    TextView tvChooseDate;
    @BindView(R.id.tv_reset)
    TextView tvReset;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private TransactionAdapter transactionAdapter;
    private ArrayList<String> transactionList;
    private TagAdapter<String> categoryAdapter;
    private TagAdapter<String> stateAdapter;
    private ArrayList<String> categoryList;
    private ArrayList<String> stateList;
    private ObjectAnimator showAnimator;
    private ObjectAnimator hideAnimator;
    private ObjectAnimator showAlphaAnimator;
    private ObjectAnimator hideAlphaAnimator;
    private int pageIndex = 1;
    private final int pageSize = 10;
    private DateSelectDialog dateSelectDialog;
    private boolean currentTypeIsMonth = true;//区分是按月选择还是按日选择

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, TransactionSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_search);
        baseLayout.setTitle("交易查询");
        baseLayout.setRightImageAndListener(R.drawable.ic_transaction_search, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseLayout.goneRightImage();
                if (llSearch.getVisibility() == View.GONE) {
                    llSearch.setVisibility(View.VISIBLE);
                }
                showSearchView();
            }
        });
        stateList = new ArrayList<>();
        stateList.add("全部");
        stateList.add("取现");
        stateList.add("现金分期");
        stateList.add("商城消费");
        stateList.add("线下消费");

        categoryList = new ArrayList<>();
        categoryList.add("全部");
        categoryList.add("已结清");
        categoryList.add("未结清");


        stateAdapter = new TagAdapter<String>(stateList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(mBaseContext).inflate(R.layout.flowlayout__choose_item, tfState, false);
                tvTag.setText(s);
                return tvTag;

            }
        };
        tfState.setAdapter(stateAdapter);
        categoryAdapter = new TagAdapter<String>(categoryList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(mBaseContext).inflate(R.layout.flowlayout__choose_item, tfState, false);
                tvTag.setText(s);
                return tvTag;
            }
        };
        tfCategory.setAdapter(categoryAdapter);
        transactionList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            transactionList.add(i + "");
        }
        transactionAdapter = new TransactionAdapter(mBaseContext, transactionList);
        ptrlv.setAdapter(transactionAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        initAnimation();
        hideSearchView();
        llSearch.setVisibility(View.GONE);
        dateSelectDialog = new DateSelectDialog(mBaseContext);
        tvChooseDate.setText(dateSelectDialog.getCurrentYearAndMonth());
    }

    private void initAnimation() {
        //这个高度是搜索view的高度，需要减去头部的高度
        float disPlayHeight = DensityUtils.getHeight() - DensityUtils.dip2px(50);
        showAnimator = ObjectAnimator.ofFloat(llSearch, "translationY", -disPlayHeight, 0f).setDuration(500);
        showAlphaAnimator = ObjectAnimator.ofFloat(llSearch, "alpha", 0f, 1f).setDuration(500);
        hideAnimator = ObjectAnimator.ofFloat(llSearch, "translationY", 0f, -disPlayHeight).setDuration(500);
        hideAlphaAnimator = ObjectAnimator.ofFloat(llSearch, "alpha", 1f, 0f).setDuration(500);
    }

    /**
     * 显示搜索框
     */
    private void showSearchView() {
        if (!showAnimator.isRunning()) {
            showAnimator.start();
            showAlphaAnimator.start();
        }
    }

    /**
     * 隐藏搜索框
     */
    private void hideSearchView() {
        if (!hideAnimator.isRunning()) {
            hideAnimator.start();
            hideAlphaAnimator.start();
        }
    }

    @OnClick({R.id.view_blank, R.id.tv_choose_type, R.id.ll_choose_date, R.id.ll_search})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.view_blank:
                hideSearchView();
                baseLayout.showRightImage();
                break;

            case R.id.tv_choose_type:
                currentTypeIsMonth = !currentTypeIsMonth;
                if (currentTypeIsMonth) {
                    tvChooseType.setText("按月选择");
                } else {
                    tvChooseType.setText("按日选择");
                }
                break;

            case R.id.ll_choose_date:
                if (currentTypeIsMonth) {
                    dateSelectDialog.hideDay();
                    dateSelectDialog.show();
                } else {
                    dateSelectDialog.showDay();
                    dateSelectDialog.show();
                }
                break;

            case R.id.ll_search:
                break;

            default:
                break;
        }
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        dateSelectDialog.setOnDateSelectListener(new DateSelectDialog.OnDateSelectListener() {
            @Override
            public void onSelectDate(String year, String month, String day) {
                if (currentTypeIsMonth) {
                    tvChooseDate.setText(year + "年" + month + "月");
                } else {
                    tvChooseDate.setText(year + "年" + month + "月" + day + "日");
                }
            }
        });

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                setData();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullRefreshEnable(true);
            }
        });
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0 && position - 1 < transactionList.size()) {
                    TransactionDetailActivity.startIt(mBaseContext);
                }

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (showAnimator != null) {
            showAnimator.end();
            showAnimator.cancel();
        }
        if (hideAnimator != null) {
            hideAnimator.end();
            hideAnimator.cancel();
        }
        if (showAlphaAnimator != null) {
            showAlphaAnimator.end();
            showAlphaAnimator.cancel();
        }
        if (hideAlphaAnimator != null) {
            hideAlphaAnimator.end();
            hideAlphaAnimator.cancel();
        }
    }
}
