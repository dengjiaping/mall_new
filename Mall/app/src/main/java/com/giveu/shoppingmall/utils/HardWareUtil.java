package com.giveu.shoppingmall.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.lidroid.xutils.util.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by 508632 on 2017/1/6.
 */

public class HardWareUtil {
	/**
	 * 获取本地IP地址
	 *
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress
								.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}

	/**
	 * 获取设备编号
	 *
	 * @return
	 */
	public static String getDeviceId(Context context) {
		String id = "";
		try{
			TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
			id = tm.getDeviceId();
			if (TextUtils.isEmpty(id)) {
				id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally {
			if (TextUtils.isEmpty(id)){
				id = UUID.randomUUID().toString();
			}
		}
		return id;
	}

	/**
	 * 获取设备MAC地址
	 *
	 * @return
	 */
	public static String getMacAdd(Context context) {
		WifiManager wifi = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);

		WifiInfo info = wifi.getConnectionInfo();

		return splitMacAdd(info.getMacAddress());
	}

	private static String splitMacAdd(String mac) {
		StringBuffer sb = new StringBuffer();
		if (null != mac && !"".equals(mac)) {
			String[] arr = mac.split(":");
			for (int i = 0; i < arr.length; i++) {
				sb.append(arr[i]);
			}
		}
		return sb.toString();
	}

	/**
	 * 获取手机品牌
	 * @return
	 */
	public static String getPhoneBrand() {

		return Build.BRAND;
	}

	/**
	 * 获取手机型号
	 *
	 * @return
	 */
	public static String getPhoneModel() {

		return Build.MODEL;
	}

	/**
	 * 获取SDK版本号
	 * @return
	 */
	public static String getSDKVersion() {

		return Build.VERSION.SDK_INT+"";
	}

	/**
	 * 获取系统版本号
	 *
	 * @param context
	 * @return
	 */
	public static String getSystemVersion(Context context) {

		return Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机分辨率
	 *
	 * @param context
	 * @return
	 */
	public static String getPhoneResolution(Context context) {

		DisplayMetrics displayMetrics = new DisplayMetrics();
		WindowManager windowMgr = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		windowMgr.getDefaultDisplay().getMetrics(displayMetrics);
		int width = displayMetrics.widthPixels;
		int height = displayMetrics.heightPixels;
		return width + "*" + height;
	}

	/**
	 * IMEI 全称�?International Mobile Equipment Identity，中文翻译为国际移动装备辨识码， 即�?常所说的手机序列号，
	 * 用于在手机网络中识别每一部独立的手机，是国际上公认的手机标志序号，相当于移动电话的身份证。序列号共有15位数字，�?位（TAC）是型号核准号码�?
	 * 代表手机类型。接�?位（FAC）是�?��装配号，代表产地。后6位（SNR）是串号，代表生产顺序号。最�?位（SP）一般为0，是�?��码，备用�?
	 * 国际移动装备辨识码一般贴于机身背面与外包装上，同时也存在于手机记忆体中，通过输入*#06#即可查询�?
	 *
	 * @param context
	 * @return
	 */
	public static String getIMEI(Context context) {
		TelephonyManager ts = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return ts.getDeviceId();
	}

	/** 判断手机是否root，不弹出root请求框<br/> */
	public static boolean isRoot() {
		String binPath = "/system/bin/su";
		String xBinPath = "/system/xbin/su";
		if (new File(binPath).exists() && isExecutable(binPath))
			return true;
		if (new File(xBinPath).exists() && isExecutable(xBinPath))
			return true;
		return false;
	}

	private static boolean isExecutable(String filePath) {
		Process p = null;
		try {
			p = Runtime.getRuntime().exec("ls -l " + filePath);
			// 获取返回内容
			BufferedReader in = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String str = in.readLine();
			LogUtils.i(str);
			if (str != null && str.length() >= 4) {
				char flag = str.charAt(3);
				if (flag == 's' || flag == 'x')
					return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(p!=null){
				p.destroy();
			}
		}
		return false;
	}


}
