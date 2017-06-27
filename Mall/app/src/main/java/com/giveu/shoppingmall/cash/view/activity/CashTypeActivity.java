package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.me.view.activity.AddBankCardFirstActivity;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.NoScrollGridView;
import com.lichfaker.scaleview.HorizontalScaleScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    private LvCommonAdapter<String> stagingTypeAdapter;
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

    }


    @Override
    public void setData() {
        final List<String> data = new ArrayList<>();
        data.add("按日计息");
        data.add("24期");
        data.add("18期");
        data.add("16期");
        data.add("12期");
        data.add("8期");
        data.add("6期");
        data.add("4期");
        data.add("3期");
        stagingTypeAdapter = new LvCommonAdapter<String>(mBaseContext, R.layout.tv_cash_type_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                TextView tv_staing_type = viewHolder.getView(R.id.tv_staging_type);
                tv_staing_type.setText(item);
            }
        };
        gvStagingType.setAdapter(stagingTypeAdapter);
    }

    @OnClick(R.id.rl_add_bank_card)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        AddBankCardFirstActivity.startIt(mBaseContext);
    }
}
