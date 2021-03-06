package com.android.volley.mynet;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

/**
 * Created by 508632 on 2017/1/2.
 */
public class VolleyErrorHelper {
	public final static String SUCCESS_STATUS = "success";
	public final static String FAIL_STATUS = "fail";
	public final static String ERROR_STATUS = "error";

	// AuthFailureError：如果在做一个HTTP的身份验证，可能会发生这个错误。
	// NetworkError：Socket关闭，服务器宕机，DNS错误都会产生这个错误。
	// NoConnectionError：和NetworkError类似，这个是客户端没有网络连接。
	// ParseError：在使用JsonObjectRequest或JsonArrayRequest时，如果接收到的JSON是畸形，会产生异常。
	// SERVERERROR：服务器的响应的一个错误，最有可能的4xx或5xx HTTP状态代码。
	// TimeoutError：Socket超时，服务器太忙或网络延迟会产生这个异常。默认情况下，Volley的超时时间为。。秒。如果得到这个错误可以使用RetryPolicy。
	public static boolean isNetworkError(Object error) {
		if (isNoConnectionError(error)) {
			return false;
		}
		return (error instanceof NetworkError);
	}

    public static boolean isNoConnectionError(Object error) {
		return (error instanceof NetworkError);
	}

    public static boolean isServerError(Object error) {
		return (error instanceof ServerError);
	}

    public static boolean isAuthFailureError(Object error) {
		return (error instanceof AuthFailureError);
	}

    public static boolean isTimeoutError(Object error) {
		return (error instanceof TimeoutError);
	}

    public static boolean isParseError(Object error) {
		return (error instanceof ParseError);
	}

    public static int getVolleyErrorCode(VolleyError error) {

		if (error != null) {
			NetworkResponse response = error.networkResponse;

			if (isServerError(error) || isAuthFailureError(error) || isNetworkError(error)) {
				return NetworkCode.NETWORK_SERVERERROR_CODE;
			} else if (isNoConnectionError(error)) {
				return NetworkCode.NETWORK_NOLINK_CODE;
			} else if (isTimeoutError(error)) {
				return NetworkCode.NETWORK_TIMEOUT_CODE;
			}

			if (response != null) {
				if (response.statusCode == 200) {
					// 等于200,请求成功,可能自己数据操作错误
					return 200;
				}
			}

		}

		return NetworkCode.NETWORK_SERVERERROR_CODE;
	}

	public static boolean isSuccess(String status){
		boolean b = SUCCESS_STATUS.equals(status);
		return b;
	}

	public static boolean isFail(String status){
		boolean b = FAIL_STATUS.equals(status);
		return b;
	}

	public static boolean isError(String status){
		boolean b = ERROR_STATUS.equals(status);
		return b;
	}

	/**
	 *	错误code定义
	 */
	public interface NetworkCode {
		/**
		 *
		 * 服务器定义的错误code
		 * -1	客户端提交的参数有误
		 */
		String NETWORK_ERROR_CODE1 = "-1"; // 客户端提交的参数有误
		/**
		 * 客户端定义的错误code
		 */
		int NETWORK_NOLINK_CODE = -801; // 没有网络
		int NETWORK_TIMEOUT_CODE = -802; // 网络连接超时
		int NETWORK_SERVERERROR_CODE = -803; // 404,500等服务器错误
	}

	public interface ErrorMessage {
		String NO_LINK = "请检查网络连接";
		String TIME_OUT = "网络连接超时";
		String SERVER_ERROR = "服务器出错了";
		String PARMAS_ERROR = "客户端提交的参数有误";
		String REPONSE_JSON_PARSE_ERROR = "数据解析错误";
	}










}
