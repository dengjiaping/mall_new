package com.android.volley.mynet;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.CrashReportUtil;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 508632 on 2017/2/4.
 */

public class FileUpload {
    /**
     * 上传图片到第三方api， 获得返回值后取{@link BaseBeanParent#originResultString}字段，自己gson转化。其他字段无效
     *
     * @param fileKey          图片key
     * @param filePaths        图片地址集合
     * @param mParams          非图片参数map
     * @param requestUrl       api地址
     * @param responseListener 返回值监听
     */
    public static void uploadFileForThirdPlatformApi(String fileKey, List<String> filePaths, Map<String, Object> mParams, final String requestUrl, Class clzz, final BaseRequestAgent.ResponseListener responseListener) {
        List<String> keys = new ArrayList<>();
        keys.add(fileKey);
        List<List<String>> filePathList = new ArrayList<>();
        filePathList.add(filePaths);
        uploadFile(true, keys, filePathList, mParams, requestUrl, clzz, responseListener);
    }

    /**
     * 支持多个key上传图片
     *
     * @param keys
     * @param filePaths
     * @param mParams
     * @param requestUrl
     * @param responseListener
     */
    public static void uploadFileForThirdPlatformApi(List<String> keys, List<List<String>> filePaths, Map<String, Object> mParams, final String requestUrl, Class clzz, final BaseRequestAgent.ResponseListener responseListener) {

        if (keys.size() != filePaths.size()) {
            ToastUtils.showShortToast("图片key与图片路径长度不一致，请检查代码");
            return;
        }
        uploadFile(true, keys, filePaths, mParams, requestUrl, clzz, responseListener);
    }

    public static void uploadFileForOwnPlatformApi(List<String> keys, List<List<String>> filePaths, Map<String, Object> mParams, final String requestUrl, Class clzz, final BaseRequestAgent.ResponseListener responseListener) {
        if (keys.size() != filePaths.size()) {
            ToastUtils.showShortToast("图片key与图片路径长度不一致，请检查代码");
            return;
        }
        uploadFile(false, keys, filePaths, mParams, requestUrl, clzz, responseListener);
    }

    /**
     * 上传图片到自己服务器
     *
     * @param fileKey          图片key
     * @param filePaths        图片地址集合
     * @param mParams          非图片参数map
     * @param requestUrl       api地址
     * @param clzz             返回值用json格式化的模型类
     * @param responseListener 返回值监听  {@link BaseBeanParent}所有字段有效
     */
    public static void uploadFileForOwnPlatformApi(String fileKey, List<String> filePaths, Map<String, Object> mParams, final String requestUrl, final Class clzz, final BaseRequestAgent.ResponseListener responseListener) {
        List<String> keys = new ArrayList<>();
        keys.add(fileKey);
        List<List<String>> filePathList = new ArrayList<>();
        if (CommonUtils.isNotNullOrEmpty(filePaths)) {
            filePathList.add(filePaths);
        }
        uploadFile(false, keys, filePathList, mParams, requestUrl, clzz, responseListener);
    }

    private static void uploadFile(final boolean isThirdPlatformApi, final List<String> keys, final List<List<String>> filePaths, final Map<String, Object> mParams, final String requestUrl, final Class clzz, final BaseRequestAgent.ResponseListener responseListener) {
        CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
            @Override
            public void onCatch() {
                uploadFileByOkhttp(isThirdPlatformApi, keys, filePaths, mParams, requestUrl, clzz, responseListener);
            }
        });
    }

    private static void uploadFileByOkhttp(final boolean isThirdPlatformApi, final List<String> keys, final List<List<String>> filePaths, final Map<String, Object> params, final String requestUrl, final Class clzz, final BaseRequestAgent.ResponseListener responseListener) {
        //文本参数，增加公共参数

        /* form的分割线,自己定义 */
        final String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        MultipartBody.Builder builder = new MultipartBody.Builder(boundary).setType(MultipartBody.FORM);

        for (Map.Entry<String, Object> entry : params.entrySet()) {
                /* 上传一个普通的String参数 , key 叫 "p" */
            builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
        }
                /* 底下是上传了两个文件 */
        for (int i = 0; i < filePaths.size(); i++) {
            List<String> path = filePaths.get(i);
            if (path != null) {
                for (int j = 0; j < path.size(); j++) {
                    RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), new File(path.get(j)));
                    builder.addFormDataPart(keys.get(i), "file1Name" + j, fileBody);
                }
            }
        }

        //打印参数
        logParams(isThirdPlatformApi, keys, filePaths, params, requestUrl);

        MultipartBody mBody = builder.build();
        /* 下边的就和post一样了 */
        Request request = new Request.Builder()
                .url(requestUrl)
                .post(mBody)
                .addHeader("content-billType", "multipart/form-data; boundary=" + boundary)
                .addHeader("cache-control", "no-cache")
                .build();
        OkHttpClient client = new okhttp3.OkHttpClient.Builder().
                connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        client.newCall(request).enqueue(new Callback() {
            public void onResponse(Call call, final okhttp3.Response response) throws IOException {
                final String responseString = response.body().string();
                final boolean ok = response.isSuccessful();
                LogUtil.w(Const.LOG_TAG_HTTP, "地址：" + requestUrl + " 返回：" + responseString);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if (ok) {//http协议返回200
                            if (isThirdPlatformApi) {
                                dealThirdPlatformApiSuccess(responseListener, responseString);
                            } else {
                                dealMyPlatformApiSuccess(responseListener, isThirdPlatformApi, keys, filePaths, params, requestUrl, responseString, clzz);
                            }
                        } else {
                            dealFail(responseListener, isThirdPlatformApi, keys, filePaths, params, requestUrl, responseString);
                        }
                    }
                });
            }

            public void onFailure(Call call, final IOException e) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        dealIOException(responseListener, isThirdPlatformApi, keys, filePaths, params, requestUrl, e);
                    }
                });

            }
        });
    }

    private static void dealFail(BaseRequestAgent.ResponseListener responseListener, boolean isThirdPlatformApi, List<String> keys, List<List<String>> filePaths, Map<String, Object> mStrParams, String requestUrl, String responseString) {
        CrashReportUtil.postApiErrorToBugly(isThirdPlatformApi, keys, filePaths, mStrParams, requestUrl, responseString);

        if (responseListener != null) {
            BaseBean baseBean = new BaseBean();
            baseBean.originResultString = responseString;
            responseListener.onError(baseBean);
        }
    }

    private static void dealMyPlatformApiSuccess(final BaseRequestAgent.ResponseListener responseListener, final boolean isThirdPlatformApi, final List<String> keys, final List<List<String>> filePaths, final Map<String, Object> mStrParams, final String requestUrl, final String responseString, final Class clzz) {
        CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
            @Override
            public void onCatch() {
                BaseBeanParent baseBeanParent = (BaseBeanParent) new Gson().fromJson(responseString, BaseBeanParent.class);
                baseBeanParent.originResultString = responseString;
                if (baseBeanParent.isStatusSuccess()) {
                    if (responseListener != null) {
                        BaseBean mBean = (BaseBean) new Gson().fromJson(responseString, clzz);
                        responseListener.onSuccess(mBean);
                    }
                } else {
                    //上报接口错误
                    CrashReportUtil.postApiErrorToBugly(isThirdPlatformApi, keys, filePaths, mStrParams, requestUrl, responseString);

                    if (baseBeanParent.isLogoutByServer() && (!ApiUrl.personCenter_account_login.equals(requestUrl))) {
                        //除登录接口外，其他接口响应被服务器强制退出登录的code
//                        LoginActivity.logoutByServerAndStartIt(baseBeanParent.message);
                    } else if (responseListener != null) {
                        responseListener.onError(baseBeanParent.toBaseBean());
                    }
                }
            }
        });
    }

    private static void dealThirdPlatformApiSuccess(BaseRequestAgent.ResponseListener responseListener, String responseString) {
        if (responseListener != null) {
            BaseBean baseBean = new BaseBean();
            baseBean.originResultString = responseString;
            responseListener.onSuccess(baseBean);
        }
    }

    private static void dealIOException(BaseRequestAgent.ResponseListener responseListener, boolean isThirdPlatformApi, List<String> keys, List<List<String>> filePaths, Map<String, Object> mStrParams, String requestUrl, IOException e) {
        e.printStackTrace();
        CrashReportUtil.postApiErrorToBugly(isThirdPlatformApi, keys, filePaths, mStrParams, requestUrl, e.getMessage());

        if (responseListener != null) {
            BaseBean baseBean = new BaseBean();
            baseBean.result = BaseBean.STATUS.FAIL;
            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                baseBean.message = e.getMessage();
            }

            responseListener.onError(baseBean);
        }
    }

    private static void logParams(boolean isThirdPlatformApi, List<String> keys, List<List<String>> filePaths, Map<String, Object> mStrParams, String requestUrl) {
        try {
            Gson gson = new Gson();
            Map<String, Object> map = new HashMap<>();
            map.put("是否第三方", isThirdPlatformApi);
            map.put("地址", requestUrl);
            map.put("文本参数", mStrParams);
            map.put("图片key", keys);
            map.put("图片地址", filePaths);
            String json = gson.toJson(map);

            LogUtil.w(Const.LOG_TAG_HTTP, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
