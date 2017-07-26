package com.android.volley.mynet;

import java.io.Serializable;

public class BaseBeanParent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5331198215023944098L;
	public String code = Integer.MIN_VALUE + "";
	public String result;
	public String message;
	public String originResultString;

	public interface STATUS{
		String SUCCESS = "success";
		String FAIL = "fail";
	}
	public static int getResultCode(String status){
		if (STATUS.SUCCESS.equals(status)){
			return 200;
		}
		int i = 0;
		try{
			i = Integer.parseInt(status);
		}catch (Exception e){
			e.printStackTrace();
		}
		return i;
	}

	public boolean isStatusSuccess(){
		return "success".equals(result);
	}

	public boolean isStatusFail(){
		return !isStatusSuccess();
	}


	public BaseBean toBaseBean() {
		BaseBean info = new BaseBean();
		info.code = this.code;
		info.result = this.result;
		info.message = this.message;
		info.originResultString = this.originResultString;
		return info;
	}

	/**
	 * 判断token失效
	 */
	public boolean isTokenInvalidate() {
		return "000401".equals(code);
	}

	/**
	 * 被服务器强制退出登录
	 */
	public boolean isLogoutByServer(){
		return "500135".equals(code);
	}


}
