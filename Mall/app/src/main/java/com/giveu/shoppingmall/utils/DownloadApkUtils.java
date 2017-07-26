package com.giveu.shoppingmall.utils;

/**
 * Created by 101900 on 2017/2/20.
 */


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.AppUpdateDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * 自动下载更新apk服务
 * Create by: chenwei.li
 * Date: 2016-08-14
 * time: 09:50
 * Email: lichenwei.me@foxmail.com
 */
public class DownloadApkUtils {
    private Activity mActivity;
    private ProgressDialog downloadProgressDialog;
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    float downloadPercent;
    float apkTotalSize;
    boolean isForce;//false普通 true强制更新
    //apk本地保存地址
    String apkSavePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + BaseApplication.getInstance().getPackageName() + ".apk";
    AppUpdateDialog tipAlertDialog;
    ApkUgradeResponse apkUgradeResponse;


    public void showUpdateApkDialog(Activity activity, final ApkUgradeResponse response) {
        if (activity == null || response == null) {
            return;
        }

        mActivity = activity;
        try {
            File file = mActivity.getExternalCacheDir();
            if (file != null) {
                apkSavePath = file.getAbsolutePath()
                        + File.separator + "jiyouqianbao.apk";
            } else {
                apkSavePath = FileUtils.getSDcardPath() + File.separator + "jiyouqianbao.apk";
            }

        } catch (Exception e) {

        }
        apkUgradeResponse = response;
        isForce = response.isForceUpdate();

        final File file = new File(apkSavePath);
        tipAlertDialog = new AppUpdateDialog(mActivity);
        //如果已下载完成则弹安装Dialog
        if (file.isFile() && file.exists() && SharePrefUtil.getDownloadApkFlag()) {
            tipAlertDialog.initDialogContent(true, "");
            tipAlertDialog.setOnChooseListener(new AppUpdateDialog.OnChooseListener() {
                @Override
                public void cancel() {
                    onDilaogCancle(mActivity, isForce);
                }

                @Override
                public void confirm() {
                    Intent installApkIntent = getInstallApkIntent(apkSavePath);
                    mActivity.startActivity(installApkIntent);
                }
            });
        } else {
            //如果未下载完成则弹下载升级Dialog
            tipAlertDialog.initDialogContent(false, response.desc);
            tipAlertDialog.setOnChooseListener(new AppUpdateDialog.OnChooseListener() {
                @Override
                public void cancel() {
                    onDilaogCancle(mActivity, isForce);
                }

                @Override
                public void confirm() {
                    dismissTipDialog();

                    try {
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //自动更新apk
                    downloadAndInstall(response.url);
                }
            });
        }
        tipAlertDialog.setCancelable(false);
        tipAlertDialog.setCanceledOnTouchOutside(false);
        showTipDialog();
    }

    private void dismissTipDialog() {
        if (tipAlertDialog != null) {
            tipAlertDialog.dismiss();
        }
    }

    private void showTipDialog() {
        if (downloadProgressDialog != null && downloadProgressDialog.isShowing()) {
            return;
        }

        if (tipAlertDialog != null && !tipAlertDialog.isShowing()) {
            tipAlertDialog.show();
        }
    }

    private void onDilaogCancle(Activity mActivity, boolean isForce) {
        if (!isForce) {
            dismissTipDialog();
        } else {
            if (mActivity != null) {
                mActivity.finish();
            }
        }
    }

    //下载并安装方法
    public void downloadAndInstall(String url) {
        downloadProgressDialog = new ProgressDialog(mActivity);
        mNotificationManager = (NotificationManager) mActivity.getSystemService(Service.NOTIFICATION_SERVICE);
        HttpUtils httpUtils = new HttpUtils();
        downloadApk(httpUtils, url);
    }

    protected void initProgressDialog(long total, long current) {
        downloadProgressDialog.setTitle(mActivity.getResources().getString(R.string.app_name));//设置标题
        downloadProgressDialog.setMessage("正在更新中,请稍后...");//设置dialog内容
        downloadProgressDialog.setIcon(R.mipmap.ic_launcher);//设置图标，与为Title左侧
        downloadProgressDialog.setCancelable(false);//点击空白处不可取消
        downloadProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);// 水平线进度条,STYLE_SPINNER:圆形进度条
        downloadPercent = Float.parseFloat(current + "") / 1024 / 1024;
        apkTotalSize = ((int) total) / 1024 / 1024;
        downloadProgressDialog.setMax((int) total);//最大值
        downloadProgressDialog.setProgress((int) current);
        downloadProgressDialog.setProgressNumberFormat(String.format("%.2fM/%.2fM", downloadPercent, apkTotalSize));

        if (mActivity != null && !mActivity.isFinishing() && downloadProgressDialog != null) {
            downloadProgressDialog.show();
        }
    }

    //下载apk的方法
    private void downloadApk(HttpUtils httpUtils, String url) {
        httpUtils.download(url, apkSavePath, new RequestCallBack<File>() {
            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                if (mActivity != null && !mActivity.isFinishing() && downloadProgressDialog != null) {
                    downloadProgressDialog.dismiss();
                }

                Toast.makeText(mActivity, "下载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                ToastUtils.showShortToast("下载失败");
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                if (isForce) {
                    initProgressDialog(total, current);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(mActivity);//为了向下兼容，这里采用了v7包下的NotificationCompat来构造
                builder.setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(mActivity.getResources(), R.mipmap.ic_launcher)).setContentTitle(mActivity.getResources().getString(R.string.app_name));
                if (current > 0 && current < total) {
                    //下载进行中
                    builder.setProgress((int) total, (int) current, false);
                    SharePrefUtil.setDownloadApkFlag(false);
                } else {
                    SharePrefUtil.setDownloadApkFlag(false);
                    builder.setProgress(0, 0, false);
                }
                builder.setAutoCancel(true);
                builder.setWhen(System.currentTimeMillis());
                builder.setContentText("正在更新中...");

                if (current >= total) {
                    //下载完成
                    SharePrefUtil.setDownloadApkFlag(true);
                    builder.setContentText("更新完成");

                    Intent installApkIntent = getInstallApkIntent(apkSavePath);
                    mActivity.startActivity(installApkIntent);

                    PendingIntent pendingIntent = PendingIntent.getActivity(mActivity, 0, installApkIntent, 0);
                    builder.setContentIntent(pendingIntent);
                }
                mNotification = builder.build();
                mNotificationManager.notify(0, mNotification);
            }
        });
    }

    private Intent getInstallApkIntent(String apkPath) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        Uri data;
        //适配7.0文件共享
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(mActivity, "com.giveu.shoppingmall.fileprovider",new File(apkPath));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            data = Uri.parse("file://" + apkPath);
        }

        intent.setDataAndType(data, "application/vnd.android.package-archive");
        return intent;
    }

    public void onActivityResume() {
        if (isForce) {
            showUpdateApkDialog(mActivity, apkUgradeResponse);
        }
    }


}
