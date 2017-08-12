package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/7/5.
 */

public class WalletQualifiedResponse extends BaseBean<WalletQualifiedResponse> {
    /**
     * bankNo : 515442940123456789
     * cyLimit : 1
     * globleLimit : 1
     * idPerson : 11413713
     * ident : 23012619810605986X
     * isActivation : 32070
     * isPhoneChage : 1
     * isSetPwd : 1
     * name : 邓绿蝶
     * phone : 15818512345
     * posLimit : 1
     */
    /**
     * 普通用户
     */
    public String bankNo;
    public String name;
    public String phone;
    public String idPerson;
    public String ident;
    /**
     * 手q用户
     */
    public String cyLimit;
    public String globleLimit;
    public String isActivation;//1 手Q激活
    public boolean isPhoneChage;
    public boolean isSetPwd;
    public String posLimit;
    public String lab;

    /**
     * 判断是否是手Q用户
     *
     * @return
     */
    public boolean hasQQActivation() {
        if ("1".equals(isActivation)) {
            //已激活
            return true;
        } else {
            //未激活
            return false;
        }
    }
}
