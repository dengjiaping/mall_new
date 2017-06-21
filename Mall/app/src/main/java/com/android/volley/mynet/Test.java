package com.android.volley.mynet;

import android.app.Activity;
import android.widget.Toast;

import com.giveu.shoppingmall.base.BaseApplication;

import java.util.Map;

/**
 * Created by 508632 on 2017/1/2.
 */

public class Test {
	/**
	 * 测试volley框架用的
	 *
	 * @param loadingDialogContext
	 */
	private static void testRequestAgent(Activity loadingDialogContext) {
		// test
		Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"userid"}, new String[]{"207077"});
		RequestAgent.getInstance().sendPostRequest(requestParams2, "", TestBean.class, loadingDialogContext, new BaseRequestAgent.ResponseListener<TestBean>() {

			@Override
			public void onSuccess(TestBean response) {
				String dString = response.message + response.result + "----" + ((TestBean) response).toString();
				Toast.makeText(BaseApplication.getInstance().getApplicationContext(), dString, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(BaseBean error) {
				Toast.makeText(BaseApplication.getInstance().getApplicationContext(), error.message, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public static class TestBean extends BaseBean {
	//	{"message":"","balance":"10","resultCode":"0","mdw":"319f3ffbcef487564bb0581885163520","TYPE":"-1","url":"http://m.pengsi.cn/pay/index.html"}
		public String balance;
		public String mdw;
		public String type;
		public String url;

		@Override
		public String toString() {
			return "TestBean [balance=" + balance + ", mdw=" + mdw + ", TYPE=" + type + ", url=" + url + "]";
		}
	}


}
