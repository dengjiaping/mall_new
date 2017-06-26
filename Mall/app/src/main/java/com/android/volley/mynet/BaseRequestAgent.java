package com.android.volley.mynet;

import android.app.Activity;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.BaseListener;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.TokenBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.CrashReportUtil;
import com.giveu.shoppingmall.utils.NetWorkUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;
import com.giveu.shoppingmall.widget.dialog.LoadingDialog;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by 508632 on 2017/1/2.
 */

public abstract class BaseRequestAgent {
	public RequestQueue mRequestQueue;

	public interface ResponseListener<T extends BaseBean> extends BaseListener{
		void onSuccess(T response);

		void onError(BaseBean errorBean);
	}

	public interface ExpandResponseListener<T extends BaseBean> extends ResponseListener<T> {
		void beforeSuccessAndError();
	}



	protected BaseRequestAgent(){
		if (mRequestQueue == null){
			mRequestQueue = Volley.newRequestQueue(BaseApplication.getInstance(), new OkHttpStack());
		}
	}


	/**
	 * @param keys
	 * @param values
	 * @return keys == null时，返回new HashMap<String, String>();
	 */
	public static Map<String, Object> getRequestParamsObject(String[] keys, String[] values) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				if (values[i] != null) {
					params.put(keys[i], values[i]);
				}
			}
		}

		return params;
	}

	public static Map<String, String> getRequestParams(String[] keys, String[] values) {
		Map<String, String> params = new HashMap<String, String>();
		if (keys != null) {
			for (int i = 0; i < keys.length; i++) {
				if (values[i] != null) {
					params.put(keys[i], values[i]);
				}
			}
		}

		return params;
	}

	/**
	 * 取消发出的请求
	 *
	 * @param urlPlus 就是sendPostRequest(..,String urlPlus,..)中的urlPlus;
	 */
	public void cancelRequest(Object urlPlus) {
		if (mRequestQueue != null && urlPlus != null) {
			mRequestQueue.cancelAll(urlPlus);
		}
	}


	/**
	 * 是否加密请求参数
	 * @return
	 */
	protected boolean isParamsNeedEncode(String url) {
//        if ( !TextUtils.isEmpty(url) && url.contains(ApiUrl.DEV_BASE_URL) && (url.contains("/a/n/") || url.contains("/a/l/")) ){
//            return true;
//        }
		return false;
	}

	protected Map<String, Object> filterEmptyParamValue(Map<String, Object> params) {
		HashMap params2 = new HashMap<String, Object>();
		if (params != null){
			for (String key : params.keySet()) {
				Object valueOb = params.get(key);
				if (valueOb instanceof String){
					String valueStr = (String) valueOb;
					if (!TextUtils.isEmpty(valueStr)) {//应服务器需要如果参数值为"",null，则不传
						params2.put(key, valueStr);
					}
				}else{
					if (valueOb != null) {//应服务器需要如果参数值为"",null，则不传
						params2.put(key, valueOb);
					}
				}
			}
		}
		return params2;
	}

	/**
	 * 对每个发出去的请求添加公有参数
	 * @return 新的参数map
	 */
	public static Map<String, Object> addMoreParams(Map<String, Object> params) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}

		params.put("VERSION_CODE", CommonUtils.getVersionCode());
		params.put("VERSION_NAME", CommonUtils.getVersionName());
		params.put("DEVICE_TYPE", "android");//1:IOS :2ANDROID

		String tokenKey = "token";
		if (StringUtils.isNull((String) params.get(tokenKey))) {
			params.put(tokenKey, SharePrefUtil.getAppToken());
		}
		//所有的接口加一个salesId
		String salesIdKey = "salesId";
//		if (LoginHelper.getInstance().hasLogin() && StringUtils.isNull((String) params.get(salesIdKey))) {
//			params.put(salesIdKey, LoginHelper.getInstance().getUserId());
//		}
		//设备唯一标识
		String identificationKey = "identification";
		if (StringUtils.isNull((String) params.get(identificationKey))) {
			params.put( identificationKey, SharePrefUtil.getUUId());
		}
//        int networkType = NetWorkUtils.getCurrentNetworkType();
//        if (networkType != -1) {
//            params.put("NETWORK_TYPE", String.valueOf(networkType));
//        }

		return params;
	}

	private void addRequest(MyRequest request, Map<String, Object> params, String urlPlus) {
		setRequestParam(urlPlus, params, request);
		if (!TextUtils.isEmpty(urlPlus)) {
			request.setTag(urlPlus);
		}

		request.buildMultipartEntity();
		mRequestQueue.add(request);
	}



	private void setRequestParam(String url, Map<String, Object> params, MyRequest request) {
		if (params == null) {
			params = new HashMap<String, Object>();
		}
		params = addMoreParams(params);
		params = filterEmptyParamValue(params);

		if (isParamsNeedEncode(url)){//需要加密
			Map encryptParams = new HashMap<String, String>();
			Gson gson = new Gson();
			String paramStr = gson.toJson(params);

			encryptParams.put("VERSION_NAME", CommonUtils.getVersionName());
			encryptParams.put("VERSION_CODE", CommonUtils.getVersionCode());
			encryptParams.put("inputParams", paramStr);

			request.setParams(encryptParams);
		}else {//不要加密
			request.setParams(params);
		}
	}

	static class VolleyResponseListener implements Response.Listener<BaseBean>, Response.ErrorListener{
		SoftReference<Activity> mSoftActivity;
		ResponseListener responseListener;
		MyRequest myRequest;

		public void setMyRequest(MyRequest myRequest) {
			this.myRequest = myRequest;
		}

		public VolleyResponseListener(final Activity activity, final ResponseListener responseListener){
			this.mSoftActivity = new SoftReference<Activity>(activity);
			this.responseListener = responseListener;
		}

		@Override
		public void onResponse(final BaseBean response) {
			dismissLoadingDialog();
			returnBeforeSuccessErrorListener();

			if (response != null) {
				if (response.isStatusSuccess()) {
					if (responseListener != null) {
						CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
							@Override
							public void onCatch() {
								responseListener.onSuccess(response);
							}
						});
					}
				} else {
					dealError(response);
				}
			}else{
				dealError(response);
			}
		}

		private void dismissLoadingDialog() {
			if (mSoftActivity.get() != null) {
				LoadingDialog.dismissIfExist();
			}
		}

		/**
		 * 访问网络返回 200 OK时的服务器接口定义的错误处理
		 */
		private void dealError(BaseBean response) {
			if (response == null){
				returnErrorListener(response);
			}

			//重新获取token
			if (response.isStatusFail() && response.isTokenInvalidate()){//token失效
				ApiImpl.getAppToken(mSoftActivity.get(), new ResponseListener<TokenBean>() {
					@Override
					public void onSuccess(TokenBean tokenResponse) {
						if (myRequest.sendCount == 2){//如果请求因为token失效发了两次，就放弃了
							myRequest.sendCount = 1;
							tokenResponse.message = "请求发了两次都失败了";
							returnErrorListener(tokenResponse);
							return;
						}
						//重新发送这个请求
						Map params = myRequest.getParams();
						myRequest.sendCount = 2;
						RequestAgent.getInstance().sendIt(myRequest, params, myRequest.requestUrl, mSoftActivity.get());
					}

					@Override
					public void onError(BaseBean error2) {
						returnErrorListener(error2);
					}
				});
			}else if (response.isLogoutByServer() && (!ApiUrl.sales_account_login.equals(myRequest.requestUrl)) ){
				//除登录接口外，其他接口响应被服务器强制退出登录的code
//				LoginActivity.logoutByServerAndStartIt(response.message);
			}else{
				returnErrorListener(response);
			}
		}

		@Override
		public void onErrorResponse(VolleyError error) {
			//访问网络返回不是 200 OK时的错误处理
			dismissLoadingDialog();
			returnBeforeSuccessErrorListener();

			BaseBean errorBean = new BaseBean();
			if (error != null) {
				errorBean.result = VolleyErrorHelper.getVolleyErrorCode(error) + "";
				errorBean.message = error.getMessage();
				if (VolleyErrorHelper.getVolleyErrorCode(error) == 200) {
					errorBean.message = "数据解析错误";
				}
			}

			returnErrorListener(errorBean);
		}

		public void returnErrorListener(final BaseBean errorBean){
			CrashReportUtil.postApiErrorToBugly(myRequest, errorBean);

			if (responseListener != null){
				CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
					@Override
					public void onCatch() {
						responseListener.onError(errorBean);
					}
				});
			}
		}

		private void returnBeforeSuccessErrorListener() {
			if (responseListener != null && responseListener instanceof ExpandResponseListener) {
				CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
					@Override
					public void onCatch() {
						((ExpandResponseListener) responseListener).beforeSuccessAndError();
					}
				});
			}
		}


	}



	protected void sendIt(final MyRequest myRequest, final Map<String, Object> requestParams, final String urlPlus, final Activity loadingDialogContext) {
		CrashReportUtil.setTryCatchOnline(new CrashReportUtil.ITryCatch() {
			@Override
			public void onCatch() {
				if ( !NetWorkUtils.isNetWorkConnected()){
					//判断网络状态
					ToastUtils.showShortToast("请检查网络连接");
					return;
				}
				if (loadingDialogContext != null) {
					LoadingDialog.showIfNotExist(loadingDialogContext, false);
				}

				Map<String, Object> requestParamsTemp = new HashMap<String, Object>();
				if (requestParams != null) {
					for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
						if (entry.getKey() == null) {
							LogUtils.e("sendRequest的requestParams中key不能为null");
							return;
						}
						if (entry.getValue() == null) {
							requestParamsTemp.put(entry.getKey(), "");
						} else {
							requestParamsTemp.put(entry.getKey(), entry.getValue());
						}
					}
				}

				addRequest(myRequest, requestParamsTemp, urlPlus);
			}
		});
	}







}
