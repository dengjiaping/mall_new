package com.android.volley.mynet;

import com.giveu.shoppingmall.base.DebugConfig;

public interface ApiUrl {

    /**
     * 线上地址前缀
     */
    String BASE_URL = DebugConfig.getBaseUrl();
    String H5_BASE_URL = DebugConfig.getH5BaseUrl();
    String FEED_BACK_URL = DebugConfig.getFeedBackBaseUrl();
    String WECHAT_BASE_URL = DebugConfig.getCourtesyUrl();

    interface WebUrl {
        String commonProblem = DebugConfig.getCommonQuestionBaseUrl() + "question/";//常见问题
        String cashBLoanStatic = H5_BASE_URL + "h5/sales/index.html#/CashB/CashBLoanStatic";//随借随还
        String cashLoanStatic = H5_BASE_URL + "h5/sales/index.html#/CashI/CashLoanStatic";//现金贷
        String test = H5_BASE_URL + "h5/sales/index.html#/CashI/CashLoanStatic";//人身意外伤害保险
        String pAProtocol = H5_BASE_URL + "h5/sales/index.html#/Common/PAProtocol";//钱包激活
        String authorize = H5_BASE_URL + "h5/sales/index.html#/Common/Authorize";//代扣
        String uRProtocol = H5_BASE_URL + "h5/sales/index.html#/Common/URProtocol";//注册
        String oConsumeLoanStatic = H5_BASE_URL + "h5/sales/index.html#/OConsume/OConsumeLoanStatic";//充值
    }

    //帮助与反馈
    String helpfeedback_queryQuestionInfo = FEED_BACK_URL + "helpfeedback/queryQuestionInfo";
    //反馈
    String helpfeedback_addQuestionMessage = FEED_BACK_URL + "helpfeedback/addQuestionMessage";
    //联系人类型
    String apersonCenter_account_getContactTypeInfo = BASE_URL + "personCenter/account/getContactTypeInfo";
    //补充其他联系人
    String personCenter_account_addOtherContact = BASE_URL + "personCenter/account/addOtherContact";
    //广告页
    String adviertisement_getad = BASE_URL + "adviertisement/getad";
    String token_getToken = BASE_URL + "personCenter/token/applyToken";
    //验证密码
    String sales_account_checkPwd = BASE_URL + "sales/account/checkPwd";
    //应用更新
    String personCenter_account_getVersion = BASE_URL + "personCenter/account/getVersion";
    //公用请求蜂鸟接口
    String common_url = BASE_URL + "sales/public/commonsMethod";
    //公用请求蜂鸟path对应的key
    String COMMON_KEY = "commonsMethod_path";
    //保存极光设备号
    String api_Account_SaveDeviceNumber = "/api/Account/SaveDeviceNumber";
    //用户注册第一步
    String personCenter_account_exist = BASE_URL + "personCenter/account/exist";
    //用户注册-第二步
    String personCenter_account_register = BASE_URL + "personCenter/account/register";
    //下发短信验证码
    String personCenter_util_sendSMSCode = BASE_URL + "personCenter/util/sendSMSCode";
    //校验短信验证码
    String personCenter_util_chkValiCode = BASE_URL + "personCenter/util/chkValiCode";
    //用户登录
    String personCenter_account_login = BASE_URL + "personCenter/account/login";
    //找回登录密码（校验短信码）
    String personCenter_account_resetPwd_checkSmsCode = BASE_URL + "personCenter/account/resetPwd/checkSmsCode";
    //找回密码（重置密码）
    String personCenter_account_resetLoginPwd = BASE_URL + "personCenter/account/resetLoginPwd";
    //找回密码（校验身份）
    String personCenter_account_resetPwd_checkUserInfo = BASE_URL + "personCenter/account/resetPwd/checkUserInfo";
    //钱包资质判定
    String personCenter_account_getWalletQualified = BASE_URL + "personCenter/account/getWalletQualified";
    //钱包激活发送短信接口
    String personCenter_account_sendActivateSmsCode = BASE_URL + "personCenter/account/sendActivateSmsCode";
    //钱包激活
    String personCenter_account_activateWallet = BASE_URL + "personCenter/account/activateWallet";
    //修改手机号
    String personCenter_account_updatePhone = BASE_URL + "personCenter/account/updatePhone";
    //设置(钱包激活)交易密码
    String personCenter_account_setPayPwd = BASE_URL + "personCenter/account/setPayPwd";
    //校验交易密码
    String personCenter_account_verifyPayPwd = BASE_URL + "personCenter/account/verifyPayPwd";
    //设置（重置）交易密码
    String personCenter_account_resetPayPwd = BASE_URL + "personCenter/account/resetPayPwd";
    //查询绑卡列表
    String personCenter_bankCard_getBankInfo = BASE_URL + "personCenter/bankCard/getBankInfo";
    //还款首页
    String personCenter_repayment_getRepaymentInfo = BASE_URL + "personCenter/repayment/getRepaymentInfo";
    //还款明细
    String personCenter_repayment_getInstalmentDetails = BASE_URL + "personCenter/repayment/getInstalmentDetails";
    //交易查询
    String personCenter_repayment_listContract = BASE_URL + "personCenter/repayment/listContract";
    //交易详细
    String personCenter_repayment_getContractDetails = BASE_URL + "personCenter/repayment/getContractDetails";
    //分期明细
    String personCenter_repayment_listInstalment = BASE_URL + "personCenter/repayment/listInstalment";
    //删除银行卡
    String personCenter_bankCard_deleteBankInfo = BASE_URL + "personCenter/bankCard/deleteBankInfo";
    //设置默认代扣卡
    String personCenter_bankCard_setDefaultCard = BASE_URL + "personCenter/bankCard/setDefaultCard";
    //获取用户信息
    String personCenter_account_getUserInfo = BASE_URL + "personCenter/account/getUserInfo";
    //退出登录
    String personCenter_account_logout = BASE_URL + "personCenter/account/logout";
    //分期产品取现费用计算
    String personCenter_enchashment_repaycost = BASE_URL + "personCenter/enchashment/repaycost";
    //随借随还取现费用计算
    String personCenter_enchashment_enchashment = BASE_URL + "personCenter/enchashment/enchashment";
    //话费流量充值产品
    String goods_callTraffics = BASE_URL + "goods/callTraffics";
    //手机归属地查询
    String goods_callTraffics_segment = BASE_URL + "goods/callTraffics/segment";
    //初始化金融产品接口
    String personCenter_enchashment_getProducts = BASE_URL + "personCenter/enchashment/getProducts";
    //分期取现月供明细
    String personCenter_enchashment_rpmDetail = BASE_URL + "personCenter/enchashment/rpmDetail";
    //取现合同生成
    String personCenter_enchashment_addEnchashmentCredit = BASE_URL + "personCenter/enchashment/addEnchashmentCredit";
    //取现记录查询
    String personCenter_enchashment_qxRecords = BASE_URL + "personCenter/enchashment/qxRecords";
    //创建充值订单
    String order_createRechargeOrder = BASE_URL + "order/createRechargeOrder";
    //确认充值订单
    String order_confirmRechargeOrder = BASE_URL + "order/confirmRechargeOrder";
    //第三方支付成功调用充值
    String order_thirdPayRecharge = BASE_URL + "order/thirdPayRecharge";
    //还款预下单
    String personCenter_repayment_createRepaymentOrder = BASE_URL + "personCenter/repayment/createRepaymentOrder";
    //获取支持的银行列表
    String personCenter_bankCard_getUsableBankList = BASE_URL + "personCenter/bankCard/getUsableBankList";
    //绑卡识别(卡bin)
    String personCenter_bankCard_identifyCard = BASE_URL + "personCenter/bankCard/identifyCard";
    //签约代扣协议(获取验证码)
    String personCenter_bankCard_agreementApply = BASE_URL + "personCenter/bankCard/agreementApply";
    //新增银行卡
    String personCenter_bankCard_addBankCard = BASE_URL + "personCenter/bankCard/addBankCard";
    //还款结果查询
    String pay_query = BASE_URL + "pay/query";
    //添加现居住地址
    String personCenter_account_addLiveAddress = BASE_URL + "personCenter/account/addLiveAddress";
    //京东地址Json数据
    String personCenter_address_getAddListJson = BASE_URL + "personCenter/address/getAddListJson";
    //获取居住地址信息
    String personCenter_address_getLiveAddress = BASE_URL + "personCenter/account/getLiveAddress";
    //获取其他联系人信息
    String personCenter_account_getOtherContact = BASE_URL + "personCenter/account/getOtherContact";
    //获取优惠券列表
    String act_getCourtesyCardList = WECHAT_BASE_URL + "activity-biz/act/getCourtesyCardList";
    //领取优惠券列表
    String act_receiveCourtesyCard = WECHAT_BASE_URL + "activity-biz/act/receiveCourtesyCard";
    //获取活动信息
    String anniversary_getActivityInfo = WECHAT_BASE_URL + "wechat-web/anniversary/getActivityInfo";
    //获取商品收藏列表
    String collections_goodsSkus_all = BASE_URL + "sc/users/collections/goodsSkus/all";
    //商品收藏
    String collections_goodsSkus_collect = BASE_URL + "sc/users/collections/goodsSkus/collect";
    //获取用户收货地址列表
    String personCenter_address_getAddress = BASE_URL + "personCenter/address/getAddress";
    //新增收货地址
    String personCenter_address_addAddress = BASE_URL + "personCenter/address/addAddress";
    //删除收货地址
    String personCenter_address_deleteAddress = BASE_URL + "personCenter/address/deleteAddress";
    //修改收货地址
    String personCenter_address_updateAddress = BASE_URL + "personCenter/address/updateAddress";
    //设置默认收货地址
    String personCenter_address_setDefaultAddress = BASE_URL + "personCenter/address/setDefaultAddress";
    //获取我的订单列表
    String orderApp_myListOrder = BASE_URL + "order/myListOrder";
    //获取我的订单数量
    String orderApp_myOrderNumByStatus = BASE_URL + "order/myOrderNumByStatus";
    //获取订单详情
    String order_orderDetail = BASE_URL + "order/orderDetail";
    //删除订单
    String order_deleteOrder = BASE_URL + "order/deleteOrder";
    //取消订单
    String order_cancelOrder = BASE_URL + "order/cancelOrder";
    //充值申请退款
    String order_rechargeApplyForRefund = BASE_URL + "order/rechargeApplyForRefund";
    //订单追踪
    String order_orderLogistics = BASE_URL + "order/orderLogistics";
    //确认收货
    String order_confirmReceipt = BASE_URL + "order/confirmReceipt";
    //商品介绍
    String sc_goods_sku_instruction = BASE_URL + "sc/goods/sku/instruction";
    //商品详情
    String sc_goods_sku_detail = BASE_URL + "sc/goods/sku/detail";
    //商品库存检查
    String sc_goods_sku_stock = BASE_URL + "sc/goods/sku/stock";
    //商品选配
    String sc_goods_sku_choose = BASE_URL + "sc/goods/sku/choose";
    //商品搜索热词更新
    String sc_goods_search_hotword_refresh = BASE_URL + "sc/goods/search/hotWord/refresh";
    //一级类目查询子类目(商品分类)
    String sc_goods_shopTypes_childrenShopTypes = BASE_URL + "sc/goods/shopTypes/childrenShopTypes";
    //商品SKU搜索
    String sc_goods_search_goodsSearch = BASE_URL + "/sc/goods/search/goodsSearch";
    //获取首付和分期数
    String order_getAppDownPayAndMonthPay = BASE_URL + "/order/getAppDownPayAndMonthPay";
}

