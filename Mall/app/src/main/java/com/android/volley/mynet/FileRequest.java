package com.android.volley.mynet;


import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by Administrator on 2015/8/31.
 */
public class FileRequest extends MyRequest<BaseBean> {

    private MultipartEntity entity = new MultipartEntity();
    public final Response.Listener<BaseBean> mListener;
    public boolean isDecodeResponse;
    public Object mFileParts;
    public Class resultClzz;


    /**
     * @param url
     * @param errorListener
     * @param listener
     * @param files
     */
    public FileRequest(boolean isDecodeResponse, String url, Object files, Class resultClzz, Response.Listener<BaseBean> listener, Response.ErrorListener errorListener
                       ) {

        super(Method.POST, url, errorListener);
        this.isDecodeResponse = isDecodeResponse;
        mListener = listener;
        mFileParts = files;
        this.resultClzz = resultClzz;
    }



    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<BaseBean> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = "";
//            if (isDecodeResponse) {
//                responseString = IOUtils.decrypt(response.data);
//            } else {
                responseString = new String(response.data);
//            }

            LogUtils.i("返回值=" + responseString);
            BaseBean info = (BaseBean) new Gson().fromJson(responseString, resultClzz);
            info.originResultString = responseString;

            if (!isDecodeResponse && TextUtils.isEmpty(info.result)) {
                info.result = VolleyErrorHelper.SUCCESS_STATUS;
            }

            return Response.success(info, HttpHeaderParser.parseCacheHeaders(response));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Response.error(new VolleyError(response));
    }

    @Override
    protected void deliverResponse(BaseBean response) {
        mListener.onResponse(response);
    }


}
