package com.android.volley.mynet;

import java.io.Serializable;

public class BaseBeanParent implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5331198215023944098L;
	public String code = Integer.MIN_VALUE + "";
	public String status;
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
		return "success".equals(status);
	}

	public boolean isStatusFail(){
		return !isStatusSuccess();
	}


	public BaseBean toBaseBean() {
		BaseBean info = new BaseBean();
		info.code = this.code;
		info.status = this.status;
		info.message = this.message;
		return info;
	}


	public boolean isTokenInvalidate() {
		return "1".equals(code);
	}

	/**
	 * 被服务器强制退出登录
	 */
	public boolean isLogoutByServer(){
		return "0001".equals(code);
	}


}
