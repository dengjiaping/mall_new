package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.utils.CommonUtils;

/**
 * Created by 101900 on 2017/1/14.
 */

public class ApkUgradeResponse extends BaseBean<ApkUgradeResponse> {
    public String url;//apk地址
    public String versionStatus;//安卓升级0无更新1普通2强制
    public long version;


	public boolean isNoUpdate(){
		return "0".equals(versionStatus);
	}
	public boolean isUnforceUpdate(){
		if (isVersionNeedUpgrade()){
			return "1".equals(versionStatus);
		}
		return false;
	}

	public boolean isVersionNeedUpgrade() {
		try {
			if (Integer.parseInt(CommonUtils.getVersionCode()) < version){
				return true;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	public boolean isForceUpdate(){
		if (isVersionNeedUpgrade()){
			return "2".equals(versionStatus);
		}
		return false;
	}

}
