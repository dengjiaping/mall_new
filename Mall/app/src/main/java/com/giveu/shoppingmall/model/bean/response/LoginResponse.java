package com.giveu.shoppingmall.model.bean.response;

import com.android.volley.mynet.BaseBean;

/**
 * Created by 513419 on 2017/6/29.
 */

public class LoginResponse extends BaseBean<LoginResponse> {

    /**
     * accessToken : 78hxxxdffs
     * activeDate : 2017/6/23
     * availableCyLimit : 5154
     * availablePosLimit : 50000
     * certNo : 430691198606187561
     * cyLimit : 50000
     * endDate : 2020/6/1
     * globleLimit : 100000
     * idPerson : 11111
     * mobile : 18118751982
     * nickName : 佳文
     * posLimit : 0
     * realName : 王佳文
     * status : 1
     * statusDesc : 注册成功-已激活
     * userId : 12
     * userName : 1856989654
     * userPic : user_09.jpg
     */




    public String accessToken;
    public String activeDate;
    public String availableCyLimit;
    public String availablePosLimit;
    public String availableRechargeLimit;
    public String creditCount;
    public String cyLimit;
    public String endDate;
    public String globleLimit;
    public String idPerson;
    public String ident;
    public String name;
    public String nickName;
    public String bankName;
    public String bankIconUrl;
    public String defaultCard;
    public String phone;
    public String posLimit;
    public String repayAmount;
    public String repayDate;
    public boolean status;
    public String userId;
    public String userName;
    public String userPic;
    public String totalCost;
    public boolean hasDefaultCard;
    public boolean isSetPwd;
    public String remainDays;
}
