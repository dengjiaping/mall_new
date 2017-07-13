package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.IdentifyCardResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 添加银行卡首页
 * Created by 101900 on 2017/2/22.
 */

public class AddBankCardFirstActivity extends BaseActivity {

    @BindView(R.id.tv_banklist)
    TextView tvBanklist;
    @BindView(R.id.et_username)
    EditView etUsername;
    @BindView(R.id.et_banknumber)
    EditView etBanknumber;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, AddBankCardFirstActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcardfirst);
        baseLayout.setTitle("添加银行卡");
        CommonUtils.openSoftKeyBoard(mBaseContext);
        //查看支持银行列表
        CommonUtils.setTextWithSpan(tvBanklist, false, "查看", "支持银行", R.color.color_9b9b9b, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开银行列表
                CommonUtils.closeSoftKeyBoard(mBaseContext);
                BankActivity.startIt(mBaseContext);
            }
        });
    }

    @Override
    public void setListener() {
        super.setListener();
        etUsername.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                tvNext.setClickEnabled(false);
                nextButtonCanClick(false);
            }
        });
        etBanknumber.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                tvNext.setClickEnabled(false);
                nextButtonCanClick(false);
            }
        });
    }

    @Override
    public void setData() {
        etUsername.checkFormat(EditView.Style.NAME);
    }

    /**
     * 用于判断下一步按钮的颜色，是否可以点击
     *
     * @param showToast
     */
    public void nextButtonCanClick(boolean showToast) {
        String username = StringUtils.getTextFromView(etUsername);
        String bankNumber = StringUtils.getTextFromView(etBanknumber);
        if (!StringUtils.checkUserNameAndTipError(username, showToast)) {
            return;
        }
        if (StringUtils.isNull(bankNumber)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入银行卡号");
            }
            return;
        }
        if (!showToast) {//只在输入时判断按钮颜色，点击按钮不操作按钮颜色
            tvNext.setClickEnabled(true);
        }
    }

    @OnClick(R.id.tv_next)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (tvNext.isClickEnabled()) {
            CommonUtils.closeSoftKeyBoard(etBanknumber.getWindowToken(), mBaseContext);
            final String name = StringUtils.getTextFromView(etUsername);
            final String bankNo = StringUtils.getTextFromView(etBanknumber);
            ApiImpl.identifyCard(mBaseContext, name, bankNo, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<IdentifyCardResponse>() {
                @Override
                public void onSuccess(IdentifyCardResponse response) {
                    String bankCode = "";
                    String bankName = "";
                    if (response != null) {
                        if (response.data != null) {
                            bankCode = response.data.bankCode;
                            bankName = response.data.bankName;
                        }
                    }
                    AddBankCardSecondActivity.startIt(mBaseContext, bankCode, bankNo, bankName, name);
                }

                @Override
                public void onError(BaseBean errorBean) {
                    if (errorBean != null) {
                        NormalHintDialog dialog = new NormalHintDialog(mBaseContext, errorBean.message);
                        dialog.showDialog();

                    }
                }
            });
        } else {
            nextButtonCanClick(true);
        }
    }


}
