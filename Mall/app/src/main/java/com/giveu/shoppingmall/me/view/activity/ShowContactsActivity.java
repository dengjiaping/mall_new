package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.ContactsBean;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;

import butterknife.BindView;


/**
 * 显示联系人
 * Created by 101900 on 2017/8/9.
 */

public class ShowContactsActivity extends BaseActivity {

    @BindView(R.id.et_contacts_relationship)
    TextView etContactsRelationship;
    @BindView(R.id.et_contacts_name)
    TextView etContactsName;
    @BindView(R.id.et_contacts_phone)
    TextView etContactsPhone;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, ShowContactsActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_show_contacts);
        baseLayout.setTitle("我的联系人");
    }

    @Override
    public void setData() {
        ApiImpl.getOtherContact(mBaseContext, LoginHelper.getInstance().getIdPerson(), new BaseRequestAgent.ResponseListener<ContactsBean>() {
            @Override
            public void onSuccess(ContactsBean response) {
                if(response != null){
                    if(response.data != null){
                        ContactsBean contactsBean = response.data;
                        String relationship = StringUtils.nullToEmptyString(contactsBean.typeName);
                        String phone = StringUtils.nullToEmptyString(contactsBean.phone);
                        String name = StringUtils.nullToEmptyString(contactsBean.name);
                        etContactsRelationship.setText(relationship);
                        etContactsPhone.setText(phone);
                        etContactsName.setText(name);
                    }
                }

            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });
    }
}
