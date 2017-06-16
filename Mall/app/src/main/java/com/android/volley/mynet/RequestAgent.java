package com.android.volley.mynet;

import android.app.Activity;

import com.android.volley.Request.Method;
import com.giveu.shoppingmall.utils.CrashReportUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RequestAgent extends BaseRequestAgent {
	/**
     * see{@link MyJsonRequest#encodeParameters} 如果我们希望map不被json格式化，map.put(NOT_FORMAT_TO_JSON, your json params str);
     */
//    public static final String NOT_FORMAT_TO_JSON = "NOT_FORMAT_TO_JSON";
    private static RequestAgent mRequestAgen = new RequestAgent();

    private RequestAgent() {
        super();
    }

    public static RequestAgent getInstance() {
        return mRequestAgen;
    }


    public void sendPostRequest(Map<String, Object> requestParams, String url, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(false, Method.POST, requestParams, url, resultClzz, loadingDialogContext, responseListener);
    }

    /**
     * 默认不需要加载框
     * @param requestParams
     * @param url
     * @param resultClzz
     * @param responseListener
     */
    public void sendPostRequest(Map<String, Object> requestParams, String url, Class resultClzz, final ResponseListener responseListener) {
        sendRequest(false, Method.POST, requestParams, url, resultClzz, null, responseListener);
    }

    /**
     * 普通请求
     *
     * @param isDecodeResponse     返回值是否需要解密
     * @param requestParams        string参数map集合
     * @param url                  完整的url地址
     * @param resultClzz           返回值类型
     * @param loadingDialogContext 是否需要loading框，null=不要
     * @param responseListener     返回值监听
     */
    public void sendPostRequest(boolean isDecodeResponse, Map<String, Object> requestParams, String url, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(isDecodeResponse, Method.POST, requestParams, url, resultClzz, loadingDialogContext, responseListener);
    }

    public void sendGetRequest(Map<String, String> requestParams, String url, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(false, Method.GET, string2Object(requestParams), url, resultClzz, loadingDialogContext, responseListener);
    }

    public void sendGetRequest(boolean isDecodeResponse, Map<String, String> requestParams, String url, Class resultClzz, Activity loadingDialogContext, final ResponseListener responseListener) {
        sendRequest(isDecodeResponse, Method.GET, string2Object(requestParams), url, resultClzz, loadingDialogContext, responseListener);
    }

    private void sendRequest(final boolean isDecodeResponse, final int requestMethod, final Map<String, Object> requestParams, final String url, final Class resultClzz, final Activity loadingDialogContext, final ResponseListener responseListener) {
        CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
            @Override
            public void onCatch() {
                VolleyResponseListener volleyResponseListener = new VolleyResponseListener(loadingDialogContext, responseListener);
                MyRequest myRequest = new MyJsonRequest(isDecodeResponse, requestMethod, url, resultClzz, volleyResponseListener, volleyResponseListener);
                volleyResponseListener.setMyRequest(myRequest);
                sendIt(myRequest, requestParams, url, loadingDialogContext);
            }
        });
    }

    public void sendFileRequest(Map<String, Object> requestParams, Map<String, File> fileParams, String url, Class resultClzz, Activity loadingDialogContext, ResponseListener responseListener) {
        sendFileRequest(false, requestParams, fileParams, url, resultClzz, loadingDialogContext, responseListener);
    }

    /**
     * 上传文件请求
     *
     * @param isDecodeResponse     返回值是否需要解密
     * @param requestParams        string参数map集合
     * @param url                  完整的url地址
     * @param resultClzz           返回值类型
     * @param loadingDialogContext 是否需要loading框，null=不要
     * @param responseListener     返回值监听
     */
    public void sendFileRequest(boolean isDecodeResponse, Map<String, Object> requestParams, Map<String, File> fileParams, String url, Class resultClzz, Activity loadingDialogContext, ResponseListener responseListener) {
        VolleyResponseListener volleyResponseListener = new VolleyResponseListener(loadingDialogContext, responseListener);
        FileRequest myRequest = new FileRequest(isDecodeResponse, url, fileParams, resultClzz, volleyResponseListener, volleyResponseListener);
        sendIt(myRequest, requestParams, url, loadingDialogContext);
    }

    @Override
    protected void sendIt(MyRequest myRequest, Map<String, Object> requestParams, String urlPlus, Activity loadingDialogContext) {
        super.sendIt(myRequest, requestParams, urlPlus, loadingDialogContext);
    }

    public static Map<String, String> object2String(Map<String, Object> map){
        Map<String, String> stringMap = new HashMap<>();
        if (map != null){
            Set<Map.Entry<String, Object>> entries = map.entrySet();
            for (Map.Entry<String, Object> entry : entries){
                Object valueObject = entry.getValue();
                if(valueObject instanceof String){
                    stringMap.put(entry.getKey(), (String) valueObject);
                }else{
                    throw new IllegalArgumentException("get请求，参数值不允许是object类型的");
                }
            }
        }

        return stringMap;
    }

    public static Map<String, Object> string2Object(Map<String, String> map){
        Map<String, Object> objectMap = new HashMap<>();
        if (map != null){
            Set<Map.Entry<String, String>> entries = map.entrySet();
            for (Map.Entry<String, String> entry : entries){
                objectMap.put(entry.getKey(), entry.getValue());
            }
        }

        return objectMap;
    }



}
