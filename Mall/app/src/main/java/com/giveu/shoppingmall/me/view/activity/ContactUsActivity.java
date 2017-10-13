package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.mynet.ApiUrl;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 联系我们
 * Created by 101900 on 2017/6/26.
 */

public class ContactUsActivity extends BaseActivity {


    @BindView(R.id.ll_call_phone)
    LinearLayout llCallPhone;
    @BindView(R.id.ll_common_problem)
    LinearLayout llCommonProblem;
    @BindView(R.id.ll_problem_feedback)
    LinearLayout llProblemFeedback;
    @BindView(R.id.tv_version_name)
    TextView tvVersionName;


    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, ContactUsActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_contact_us);
        baseLayout.setTitle("联系我们");
    }

    @Override
    public void setData() {
        String version_name = CommonUtils.getVersionName();
        if (StringUtils.isNotNull(version_name)) {
            tvVersionName.setText(version_name);
        }
    }

    @OnClick({R.id.ll_call_phone, R.id.ll_common_problem, R.id.ll_problem_feedback})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_call_phone:
                //拨打热线电话
                CustomerServicePhoneActivity.startIt(mBaseContext);
                break;
            case R.id.ll_common_problem:
                //常见问题
                // CommonProblemActivity.startIt(mBaseContext);
                CustomWebViewActivity.startIt(mBaseContext, ApiUrl.WebUrl.commonProblem, "常见问题");
                break;
            case R.id.ll_problem_feedback:
                //异常问题反馈
                if (LoginHelper.getInstance().hasLoginAndGotoLogin(mBaseContext)) {
                    ProblemFeedbackActivity.startIt(mBaseContext);
                }
                break;
        }
    }
}
