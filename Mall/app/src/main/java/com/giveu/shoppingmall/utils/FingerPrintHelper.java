package com.giveu.shoppingmall.utils;

import android.app.Activity;

import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

/**
 * Created by 513419 on 2017/7/7.
 * 指纹解锁的帮助类
 */

public class FingerPrintHelper {
    private FingerprintIdentify mFingerprintIdentify;
    private BaseFingerprint.FingerprintIdentifyListener identifyListener;
    private Activity mBaseContext;

    public FingerPrintHelper(Activity mBaseContext) {
        try {
            mFingerprintIdentify = new FingerprintIdentify(mBaseContext, null);
        } catch (Exception e) {
        }
        this.mBaseContext = mBaseContext;
    }

    /**
     * 指纹解锁是否可用，硬件可用但是未设置指纹
     *
     * @return
     */
    public boolean isFingerprintEnable() {
        boolean isFingerPrintEnable = false;
        try {
            isFingerPrintEnable = mFingerprintIdentify.isFingerprintEnable();
        } catch (Exception e) {
        } finally {
            return isFingerPrintEnable;
        }
    }

    /**
     * 指纹解锁硬件是否可用
     *
     * @return
     */
    public boolean isHardwareEnable() {
        boolean isHardwareEnable = false;
        try {
            isHardwareEnable = mFingerprintIdentify.isHardwareEnable();
        } catch (Exception e) {
        } finally {
            return isHardwareEnable;
        }
    }

    /**
     * 开始指纹识别
     */
    public void startIdentify() {
        try {
            mFingerprintIdentify.startIdentify(10, identifyListener);
        } catch (Exception e) {
        }
    }

    /**
     * 暂停指纹识别
     */
    public void onPauseIdentify() {
        try {
            mFingerprintIdentify.cancelIdentify();
        } catch (Exception e) {
        }
    }

    /**
     * 恢复指纹识别
     */
    public void onResumeIdentify() {
        try {
            mFingerprintIdentify = new FingerprintIdentify(mBaseContext, null);
        } catch (Exception e) {
        }
    }

    public interface OnFingerMathchListener {
        void onSuccess();

        void onFailed();

        void onNotMatch();
    }

    private OnFingerMathchListener onFingerMathchListener;

    /**
     * 指纹识别回调
     *
     * @param listener
     */
    public void setOnFingerMathchListener(OnFingerMathchListener listener) {
        this.onFingerMathchListener = listener;
        identifyListener = new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                if (onFingerMathchListener != null) {
                    onFingerMathchListener.onSuccess();
                }
            }

            @Override
            public void onNotMatch(int availableTimes) {
                if (onFingerMathchListener != null) {
                    onFingerMathchListener.onNotMatch();
                }
            }

            @Override
            public void onFailed() {
                if (onFingerMathchListener != null) {
                    onFingerMathchListener.onFailed();
                }
            }
        };
    }
}
