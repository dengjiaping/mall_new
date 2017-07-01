package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.cash.view.dialog.MonthlyDetailsDialog;
import com.giveu.shoppingmall.me.view.activity.AddBankCardFirstActivity;
import com.giveu.shoppingmall.model.bean.response.CashTypeResponse;
import com.giveu.shoppingmall.recharge.view.dialog.PwdDialog;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.lichfaker.scaleview.HorizontalScaleScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.giveu.shoppingmall.R.id.tv_ensure;

/**
 * 取现类型页
 * Created by 101900 on 2017/6/26.
 */

public class CashTypeActivity extends BaseActivity {
    TextView mTvVerticalScale;
    HorizontalScaleScrollView scaleScrollView;
    @BindView(R.id.et_input_amount)
    EditText etInputAmount;
    @BindView(R.id.gv_staging_type)
    NoScrollGridView gvStagingType;
    @BindView(R.id.rl_add_bank_card)
    RelativeLayout rlAddBankCard;
    @BindView(R.id.tv_monthly_payment)
    TextView tvMonthlyPayment;
    @BindView(tv_ensure)
    TextView tvEnsure;
    private LvCommonAdapter<CashTypeResponse> stagingTypeAdapter;
    int amount;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, CashTypeActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cash_type);
        baseLayout.setTitle("我要取现");

        scaleScrollView = (HorizontalScaleScrollView) findViewById(R.id.horizontalScale);


//        findViewById(R.id.horizontalScaleValueBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String valStr = mTvHorizontalScale.getText().toString().trim();
//                scaleScrollView.setCurScale(Integer.parseInt(valStr));
//            }
//        });
    }

    @Override
    public void setListener() {
        super.setListener();
        scaleScrollView.setOnScrollListener(new HorizontalScaleScrollView.OnScrollListener() {
            @Override
            public void onScaleScroll(int scale) {
                etInputAmount.setText("" + scale * 50);
            }
        });

        etInputAmount.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                etInputAmount.setSelection(s.length());
                if (StringUtils.isNotNull(s.toString())) {
                    if (Integer.parseInt(s.toString()) == amount) {//这一次输入与上一次相同，不做操作
                        return;
                    }
                    amount = Integer.parseInt(s.toString());
                    if (amount >= 0 && amount <= 3000 && (amount % 50 == 0)) {
                        scaleScrollView.setCurScale(amount);
                    }
                }
            }
        });

        gvStagingType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int checkId, long id) {
                if (stagingTypeAdapter != null) {
                    if (stagingTypeAdapter.getCount() > 0) {
                        for (int i = 0; i < stagingTypeAdapter.getCount(); i++) {
                            if (checkId == i) {
                                stagingTypeAdapter.getItem(checkId).isChecked = true;
                            } else {
                                stagingTypeAdapter.getItem(i).isChecked = false;
                            }
                        }
                        stagingTypeAdapter.notifyDataSetChanged();
                    }
                }


            }
        });
    }


    @Override
    public void setData() {
        CashTypeResponse c1 = new CashTypeResponse("按日计息", false);
        CashTypeResponse c2 = new CashTypeResponse("24期", false);
        CashTypeResponse c3 = new CashTypeResponse("18期", false);
        CashTypeResponse c4 = new CashTypeResponse("16期", false);
        CashTypeResponse c5 = new CashTypeResponse("12期", false);
        CashTypeResponse c6 = new CashTypeResponse("8期", false);
        CashTypeResponse c7 = new CashTypeResponse("6期", false);
        CashTypeResponse c8 = new CashTypeResponse("4期", false);
        CashTypeResponse c9 = new CashTypeResponse("3期", false);
        final List<CashTypeResponse> data = new ArrayList<>();
        data.add(c1);
        data.add(c2);
        data.add(c3);
        data.add(c4);
        data.add(c5);
        data.add(c6);
        data.add(c7);
        data.add(c8);
        data.add(c9);

        stagingTypeAdapter = new LvCommonAdapter<CashTypeResponse>(mBaseContext, R.layout.tv_cash_type_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, CashTypeResponse item, int position) {
                TextView tv_staging_type = viewHolder.getView(R.id.tv_staging_type);
                tv_staging_type.setText(item.month);
                if (item.isChecked) {
                    tv_staging_type.setTextColor(getResources().getColor(R.color.white));
                    tv_staging_type.setBackgroundResource(R.drawable.shape_ordinary_pressed);
                } else {
                    tv_staging_type.setTextColor(getResources().getColor(R.color.title_color));
                    tv_staging_type.setBackgroundResource(R.drawable.shape_ordinary_normal);
                }
            }
        };
        gvStagingType.setAdapter(stagingTypeAdapter);
    }

    @OnClick({R.id.tv_monthly_payment, R.id.rl_add_bank_card, R.id.tv_ensure})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_monthly_payment:
                MonthlyDetailsDialog monthlyDetailsDialog = new MonthlyDetailsDialog(mBaseContext);
                monthlyDetailsDialog.showDialog();
                //查看月供
                break;
            case R.id.rl_add_bank_card:
                //添加银行卡
                AddBankCardFirstActivity.startIt(mBaseContext);
                break;
            case R.id.tv_ensure:
                //确定
                PwdDialog pwdDialog = new PwdDialog(mBaseContext);
                pwdDialog.showDialog();
                break;
        }

    }
}
