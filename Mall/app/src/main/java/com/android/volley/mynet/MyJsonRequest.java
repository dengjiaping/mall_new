package com.android.volley.mynet;

import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.MD5;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MyJsonRequest extends JsonRequest<BaseBean> {

    private int requestMethod;
    private Class resultClzz;
    private boolean isDecodeResponse = false;

    public MyJsonRequest(boolean isDecodeResponse, int requestMethod, String url, Class resultClzz, Listener<BaseBean> listener, ErrorListener errorListener) {
        super(requestMethod, url, listener, errorListener);

        this.isDecodeResponse = isDecodeResponse;
        this.requestMethod = requestMethod;
        this.resultClzz = resultClzz;
    }


    @Override
    protected Response<BaseBean> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = "";
//            if (isDecodeResponse) {
//                responseString = IOUtils.decrypt(response.data);
//            } else {
//                responseString = new String(response.data);
//            }
            responseString = new String(response.data);
            LogUtil.w(Const.LOG_TAG_HTTP, "地址：" + requestUrl + " 返回：" + responseString);

            BaseBean info = new BaseBean();
            BaseBeanParent baseBeanParent = (BaseBeanParent) new Gson().fromJson(responseString, BaseBeanParent.class);
            try{
                if (baseBeanParent != null){
                    if (baseBeanParent.isStatusSuccess()){
                        //status成功才解析
                        info = (BaseBean) new Gson().fromJson(responseString, resultClzz);
                    }else{
                        info = baseBeanParent.toBaseBean();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
                info.message = "app数据解析错误";
            }
            info.originResultString = responseString;

//            if (!isDecodeResponse && TextUtils.isEmpty(info.result)) {
//                info.result = VolleyErrorHelper.SUCCESS_STATUS;
//            }

            return Response.success(info, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error(new VolleyError(response));
    }

    /**
     * 设置getBodyContentType
     */
    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    /**
     * 设置参数格式为json字符串
     */
    public byte[] encodeParameters(Map<String, Object> params, String paramsEncoding) {
        StringBuilder encodedParams = new StringBuilder();
        try {
            String jsonParams = new Gson().toJson(params);
            encodedParams.append(jsonParams);

            LogUtil.w(Const.LOG_TAG_HTTP, "地址：" + requestUrl + " 参数：" + jsonParams);

            return encodedParams.toString().getBytes(paramsEncoding);
        } catch (Exception e) {
            throw new RuntimeException("Encoding not supported: " + paramsEncoding, e);
        }
    }


    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("token", "Authorization: Bearer " + SharePrefUtil.getAppToken());
        String jsonParams = new Gson().toJson(getParams());
        if(getParams().isEmpty()){
            jsonParams = "";
        }
        String md5Str = MD5.MD5Encode(jsonParams + "!$*6uOuop@^&%,.$*ci(hf_&]|");
        if ( !TextUtils.isEmpty(md5Str)){
            headerMap.put("sign", md5Str.toLowerCase());
        }
        headerMap.put("deviceId", SharePrefUtil.getUUId());
        LogUtil.w(Const.LOG_TAG_HTTP, "地址：" + requestUrl + " 头部参数：" + new Gson().toJson(headerMap));
        return headerMap;
    }



}
