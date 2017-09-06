package com.giveu.shoppingmall.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.fastaccess.permission.base.PermissionHelper;
import com.fastaccess.permission.base.callback.OnPermissionCallback;
import com.lidroid.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *  android6.0之后的申请权限helper
 *  如果activity要申请权限请继承我
 */
public abstract class BasePermissionActivity extends BaseActivity implements OnPermissionCallback {
	PermissionHelper permissionHelper = null;

	public PermissionHelper getPermissionHelper() {
		return permissionHelper;
	}

	//------------ android6.0之后的申请权限helper---------------------------------------------------------
	/**
	 * @param permissionName
	 */
	@Override
	public void onPermissionGranted(@NonNull String[] permissionName) {
		LogUtils.i("onPermissionGranted");
	}

	@Override
	public void onPermissionDeclined(@NonNull String[] permissionName) {
		LogUtils.i("onPermissionDeclined");
	}

	@Override
	public void onPermissionPreGranted(@NonNull String permissionsName) {
		LogUtils.i("onPermissionPreGranted");
	}

	@Override
	public void onPermissionNeedExplanation(@NonNull String permissionName) {
		LogUtils.i("onPermissionNeedExplanation");
	}

	@Override
	public void onPermissionReallyDeclined(@NonNull String permissionName) {
		LogUtils.i("onPermissionReallyDeclined");
	}

	@Override
	public void onPermissionReallyDeclined(@NonNull List<String> permissionName) {

	}

	@Override
	public void onNoPermissionNeeded() {
		LogUtils.i("onNoPermissionNeeded");
	}

	/**
	 * @param grantResults -1=拒绝 0=允许
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		LogUtils.i("onRequestPermissionsResult");
		if (permissionHelper != null){
			permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}

		for (int i = 0; i < permissions.length; i++) {
			if (!TextUtils.isEmpty(permissions[i])) {
				boolean isGranted = permissionHelper.isPermissionGranted(permissions[i]);
			}
		}

		boolean isAllow = true;
		for (int i = 0; i < grantResults.length; i++) {
			if (grantResults[i] != 0) {
				isAllow = false;
			}
		}
		if (isAllow) {
			//获取到权限
			onAccessPermission();
		} else {
			onDeclinePermission();
		}

	}

	/**
	 * 获取到权限
	 */
	protected void onAccessPermission(){

	}
	/**
	 *  拒绝了权限
	 */
	protected void onDeclinePermission(){

	}

	public void setPermissionHelper(boolean isForce, @NonNull Object multiPermissions) {
		permissionHelper = PermissionHelper.getInstance(this);
		permissionHelper.setForceAccepting(isForce) // default is false. its here so you know that it exists.
				.request(multiPermissions);
	}
//----------------------------------------------------------------------------------------------------------------


}
