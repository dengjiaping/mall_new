package com.giveu.shoppingmall.index.presenter;

import android.text.TextUtils;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.base.BasePresenter;
import com.giveu.shoppingmall.base.IView;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.utils.FileUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

/**
 * Created by 513419 on 2017/9/8.
 */

public class SplashAdPresenter extends BasePresenter<IView> {
    public SplashAdPresenter(IView view) {
        super(view);
    }

    public void getAdSplashImage() {
        ApiImpl.AdSplashImage(new BaseRequestAgent.ResponseListener<AdSplashResponse>() {
            @Override
            public void onSuccess(AdSplashResponse response) {
                AdSplashResponse data = response.data;
                if (data != null) {
                    if (data.enableAd()){
                        //需要广告
                        final String imageUrl = data.imgUrl;
                        if (StringUtils.isNotNull(imageUrl)) {
                            if (SharePrefUtil.getAdSplashImage() != null && SharePrefUtil.getAdSplashImage().adId == data.adId && isAdImageFileAtSdcard()) {
                                //新广告id与上一次广告id相同，不重新保存广告
                                return;
                            }
                            //保存最新广告接口的返回值
                            SharePrefUtil.setAdSplashImage(data);
                            FileUtils.deleteAllFile(FileUtils.getSplashAdImageDirFile());

                            final String localPath = FileUtils.getSplashAdImageDirFile() + File.separator + System.currentTimeMillis() + "_AdImg.jpg";
                            HttpUtils httpUtils = new HttpUtils();
                            httpUtils.download(imageUrl, localPath, new RequestCallBack<File>() {
                                @Override
                                public void onSuccess(ResponseInfo<File> responseInfo) {
                                    SharePrefUtil.setAdSplashImageSdcardPath(localPath);
                                }

                                @Override
                                public void onFailure(HttpException e, String s) { }

                                @Override
                                public void onLoading(long total, long current, boolean isUploading) {
                                    super.onLoading(total, current, isUploading);
                                }
                            });
                        }
                    }else{
                        //不需要广告
                        SharePrefUtil.setAdSplashImage(null);
                        FileUtils.deleteAllFile(FileUtils.getSplashAdImageDirFile());
                    }
                }
            }

            @Override
            public void onError(BaseBean errorBean) { }
        });
    }

    //判断文件是否存在
    public static boolean isAdImageFileAtSdcard() {
        try {
            String strFile = SharePrefUtil.getAdSplashImageSdcardPath();
            if (TextUtils.isEmpty(strFile)){
                return false;
            }

            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }




}
