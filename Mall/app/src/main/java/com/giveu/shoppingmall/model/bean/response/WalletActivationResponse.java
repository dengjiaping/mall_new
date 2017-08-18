package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.utils.StringUtils;

/**
 * Created by 101900 on 2017/6/30.
 */

public class WalletActivationResponse extends BaseBean<WalletActivationResponse> {



    /**
     * cyLimit : 50000
     * globleLimit : 100000
     * isPhoneChage : false
     * lab : 通过取现或商城消费可积累信用提高消费额度哦
     * posLimit : 0
     */

    public String cyLimit;
    public String globleLimit;
    public boolean isPhoneChage;
    public boolean coupon;
    public String lab;
    public String posLimit;

    /**
     * 是否显示优惠券弹窗
     * @return
     */
    public boolean hasShowCouponDialog(){
        //取现额度和消费额度为0的用户
        if(StringUtils.isNotNull(cyLimit) && StringUtils.isNotNull(posLimit)){
            if((0 == Double.parseDouble(cyLimit)) && (0 == Double.parseDouble(posLimit))){
                return true;
            }
        }
        return false;
    }
}
