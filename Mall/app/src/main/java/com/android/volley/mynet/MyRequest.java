package com.android.volley.mynet;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by 508632 on 2017/1/2.
 */

public abstract class MyRequest<T> extends Request<T> {
	protected Map<String, Object> mParams = new TreeMap<String, Object>();//这个请求的所有参数
	public String requestUrl;//这个请求的地址 http://test.pengsi.cn:7060/service/browser/v2_getuserfisrtconnection.jsp
	public int sendCount = 1;//同一个请求计数,当token失效时重新发送该请求时设置sendCount=2

	public MyRequest(int method, String url, Response.ErrorListener listener) {
		super(method, url, listener);
		requestUrl = url;
	}


	@Override
	public Map<String, Object> getParams() {
		return mParams;
	}

	public void setParams(Map<String, Object> mParams) {
		this.mParams = mParams;
	}


	/**
	 * 不缓存
	 */
	@Override
	public boolean shouldCache() {
		return false;
	}

	//是文件时重写，需要调用
	public void buildMultipartEntity() {

	}



}
