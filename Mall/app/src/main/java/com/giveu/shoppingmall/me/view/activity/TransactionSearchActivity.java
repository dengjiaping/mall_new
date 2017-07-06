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
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.adapter.TransactionAdapter;
import com.giveu.shoppingmall.me.presenter.TransactionPresenter;
import com.giveu.shoppingmall.me.view.agent.ITransactionView;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.TypeUtlis;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.dialog.DateSelectDialog;
import com.giveu.shoppingmall.widget.flowlayout.FlowLayout;
import com.giveu.shoppingmall.widget.flowlayout.TagAdapter;
import com.giveu.shoppingmall.widget.flowlayout.TagFlowLayout;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 513419 on 2017/6/23.
 */

public class TransactionSearchActivity extends BaseActivity implements ITransactionView {
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
    ClickEnabledTextView tvSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    private TransactionAdapter transactionAdapter;
    private ArrayList<ContractResponse.Contract> transactionList;
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
    private String creditStatus;
    private String creditType;
    private String idPerson;
    private String loanDate;
    private String timeType;
    //区分搜索框的数据和真正要进行查询时的值
    private String selCreditStatus;
    private String selCreditType;
    private String selLoanDate;
    private String selTimeType;
    private DateSelectDialog dateSelectDialog;
    private boolean currentTypeIsMonth = true;//区分是按月选择还是按日选择
    private TransactionPresenter presenter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, TransactionSearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_transaction_search);
        baseLayout.setTitle("交易查询");
        baseLayout.goneRightImage();
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
        tfState.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer integer : selectPosSet) {
                    selCreditType = TypeUtlis.getCreditTypeTotalValue(stateList.get(integer));
                    canClick();
                }
            }
        });
        tfState.setAdapter(stateAdapter);
        categoryAdapter = new TagAdapter<String>(categoryList) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tvTag = (TextView) LayoutInflater.from(mBaseContext).inflate(R.layout.flowlayout__choose_item, tfState, false);
                tvTag.setText(s);
                return tvTag;
            }
        };

        tfCategory.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                for (Integer integer : selectPosSet) {
                    selCreditStatus = TypeUtlis.getCreditStatusValue(stateList.get(integer));
                    canClick();
                }
            }
        });
        tfCategory.setAdapter(categoryAdapter);
        transactionList = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(mBaseContext, transactionList);
        ptrlv.setAdapter(transactionAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        initAnimation();
        dateSelectDialog = new DateSelectDialog(mBaseContext);
        //设置时间默认值
        tvChooseDate.setText(dateSelectDialog.getCurrentYearAndMonth(null));
        selLoanDate = dateSelectDialog.getCurrentYearAndMonth("/");
        presenter = new TransactionPresenter(this);
        //默认是按月查询
        selTimeType = "Month";
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    private void initAnimation() {
        //这个高度是搜索view的高度，需要减去头部的高度
        float disPlayHeight = DensityUtils.getHeight() - DensityUtils.dip2px(50);
        showAnimator = ObjectAnimator.ofFloat(llSearch, "translationY", -disPlayHeight, 0f).setDuration(500);
        showAlphaAnimator = ObjectAnimator.ofFloat(llSearch, "alpha", 0f, 1f).setDuration(500);
        hideAnimator = ObjectAnimator.ofFloat(llSearch, "translationY", 0f, -disPlayHeight).setDuration(500);
        hideAlphaAnimator = ObjectAnimator.ofFloat(llSearch, "alpha", 1f, 0f).setDuration(500);
    }

    private boolean canClick() {
        if (tfState.getSelectedList().size() > 0 && tfCategory.getSelectedList().size() > 0) {
            tvSearch.setClickEnabled(true);
            return true;
        } else {
            tvSearch.setClickEnabled(false);
            return false;
        }
    }

    /**
     * 显示搜索框
     */
    private void showSearchView() {
        if (!showAnimator.isRunning()) {
            hideAnimator.end();
            hideAlphaAnimator.end();
            showAnimator.start();
            showAlphaAnimator.start();
        }
        baseLayout.setClickable(true);
    }

    /**
     * 隐藏搜索框
     */
    private void hideSearchView() {
        if (!hideAnimator.isRunning()) {
            showAnimator.end();
            showAlphaAnimator.end();
            hideAnimator.start();
            hideAlphaAnimator.start();
        }
        baseLayout.setClickable(false);
    }

    @OnClick({R.id.view_blank, R.id.tv_choose_type, R.id.ll_choose_date, R.id.tv_search, R.id.tv_reset})
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
                //选择日期类型后需重新更新已选择的日期
                if (currentTypeIsMonth) {
                    tvChooseType.setText("按月选择");
                    selTimeType = "Month";
                    tvChooseDate.setText(dateSelectDialog.getCurrentYearAndMonth(null));
                    selLoanDate = dateSelectDialog.getCurrentYearAndMonth("/");
                } else {
                    tvChooseType.setText("按日选择");
                    selTimeType = "Day";
                    tvChooseDate.setText(dateSelectDialog.getCurrentYearAndMonthAndDay(null));
                    selLoanDate = dateSelectDialog.getCurrentYearAndMonthAndDay("/");
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

            case R.id.tv_search:
                //搜索交易记录
                if (tvSearch.isClickEnabled()) {
                    searchTransaction();
                    hideSearchView();
                    baseLayout.showRightImage();
                    transactionList.clear();
                    transactionAdapter.notifyDataSetChanged();
                }
                break;

            case R.id.tv_reset:
                //重置搜索条件
                stateAdapter.notifyDataChanged();
                categoryAdapter.notifyDataChanged();
                selCreditStatus = "";
                selCreditType = "";
                selTimeType = "Month";
                dateSelectDialog.setOriginalDate();
                tvChooseDate.setText(dateSelectDialog.getCurrentYearAndMonth(null));
                selLoanDate = dateSelectDialog.getCurrentYearAndMonth("/");
                tvChooseType.setText("按月选择");
                tvSearch.setClickEnabled(false);
                dateSelectDialog.hideDay();
                break;

            default:
                break;
        }
    }

    public void searchTransaction() {
        creditStatus = selCreditStatus;
        creditType = selCreditType;
        loanDate = selLoanDate;
        timeType = selTimeType;
        presenter.searchContract(creditStatus, creditType, LoginHelper.getInstance().getIdPerson(), loanDate, pageIndex, pageSize, timeType);
    }

    @Override
    public void setData() {

    }

    @Override
    public void setListener() {
        super.setListener();
        baseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSearchView();
                baseLayout.showRightImage();
            }
        });
        dateSelectDialog.setOnDateSelectListener(new DateSelectDialog.OnDateSelectListener() {
            @Override
            public void onSelectDate(String year, String month, String day) {
                if (currentTypeIsMonth) {
                    tvChooseDate.setText(year + "年" + month + "月");
                    selLoanDate = year + "/" + month;
                } else {
                    tvChooseDate.setText(year + "年" + month + "月" + day + "日");
                    selLoanDate = year + "/" + month + "/" + day;
                }
            }
        });

        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pageIndex = 1;
                searchTransaction();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                searchTransaction();
                ptrlv.setPullRefreshEnable(true);
            }
        });
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position - 1 >= 0 && position - 1 < transactionList.size()) {
                    ContractResponse.Contract contract = transactionList.get(position - 1);
                    TransactionDetailActivity.startIt(mBaseContext, contract.idCredit, contract.creditType);
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

    @Override
    public void showContractResult(ArrayList<ContractResponse.Contract> contractList) {
        //显示查询结果
        if (pageIndex == 1) {
            ptrlv.onRefreshComplete();
            ptrlv.setPullRefreshEnable(true);
        }
        if (CommonUtils.isNotNullOrEmpty(contractList)) {
            if (pageIndex == 1) {
                transactionList.clear();
                if (contractList.size() >= pageSize) {
                    ptrlv.setPullLoadEnable(true);
                } else {
                    ptrlv.setPullLoadEnable(false);
                    ptrlv.showEnd("没有更多数据");
                }
                baseLayout.showEmpty("暂无交易记录");
            }
            baseLayout.hideEmpty();
            transactionList.addAll(contractList);
            transactionAdapter.notifyDataSetChanged();
            pageIndex++;
        } else {
            if (pageIndex == 1) {
                baseLayout.showEmpty("暂无交易记录");
                ptrlv.setPullLoadEnable(false);
            } else {
                ptrlv.setPullLoadEnable(false);
                ptrlv.showEnd("没有更多数据");
            }
        }
    }
}
