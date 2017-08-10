package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.activity.LivingAddressActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ContactsResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.EditView;
import com.giveu.shoppingmall.widget.dialog.CustomListDialog;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 完善个人信息-联系人
 * Created by 101900 on 2017/8/9.
 */

public class PerfectContactsActivity extends BaseActivity {

    @BindView(R.id.tv_relationship)
    TextView tvRelationship;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.ll_choose_relationship)
    LinearLayout llChooseRelationship;
    @BindView(R.id.et_contacts_name)
    EditView etContactsName;
    @BindView(R.id.et_contacts_phone)
    EditView etContactsPhone;
    @BindView(R.id.tv_next)
    ClickEnabledTextView tvNext;
    String flag;//上个页面的标记
    CustomListDialog contactsDialog;
    private ArrayList<CharSequence> contactsTextList = new ArrayList<>();
    private ArrayList<String> contactsCodeList = new ArrayList<>();
    public static void startIt(Activity mActivity, String flag) {
        Intent intent = new Intent(mActivity, PerfectContactsActivity.class);
        intent.putExtra("flag", flag);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_perfect_contacts);
        initChooseDialog();
        saveContactsList();
    }

    /**
     * 获取联系人关系列表
     */
    public void saveContactsList(){
        ApiImpl.getContactTypeInfo(mBaseContext, new BaseRequestAgent.ResponseListener<ContactsResponse>() {
            @Override
            public void onSuccess(ContactsResponse response) {
                for (ContactsResponse contactsResponse : response.data) {
                    contactsTextList.add(contactsResponse.typeName);
                    contactsCodeList.add(contactsResponse.personType);
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
    /**
     * 初始化其他证明选择对话框
     */
    private void initChooseDialog() {
        contactsDialog = new CustomListDialog(mBaseContext, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tvRelationship.setText(contactsTextList.get(position));
               // innerCode = innerCodeList.get(position).toString();
                contactsDialog.dismiss();
            }
        });
    }
    @Override
    public void setListener() {
        super.setListener();
        etContactsName.checkFormat(EditView.Style.NAME);
        etContactsPhone.checkFormat(11);
        etContactsName.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                //按钮置灰的判断
                nextButtonCanClick(false);
            }
        });
        etContactsPhone.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                //按钮置灰的判断
                nextButtonCanClick(false);
            }
        });
        llChooseRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtils.isNullOrEmpty(contactsCodeList) || CommonUtils.isNullOrEmpty(contactsTextList)){
                    //没有获取到列表
                    saveContactsList();
                }else{
                    contactsDialog.show();
                }
            }
        });
    }

    @Override
    public void setData() {
        contactsDialog.setData(contactsTextList);
         flag = getIntent().getStringExtra("flag");
        switch (flag) {
            case Const.CASH:
                //取现跳转过来
                baseLayout.setTitle("请完善资料");
                tvNext.setText("下一步");
                break;
            case Const.PERSONCENTER:
                //个人中心跳转过来
                baseLayout.setTitle("我的联系人");
                tvNext.setText("提交");
                break;
            case Const.RECHARGE:
                //充值跳转过来
                break;
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
        String relationship = StringUtils.getTextFromView(tvRelationship);
        String name = StringUtils.getTextFromView(etContactsName);
        String phone = StringUtils.getTextFromView(etContactsPhone);

        if ("请选择".equals(relationship)) {
            if (showToast) {
                ToastUtils.showShortToast("请选择与本人的关系");
            }
            return;
        }

        if (StringUtils.isNull(name)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入姓名！");
            }
            return;
        }
        if (!StringUtils.checkUserNameAndTipError(name, showToast)) {
            return;
        }
        if (!StringUtils.checkPhoneNumberAndTipError(phone, showToast)) {
            return;
        }
        tvNext.setClickEnabled(true);
    }

    @OnClick({R.id.ll_choose_relationship, R.id.tv_next})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_choose_relationship:
                break;
            case R.id.tv_next:
                if (tvNext.isClickEnabled()) {
                    switch (flag) {
                        case Const.CASH:
                            //取现跳转过来
                                //信息正确，下一步
                            if(LoginHelper.getInstance().hasExistLive()){
                                //设置了地址
                                finish();
                            }else{
                                //没设置地址，去设置
                                LivingAddressActivity.startIt(mBaseContext);
                            }
                            break;
                        case Const.PERSONCENTER:
                            //个人中心跳转过来
                            finish();
                            break;
                        case Const.RECHARGE:
                            //充值跳转过来
                            break;
                    }
                } else {
                    nextButtonCanClick(true);
                }
                break;
        }
    }
}
