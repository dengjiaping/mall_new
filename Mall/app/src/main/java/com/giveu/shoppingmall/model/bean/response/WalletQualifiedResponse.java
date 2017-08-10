package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 101900 on 2017/7/5.
 */

public class WalletQualifiedResponse extends BaseBean<WalletQualifiedResponse> {

    /**
     * bankNo : 6228481218005648235
     * idPerson : 10000923
     * ident : 371082199201058672
     * name : 唐兴
     * phone : 18109491314
     */

    public String bankNo;
    public String idPerson;
    public String ident;
    public String name;
    public String isActivation;//1 手Q激活
    public String phone;

    /**
     * 判断是否是手Q用户
     * @return
     */
    public boolean hasQQActivation(){
        if("1".equals(isActivation)){
            //已激活
            return true;
        }else{
            //未激活
            return false;
        }
    }
}
