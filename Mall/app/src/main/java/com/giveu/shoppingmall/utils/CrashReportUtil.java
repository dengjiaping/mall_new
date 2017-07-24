package com.giveu.shoppingmall.utils;

import android.text.TextUtils;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBeanParent;
import com.android.volley.mynet.MyRequest;
import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.DebugConfig;
import com.giveu.shoppingmall.base.MyException;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 508632 on 2017/4/5.
 */

public class CrashReportUtil {
	static class SceneTag{
		public static final int API_ERROR = 41761;//接口错误
		public static final int TRY_CATCH_EXCEPTION = 42071;//捕获的异常
		private static SceneTag sceneTag;
		Map<String, Integer> tagMap;

		private SceneTag(){
			tagMap = new HashMap<>();
			tagMap.put(ApiUrl.token_getToken, 46688);//获取token
			tagMap.put(ApiUrl.sales_account_login, 46687);//登录
		}

		public static SceneTag getInstance(){
			if (sceneTag == null){
				synchronized (SceneTag.class){
					if (sceneTag == null){
						sceneTag = new SceneTag();
					}
				}
			}
			return sceneTag;
		}

		public int getApiSceneTag(String requstUrl) {
			int tag = API_ERROR;
			if (tagMap != null){
				Integer tag2 = tagMap.get(requstUrl);
				tag = (tag2 == null ? API_ERROR : tag2);
			}
			return tag;
		}

	}

	/**
	 * 上传文件时利用bugly上报访问网络时的自定义错误
	 */
	public static void postApiErrorToBugly(final boolean isThirdPlatformApi, List<String> keys, List<List<String>> filePaths, Map<String, Object> mParams, final String requestUrl, String response){
//		try{
//			Gson gson = new Gson();
//			Map<String, Object> map = new HashMap<>();
//			map.put("是否第三方", isThirdPlatformApi);
//			map.put("地址", requestUrl);
//			map.put("文本参数", mParams);
//			map.put("图片key", keys);
//			map.put("图片地址", filePaths);
//			map.put("返回值", response);
//			String json = gson.toJson(map);
//			Throwable thr = new MyException(json);
//			CrashReport.setUserSceneTag(BaseApplication.getInstance(), SceneTag.getInstance().getApiSceneTag(requestUrl)); // 上报后的Crash会显示该标签
//			CrashReport.postCatchedException(thr);  // bugly会将这个throwable上报
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	}

	public static void postTryCatchToBugly(String str){
		try{
			if ( !TextUtils.isEmpty(str)){
				Throwable thr = new MyException(str);
				CrashReport.postCatchedException(thr);  // bugly会将这个throwable上报
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public static void postTryCatchToBugly(Throwable thr){
		try{
			if ( thr != null){
				CrashReport.setUserSceneTag(BaseApplication.getInstance(), SceneTag.TRY_CATCH_EXCEPTION); // 上报后的Crash会显示该标签
				CrashReport.postCatchedException(thr);  // bugly会将这个throwable上报
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 普通请求时利用bugly上报访问网络时的自定义错误
	 * @param myRequest
	 * @param baseBean
	 */
	public static void postApiErrorToBugly(MyRequest myRequest, BaseBeanParent baseBean){
//		try{
//			if (baseBean != null){
//				Gson gson = new Gson();
//				Map<String, Object> map = new HashMap<>();
//				map.put("地址", myRequest.requestUrl);
//				map.put("参数", myRequest.getParams());
//				map.put("返回值", baseBean);
//				String json = gson.toJson(map);
//				Throwable thr = new MyException(json);
//				CrashReport.setUserSceneTag(BaseApplication.getInstance(), SceneTag.getInstance().getApiSceneTag(myRequest.requestUrl)); // 上报后的Crash会显示该标签
//				CrashReport.postCatchedException(thr);  // bugly会将这个throwable上报
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}
	}

//	public static void setUserIdTobugly(RegisterResponse loginBean) {
//		try{
//			if (loginBean != null){
//				CrashReport.setUserId(loginBean.getSalesId());//bugly记录用户信息
//			}
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//	}

	/**
     * 线上环境加入整体TryCatch，可以捕捉和调用者在同一个线程下面发生的exception
     */
    public static void setTryCatchOnline(ITryCatch tryCatch){
        if ( !DebugConfig.isDev ){
            try{
                if (tryCatch != null){
                    tryCatch.onCatch();
                }
            }catch (Exception e){
				if (e != null){
					e.printStackTrace();
					postTryCatchToBugly(e);
				}
            }
        }else{
            if (tryCatch != null){
                tryCatch.onCatch();
            }
        }
    }

	public interface ITryCatch{
		void onCatch();
	}




}
