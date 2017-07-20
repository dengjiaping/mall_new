package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * create gesture activity
 * Created by Sym on 2015/12/23.
 */
public class CreateGestureActivity extends BaseActivity {

    @BindView(R.id.lockPatternView)
    PatternLockView lockPatternView;
    @BindView(R.id.resetBtn)
    TextView resetBtn;
    @BindView(R.id.tv_message)
    TextView messageTv;
    private List<PatternLockView.Dot> mChosenPattern = null;
    private static final long DELAYTIME = 600L;
    private static final String TAG = "CreateGestureActivity";
    public static final int REQUEST_FINISH = 10000;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, CreateGestureActivity.class);
        activity.startActivityForResult(intent, REQUEST_FINISH);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_gesture);
        baseLayout.setTitle("设置");
        baseLayout.hideBack();
        SpannableString cancleText = StringUtils.getColorSpannable("", "取消", R.color.color_00adb2, R.color.color_00adb2);
        baseLayout.setRightTextAndListener(cancleText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lockPatternView.addPatternLockListener(patternListener);
    }


    @Override
    public void setData() {

    }

    /**
     * 手势监听
     */
    private PatternLockViewListener patternListener = new PatternLockViewListener() {

        @Override
        public void onStarted() {

        }

        @Override
        public void onProgress(List<PatternLockView.Dot> progressPattern) {

        }

        @Override
        public void onComplete(List<PatternLockView.Dot> pattern) {
            if (mChosenPattern == null && pattern.size() >= 4) {
                mChosenPattern = new ArrayList<>(pattern);
                updateStatus(Status.CORRECT, pattern);
            } else if (mChosenPattern == null && pattern.size() < 4) {
                updateStatus(Status.LESSERROR, pattern);
            } else if (mChosenPattern != null) {
                if (mChosenPattern.equals(pattern)) {
                    updateStatus(Status.CONFIRMCORRECT, pattern);
                } else {
                    updateStatus(Status.CONFIRMERROR, pattern);
                }
            }
        }

        @Override
        public void onCleared() {

        }
    };

    /**
     * 更新状态
     *
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<PatternLockView.Dot> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                break;
            case CORRECT:
                lockPatternView.setViewMode(PatternLockView.PatternViewMode.CORRECT);
                lockPatternView.clearPattern();
                break;
            case LESSERROR:
                lockPatternView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                lockPatternView.clearPattern();
                break;
            case CONFIRMERROR:
                lockPatternView.setViewMode(PatternLockView.PatternViewMode.WRONG);
                lockPatternView.clearPattern();
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                setLockPatternSuccess();
                lockPatternView.clearPattern();
                break;
        }
    }

    /**
     * 重新设置手势
     */
    @OnClick(R.id.resetBtn)
    void resetLockPattern() {
        mChosenPattern = null;
        updateStatus(Status.DEFAULT, null);
        lockPatternView.clearPattern();
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        ToastUtils.showShortToast("手势密码设置成功");
        setResult(RESULT_OK);
        finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<PatternLockView.Dot> cells) {
        SharePrefUtil.setPatternPwd(PatternLockUtils.patternToString(lockPatternView, cells));
    }


    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }

        private int strId;
        private int colorId;
    }
}
