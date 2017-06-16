package com.android.volley.mynet;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.giveu.shoppingmall.view.dialog.LoadingDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.lidroid.xutils.util.LogUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @deprecated use {@link RequestAgent sendfilerequest()}
 */
public class FileRequestAgent extends BaseRequestAgent{
    private static FileRequestAgent mFileRequestAgen = new FileRequestAgent();

    public FileRequestAgent() {
        super();
    }

    public static FileRequestAgent getInstance() {
        return mFileRequestAgen;
    }

    private void addRequest(boolean isDecodeResponse, Map<String, File> fileParams, Map<String, String> params, String urlPlus, Class resultClzz, Listener<BaseBean> listener, ErrorListener errorListener) {
//        CommonUtils.addMoreParams(params);
        LogUtils.i("请求路径：" + urlPlus);
        if (params != null) {
            for (Map.Entry<String, String> me : params.entrySet()) {
                LogUtils.i("参数：" + me.getKey() + "    值：" + me.getValue());
            }
        }

        FileRequest request = new FileRequest(isDecodeResponse, urlPlus, fileParams, resultClzz, listener, errorListener);


        if (!TextUtils.isEmpty(urlPlus)) {
            request.setTag(urlPlus);
        }
        mRequestQueue.add(request);
    }


    public static void sendPostRequest(Map<String, File> FileParams, Map<String, String> stringParams, String urlPlus, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(true, FileParams, stringParams, urlPlus, resultClzz, loadingDialogContext, responseListener);
    }

    public static void sendPostRequest(boolean isDecodeResponse, Map<String, File> fileParams, Map<String, String> stringParams, String urlPlus, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(isDecodeResponse, fileParams, stringParams, urlPlus, resultClzz, loadingDialogContext, responseListener);
    }


    private static void sendRequest(boolean isDecodeResponse, Map<String, File> fileParams, Map<String, String> stringParams, String urlPlus, Class resultClzz, final Activity loadingDialogContext, final ResponseListener responseListener) {
        if (loadingDialogContext != null) {
            LoadingDialog.showIfNotExist(loadingDialogContext, false);
        }

        Map<String, String> mStringParams = new HashMap<String, String>();


        if(!CommonUtils.isNullOrEmpty(stringParams)){
            for (Map.Entry<String, String> entry : stringParams.entrySet()) {
                if (entry.getKey() == null) {
                    LogUtils.e("sendRequest的requestParams中key不能为null");
                    return;
                }
                if (entry.getValue() == null) {
                    mStringParams.put(entry.getKey(), "");
                } else {
                    mStringParams.put(entry.getKey(), entry.getValue());
                }
            }
        }

        mFileRequestAgen.addRequest(isDecodeResponse, fileParams, mStringParams, urlPlus, resultClzz, new Listener<BaseBean>() {

            @Override
            public void onResponse(BaseBean response) {
                if (loadingDialogContext != null) {
                    LoadingDialog.dismissIfExist();
                }
                if (responseListener != null && responseListener instanceof ExpandResponseListener) {
                    ((ExpandResponseListener) responseListener).beforeSuccessAndError();
                }

                if (response != null) {
                    if ("0".equals(response.status)) {
                        if (responseListener != null) {
                            responseListener.onSuccess(response);
                        }
                    } else {
                        if (responseListener != null) {
                            responseListener.onError(response);
                        }
                    }
                }
            }
        }, new ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (loadingDialogContext != null) {
                    LoadingDialog.dismissIfExist();
                }
                if (responseListener != null && responseListener instanceof ExpandResponseListener) {
                    ((ExpandResponseListener) responseListener).beforeSuccessAndError();
                }

                if (responseListener != null && error != null) {
                    BaseBean errorBean = new BaseBean();
                    errorBean.status = VolleyErrorHelper.getVolleyErrorCode(error) + "";
                    errorBean.message = error.getMessage();
                    if (VolleyErrorHelper.getVolleyErrorCode(error) == 200) {
                        errorBean.message = "数据解析错误";
                    }

                    responseListener.onError(errorBean);
                }
            }
        });
    }



}
