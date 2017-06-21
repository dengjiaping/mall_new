package com.giveu.shoppingmall.me.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 添加银行卡首页
 * Created by 101900 on 2017/2/22.
 */

public class AddCardFirstActivity extends BaseActivity {
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_banklist)
    TextView tvBanklist;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_banknumber)
    EditText etBanknumber;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_addcardfirst);
        baseLayout.setTitle("添加银行卡");
        //查看支持银行列表
        CommonUtils.setTextWithSpan(tvBanklist, false, "查看", "支持银行", R.color.color_9b9b9b, R.color.title_color, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开银行列表
                BankActivity.startIt(mBaseContext);
            }
        });
    }

    @Override
    public void setData() {
    }

    //是否输入相关信息的判断
    private boolean checkEditTextExceptCode() {
        String username = StringUtils.getTextFromView(etUsername);
        String bankNumber = StringUtils.getTextFromView(etBanknumber);
        if (!StringUtils.checkUserNameAndTipError(username)) {
            return false;
        }
        if (StringUtils.isNull(bankNumber)) {
            ToastUtils.showShortToast("请输入银行卡号");
            return false;
        } else if (!StringUtils.isCardNum(bankNumber)) {
            ToastUtils.showShortToast("请输入正确的银行卡号");
            return false;
        }
        return true;
    }

    @OnClick(R.id.tv_next)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (checkEditTextExceptCode()) {
            AddCardSecondActivity.startIt(mBaseContext);
        }
    }

    public static void startIt(Activity mActivity){
        Intent intent = new Intent(mActivity, AddCardFirstActivity.class);
        mActivity.startActivity(intent);
    }
}
