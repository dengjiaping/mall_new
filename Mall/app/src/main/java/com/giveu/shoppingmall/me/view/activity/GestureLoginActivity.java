package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.view.dialog.CustomDialogUtil;
import com.giveu.shoppingmall.view.lockpattern.LockPatternUtil;
import com.giveu.shoppingmall.view.lockpattern.LockPatternView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Sym on 2015/12/24.
 */
public class GestureLoginActivity extends BaseActivity {

    private static final String TAG = "LoginGestureActivity";

    @BindView(R.id.lockPatternView)
    LockPatternView lockPatternView;
    @BindView(R.id.tv_message)
    TextView messageTv;
    @BindView(R.id.tv_forget_gesture)
    TextView forgetGestureBtn;
    @BindView(R.id.tv_userName)
    TextView tv_userName;
    @BindView(R.id.rl_root)
    RelativeLayout rl_root;

    private static final long DELAYTIME = 600l;
    private String gesturePassword;
    boolean isClosePattern;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_gesture_login);
        isClosePattern = getIntent().getBooleanExtra("isClosePattern", false);

//        tv_userName.setText(LoginHelper.getInstance().getUserName());
        if (isClosePattern) {
            SpannableString titleText = StringUtils.getColorSpannable("", "关闭手势", R.color.color_4a4a4a, R.color.color_4a4a4a);
            baseLayout.setTitle(titleText);
            baseLayout.hideBack();
            baseLayout.setTopBarBackgroundColor(R.color.white);
            SpannableString cancleText = StringUtils.getColorSpannable("", "取消", R.color.color_00adb2, R.color.color_00adb2);
            baseLayout.setRightTextAndListener(cancleText, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            SpannableString titleText = StringUtils.getColorSpannable("", "解锁", R.color.color_4a4a4a, R.color.color_4a4a4a);
            baseLayout.setTitle(titleText);
            baseLayout.hideBack();
            baseLayout.setTopBarBackgroundColor(R.color.white);
        }

    }

    @Override
    public void setData() {
        //得到当前用户的手势密码
        gesturePassword = SharePrefUtil.getInstance().getPatternPwd();
        lockPatternView.setOnPatternListener(patternListener);
        updateStatus(Status.DEFAULT);
    }

    private LockPatternView.OnPatternListener patternListener = new LockPatternView.OnPatternListener() {

        @Override
        public void onPatternStart() {
            lockPatternView.removePostClearPatternRunnable();
        }

        @Override
        public void onPatternComplete(List<LockPatternView.Cell> pattern) {
            if (pattern != null) {
                if (LockPatternUtil.checkPattern(pattern, gesturePassword)) {
                    updateStatus(Status.CORRECT);
                } else {
                    updateStatus(Status.ERROR);
                }
            }
        }
    };

    /**
     * 更新状态
     *
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     * 手势登录成功（去首页）
     */
    private void loginGestureSuccess() {
        if (isClosePattern) {
            //告知设置界面
            setResult(RESULT_OK);
        }
        //解锁成功，重新开始计时
        BaseApplication.getInstance().setLastestStopMillis(System.currentTimeMillis());
        finish();
    }

    /**
     * 忘记手势密码（去账号登录界面）
     */
    @OnClick(R.id.tv_forget_gesture)
    void forgetGesturePassword() {
        VerifyPwdActivity.startIt(GestureLoginActivity.this, isClosePattern);
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        DEFAULT_GESTURE_FINGER(R.string.gesture_and_finger, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //指纹错误
        FINGER_ERROR(R.string.finger_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);
        //指纹错误

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }

    public static void startIt(Context mContext) {
        Intent intent = new Intent(mContext, GestureLoginActivity.class);
        mContext.startActivity(intent);
    }

    public static void startItForClosePattern(Activity mContext) {
        Intent intent = new Intent(mContext, GestureLoginActivity.class);
        intent.putExtra("isClosePattern", true);
        mContext.startActivityForResult(intent, 10);
    }

    // 返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && !isClosePattern) {
//            弹框确认退出应用
            final CustomDialogUtil customDialogUtil = new CustomDialogUtil(mBaseContext);
            customDialogUtil.getDialogMode1("提示", "应用已锁定，请输入手势密码解锁", "取消", "忘记密码", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerifyPwdActivity.startIt(GestureLoginActivity.this, isClosePattern);
                }
            }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK) {
            //使用登录密码解锁成功
            loginGestureSuccess();
        }
    }


}
