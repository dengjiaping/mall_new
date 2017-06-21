package com.giveu.shoppingmall.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by 101900 on 2017/1/10.
 */

public class MD5Util {
    /**利用MD5进行加密
     * @param str  待加密的字符串
     * @return  加密后的字符串
     * @throws NoSuchAlgorithmException  没有这种产生消息摘要的算法
     * @throws UnsupportedEncodingException
     */
    public String EncoderByMd5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        //确定计算方法
        MessageDigest md5=MessageDigest.getInstance("MD5");
        byte[] bytes = str.getBytes("utf-8");
        //加密后的字符串
        byte[] encode = Base64.encode(md5.digest(bytes), Base64.DEFAULT);
        return new String(encode);
    }

}