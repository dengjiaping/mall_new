package com.giveu.shoppingmall.me.view.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.widget.imageselect.ImageItem;
import com.giveu.shoppingmall.widget.imageselect.ImagesSelectView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 问题反馈页面
 * Created by 101900 on 2017/6/23.
 */

public class ProblemFeedbackActivity extends BasePermissionActivity {


    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.image_select)
    ImagesSelectView imageSelect;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_problem_feedback);
        baseLayout.setTitle("问题反馈");
        setPermissionHelper(true, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA});
        setTextCustomerPhone();
        baseLayout.setRightTextColor(R.color.title_color);
        baseLayout.setRightTextAndListener("记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     CommonUtils.startActivity(mBaseContext, FeedbackListActivity.class);
            }
        });
    }

    private void setTextCustomerPhone() {
        final String customer_phone = getResources().getString(R.string.customer_phone);
        CommonUtils.setTextWithSpan(tvPhone, true, "业务咨询联系客服：", customer_phone,
                R.color.black, R.color.color_00adb2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.callPhone(mBaseContext, customer_phone);
                    }
                });
    }

    @Override
    public void setData() {

    }

    /**
     * 提交按钮是否可以点击的检测
     */
    public void commitButtonCanClick(){
        String feedbackContent = StringUtils.getTextFromView(etInput);
        if (StringUtils.isNull(feedbackContent)) {
            ToastUtils.showShortToast("请填写反馈内容");
            return;
        }
        List<ImageItem> imageItems = imageSelect.getSelectImages();
        if (CommonUtils.isNullOrEmpty(imageItems)) {
            ToastUtils.showShortToast("请添加截图");
            return;
        }
        File file = null;
        List<String> imagePathList = new ArrayList<>();
        if (CommonUtils.isNotNullOrEmpty(imageItems)) {
            for (ImageItem imageItem : imageItems) {
                imagePathList.add(imageItem.imagePath);
            }
//			file = new File(imageItems.get(0).imagePath);
//			upload(imageItems.get(0).imagePath);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imageSelect != null) {
            imageSelect.clearImageNotDeleteFile();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (imageSelect != null) {
            imageSelect.doResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
