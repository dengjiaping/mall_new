package com.giveu.shoppingmall.index.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 钱包激活首页页面
 * Created by 101900 on 2017/6/19.
 */

public class WalletActivationFirstActivity extends BaseActivity {

    @BindView(R.id.iv_name)
    ImageView ivName;
    @BindView(R.id.et_name)
    EditView etName;
    @BindView(R.id.iv_ident)
    ImageView ivIdent;
    @BindView(R.id.et_ident)
    EditView etIdent;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wallet_activation_first);
        baseLayout.setTitle("钱包激活");
        CommonUtils.openSoftKeyBoard(mBaseContext);
    }

    @Override
    public void setListener() {
        super.setListener();
        etName.checkFormat(EditView.Style.NAME);
        etIdent.checkFormat(EditView.Style.IDENT);
        editTextListener(etName, ivName);
        editTextListener(etIdent, ivIdent);
    }

    @Override
    public void setData() {

    }

    /**
     * 每个输入框监听 输入字符后图标改变，未输时还原图标
     *
     * @param editText
     * @param imageView
     */
    public void editTextListener(final EditText editText, final ImageView imageView) {
        editText.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    imageView.setImageResource(R.drawable.ic_pen);
                } else {
                    imageView.setImageResource(R.drawable.ic_add);
                }
                nextButtonCanClick(false);
            }
        });
    }

    @OnClick(R.id.tv_next)
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (tvNext.isClickEnabled()) {
            WalletActivationSecondActivity.startIt(mBaseContext, StringUtils.getTextFromView(etName), StringUtils.getTextFromView(etIdent));
        } else {
            nextButtonCanClick(true);
        }
    }

    /**
     * 下一步按钮的颜色控制
     *
     * @param showToast
     * @return
     */
    private void nextButtonCanClick(boolean showToast) {
        tvNext.setClickEnabled(false);
        String name = StringUtils.getTextFromView(etName);
        String ident = StringUtils.getTextFromView(etIdent);

        if (StringUtils.isNull(name)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入姓名！");
            }
            return;
        }
        if (!StringUtils.checkUserNameAndTipError(name, showToast)) {
            return;
        }
        if (!StringUtils.checkIdCardAndTipError(ident, showToast)) {
            return;
        }
        tvNext.setClickEnabled(true);
    }


}