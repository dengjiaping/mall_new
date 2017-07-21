package com.giveu.shoppingmall.me.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BasePermissionActivity;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.me.presenter.ProblemFeedBackPresenter;
import com.giveu.shoppingmall.me.view.agent.IProblemFeedBackView;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.FileUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.listener.TextChangeListener;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.dialog.ConfirmDialog;
import com.giveu.shoppingmall.widget.dialog.NormalHintDialog;
import com.giveu.shoppingmall.widget.dialog.PermissionDialog;
import com.giveu.shoppingmall.widget.imageselect.ImageItem;
import com.giveu.shoppingmall.widget.imageselect.ImagesSelectView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 问题反馈页面
 * Created by 101900 on 2017/6/23.
 */

public class ProblemFeedbackActivity extends BasePermissionActivity implements IProblemFeedBackView {


    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.image_select)
    ImagesSelectView imageSelect;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_commit)
    ClickEnabledTextView tvCommit;
    private ProblemFeedBackPresenter presenter;
    private ArrayList<String> uploadList;
    private PermissionDialog permissionDialog;
    private boolean needShow;


    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, ProblemFeedbackActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_problem_feedback);
        baseLayout.setTitle("问题反馈");
        //先申请存储权限，存储权限获取后才去申请相机权限
        setPermissionHelper(true, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        setTextCustomerPhone();
        baseLayout.setRightTextColor(R.color.title_color);
        baseLayout.setRightTextAndListener("记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackListActivity.startIt(mBaseContext);
            }
        });
        presenter = new ProblemFeedBackPresenter(this);
        uploadList = new ArrayList<>();
        //创建图片缓存目录
        FileUtils.getDirFile(FileUtils.TEMP_IMAGE);
        permissionDialog = new PermissionDialog(mBaseContext);
        permissionDialog.setConfirmStr("去开启");
        permissionDialog.setOnChooseListener(new ConfirmDialog.OnChooseListener() {
            @Override
            public void confirm() {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                permissionDialog.dismiss();
                finish();
            }

            @Override
            public void cancle() {
                permissionDialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onPermissionGranted(@NonNull String[] permissionName) {
        super.onPermissionGranted(permissionName);
        for (String s : permissionName) {
            if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(s)) {
                setPermissionHelper(true, new String[]{Manifest.permission.CAMERA});
            }
        }
    }

    @Override
    public void onPermissionReallyDeclined(@NonNull String permissionName) {
        super.onPermissionReallyDeclined(permissionName);
        String permissionStr = "";
        if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permissionName)) {
            permissionStr = "存储";

        } else if (Manifest.permission.CAMERA.equals(permissionName)) {
            permissionStr = "相机";
        }
        permissionDialog.setPermissionStr("问题反馈需要" + permissionStr + "权限才可正常使用");
        permissionDialog.show();
    }

    @Override
    protected BasePresenter[] initPresenters() {
        return new BasePresenter[]{presenter};
    }

    private void setTextCustomerPhone() {
        final String customer_phone = "4008075546";
        CommonUtils.setTextWithSpan(tvPhone, true, "业务咨询联系客服：", customer_phone,
                R.color.black, R.color.color_00adb2, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CommonUtils.callPhone(mBaseContext, customer_phone);
                    }
                });
    }

    @Override
    public void setListener() {
        super.setListener();
        etInput.addTextChangedListener(new TextChangeListener() {
            @Override
            public void afterTextChanged(Editable s) {
                commitButtonCanClick(false);
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvCommit.isClickEnabled()) {
                    final List<ImageItem> imageItems = imageSelect.getSelectImages();
                    if (CommonUtils.isNotNullOrEmpty(imageItems)) {
                        uploadList.clear();
                        uploadPhoto(imageItems);
                    }
                } else {
                    commitButtonCanClick(true);
                }
            }
        });
        imageSelect.setOnCheckListener(new ImagesSelectView.OnCheckListener() {
            @Override
            public boolean onCheck() {
                //删除图片后判断是否可点击提交
                commitButtonCanClick(false);
                return false;
            }
        });
    }

    private void uploadPhoto(final List<ImageItem> imageItems) {
        //开启子线程压缩图片，压缩完成后上传图片
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String photoPath;
                for (ImageItem imageItem : imageItems) {
                    photoPath = FileUtils.TEMP_IMAGE + "/giveU_" + System.currentTimeMillis() + ".jpg";
                    //压缩图片并保存至缓存目录，上传成功或失败都会删除，在presenter进行删除
                    FileUtils.saveBitmapWithPath(ImageUtils.decodeScaleImage(imageItem.imagePath), photoPath);
                    uploadList.add(photoPath);
                }
                if (presenter != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //上传反馈
                            presenter.addQuestionMessage("files", uploadList, 2, etInput.getText().toString(), LoginHelper.getInstance().getIdent(),
                                    LoginHelper.getInstance().getName(), LoginHelper.getInstance().getUserName(), LoginHelper.getInstance().getPhone(),
                                    LoginHelper.getInstance().getUserId());
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void setData() {

    }

    /**
     * 提交按钮是否可以点击的检测
     */
    public void commitButtonCanClick(boolean showToast) {
        tvCommit.setClickEnabled(false);
        String feedbackContent = StringUtils.getTextFromView(etInput);
        if (StringUtils.isNull(feedbackContent)) {
            if (showToast) {
                ToastUtils.showShortToast("请填写反馈内容");
            }
            return;
        }
        List<ImageItem> imageItems = imageSelect.getSelectImages();
        if (CommonUtils.isNullOrEmpty(imageItems)) {
            if (showToast) {
                ToastUtils.showShortToast("请添加截图");
            }
            return;
        }
        tvCommit.setClickEnabled(true);
//			file = new File(imageItems.get(0).imagePath);
//			upload(imageItems.get(0).imagePath);
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
        commitButtonCanClick(false);
    }

    @Override
    public void uploadSuccess() {
        NormalHintDialog dialog = new NormalHintDialog(mBaseContext, "提交成功！\n我们将在3个工作日内处理您的反馈！");
        dialog.showDialog();
        dialog.setOnDialogDismissListener(new NormalHintDialog.OnDialogDismissListener() {
            @Override
            public void onDismiss() {
                finish();
            }
        });
    }
}
