package com.giveu.shoppingmall.model;

import android.app.Activity;

import com.android.volley.mynet.ApiUrl;
import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.android.volley.mynet.FileUpload;
import com.android.volley.mynet.RequestAgent;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.model.bean.response.AdSplashResponse;
import com.giveu.shoppingmall.model.bean.response.AddressBean;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.model.bean.response.AgreementApplyResponse;
import com.giveu.shoppingmall.model.bean.response.ApkUgradeResponse;
import com.giveu.shoppingmall.model.bean.response.BankCardListResponse;
import com.giveu.shoppingmall.model.bean.response.BankListResponse;
import com.giveu.shoppingmall.model.bean.response.CashRecordsResponse;
import com.giveu.shoppingmall.model.bean.response.CheckSmsResponse;
import com.giveu.shoppingmall.model.bean.response.CollectionResponse;
import com.giveu.shoppingmall.model.bean.response.CommodityDetailResponse;
import com.giveu.shoppingmall.model.bean.response.CommodityInfoResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmOrderScResponse;
import com.giveu.shoppingmall.model.bean.response.ConfirmPayResponse;
import com.giveu.shoppingmall.model.bean.response.ContactsBean;
import com.giveu.shoppingmall.model.bean.response.ContactsResponse;
import com.giveu.shoppingmall.model.bean.response.ContractResponse;
import com.giveu.shoppingmall.model.bean.response.CostFeeResponse;
import com.giveu.shoppingmall.model.bean.response.CouponListResponse;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.DownPayMonthPayResponse;
import com.giveu.shoppingmall.model.bean.response.EnchashmentCreditResponse;
import com.giveu.shoppingmall.model.bean.response.FeedBackResponse;
import com.giveu.shoppingmall.model.bean.response.GoodsSearchResponse;
import com.giveu.shoppingmall.model.bean.response.IdentifyCardResponse;
import com.giveu.shoppingmall.model.bean.response.IndexResponse;
import com.giveu.shoppingmall.model.bean.response.InstalmentDetailResponse;
import com.giveu.shoppingmall.model.bean.response.ListInstalmentResponse;
import com.giveu.shoppingmall.model.bean.response.LivingAddressBean;
import com.giveu.shoppingmall.model.bean.response.LoginResponse;
import com.giveu.shoppingmall.model.bean.response.OrderDetailResponse;
import com.giveu.shoppingmall.model.bean.response.OrderListResponse;
import com.giveu.shoppingmall.model.bean.response.OrderTraceResponse;
import com.giveu.shoppingmall.model.bean.response.PayPwdResponse;
import com.giveu.shoppingmall.model.bean.response.PayQueryResponse;
import com.giveu.shoppingmall.model.bean.response.ProductResponse;
import com.giveu.shoppingmall.model.bean.response.RandCodeResponse;
import com.giveu.shoppingmall.model.bean.response.RechargeResponse;
import com.giveu.shoppingmall.model.bean.response.RegisterResponse;
import com.giveu.shoppingmall.model.bean.response.RepayCostResponse;
import com.giveu.shoppingmall.model.bean.response.RepaymentActionResponse;
import com.giveu.shoppingmall.model.bean.response.RepaymentResponse;
import com.giveu.shoppingmall.model.bean.response.RpmDetailResponse;
import com.giveu.shoppingmall.model.bean.response.SegmentResponse;
import com.giveu.shoppingmall.model.bean.response.ShopTypesBean;
import com.giveu.shoppingmall.model.bean.response.SkuInfo;
import com.giveu.shoppingmall.model.bean.response.SkuIntroductionResponse;
import com.giveu.shoppingmall.model.bean.response.SmsCodeResponse;
import com.giveu.shoppingmall.model.bean.response.TokenBean;
import com.giveu.shoppingmall.model.bean.response.TransactionDetailResponse;
import com.giveu.shoppingmall.model.bean.response.WalletActivationResponse;
import com.giveu.shoppingmall.model.bean.response.WalletQualifiedResponse;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import java.util.List;
import java.util.Map;


/**
 * 所有的网络请求放在这个类中
 */


public class ApiImpl {


    //广告页
    public static void AdSplashImage(String type, BaseRequestAgent.ResponseListener<AdSplashResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"platform", "type"}, new String[]{"Android", type});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.adviertisement_getad, AdSplashResponse.class, responseListener);
    }

    public static void getAppToken(Activity context, final BaseRequestAgent.ResponseListener<TokenBean> responseListener) {
//        tokenType	带权限token 1 不带权限token 2
        String tokenType = "2";
//        if (LoginHelper.getInstance().hasLogin()){
//            tokenType = "1";
//        }
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "platform"}, new String[]{SharePrefUtil.getUUId(), "Android"});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.token_getToken, TokenBean.class, context, new BaseRequestAgent.ResponseListener<TokenBean>() {
            @Override
            public void onSuccess(TokenBean response) {
                try {
                    //把token存起来
                    SharePrefUtil.setAppToken(response.data.accessToken);

                    if (responseListener != null) {
                        responseListener.onSuccess(response);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                try {
                    if (responseListener != null) {
                        responseListener.onError(errorBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void sendApkUpgradeRequest(Activity context, BaseRequestAgent.ResponseListener<ApkUgradeResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"version"},
                new Object[]{CommonUtils.getVersionCode()});

        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getVersion, ApkUgradeResponse.class, context, responseListener);
    }


    //忘记手势密码调用这个验证文本密码
    public static void checkPwd(Activity context, String password, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bind", "identification", "password"}, new String[]{"2", SharePrefUtil.getUUId(), password});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.sales_account_checkPwd, BaseBean.class, context, responseListener);
    }

    //保存极光设备号
    public static void saveDeviceNumber(String deviceNumber) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{ApiUrl.COMMON_KEY, "deviceNumber"}, new String[]{ApiUrl.api_Account_SaveDeviceNumber, deviceNumber});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.common_url, BaseBean.class, null, new BaseRequestAgent.ResponseListener() {
            @Override
            public void onSuccess(BaseBean response) {
                //设置为已上传过设备号
//                LoginHelper.getInstance().setHasUploadDeviceNumber(true);
            }

            @Override
            public void onError(BaseBean errorBean) {

            }
        });
    }

    //退出登录
    public static void logout(Activity context, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId"}, new Object[]{SharePrefUtil.getUUId()});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_logout, BaseBean.class, context, responseListener);
    }

    //获取用户信息
    public static void getUserInfo(Activity context, String idPerson, String userId, BaseRequestAgent.ResponseListener<LoginResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "userId"}, new Object[]{StringUtils.string2Long(idPerson), userId});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getUserInfo, LoginResponse.class, context, responseListener);
    }

    //用户注册
    public static void register(Activity context, String mobile, String password, String randCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "mobile", "password", "randCode", "platform"}, new String[]{SharePrefUtil.getUUId(), mobile, password, randCode, "Android"});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_register, RegisterResponse.class, context, responseListener);
    }

    //下发短信验证码
    public static void sendSMSCode(Activity context, String phone, String codeType, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"phone", "codeType",}, new String[]{phone, codeType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_util_sendSMSCode, BaseBean.class, context, responseListener);
    }

    //校验短信验证码
    public static void chkValiCode(Activity context, String code, String phone, String codeType, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"code", "phone", "codeType"}, new String[]{code, phone, codeType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_util_chkValiCode, BaseBean.class, context, responseListener);
    }

    //用户注册第一步
    public static void registerFirst(Activity context, String mobile, String smsCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "smsCode"}, new String[]{mobile, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_exist, BaseBean.class, context, responseListener);
    }

    //用户登录
    public static void login(Activity context, String userName, String password, BaseRequestAgent.ResponseListener<LoginResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"deviceId", "userName", "password", "platform"}, new String[]{SharePrefUtil.getUUId(), userName, password, "Android"});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_login, LoginResponse.class, context, responseListener);
    }

    //钱包激活发送短信接口
    public static void sendActivateSmsCode(Activity context, String bankNo, String idPerson, String ident, String name, String phone, BaseRequestAgent.ResponseListener<SmsCodeResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankNo", "idPerson", "ident", "name", "phone"}, new Object[]{bankNo, StringUtils.string2Long(idPerson), ident, name, phone});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_sendActivateSmsCode, SmsCodeResponse.class, context, responseListener);
    }

    //钱包资质判定
    public static void getWalletQualified(Activity context, String userId, String ident, String name, BaseRequestAgent.ResponseListener<WalletQualifiedResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"userId", "ident", "name"}, new Object[]{StringUtils.string2Long(userId), ident, name});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getWalletQualified, WalletQualifiedResponse.class, context, responseListener);
    }

    //钱包激活
    public static void activateWallet(Activity context, String bankNo, String idPerson, String ident, String latitude, String longitude, String orderNo, String phone, String realName, String sendSource, String smsCode, String smsSeq, BaseRequestAgent.ResponseListener<WalletActivationResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankNo", "idPerson", "ident", "latitude", "longitude", "orderNo", "phone", "realName", "sendSource", "smsCode", "smsSeq", "userId"}, new Object[]{bankNo, StringUtils.string2Long(idPerson), ident, latitude, longitude, orderNo, phone, realName, sendSource, smsCode, smsSeq, StringUtils.string2Long(LoginHelper.getInstance().getUserId())});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_activateWallet, WalletActivationResponse.class, context, responseListener);
    }


    //找回登录密码（校验短信码）
    public static void checkSmsCode(Activity context, String mobile, String smsCode, BaseRequestAgent.ResponseListener<CheckSmsResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "smsCode"}, new String[]{mobile, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkSmsCode, CheckSmsResponse.class, context, responseListener);
    }

    //找回密码（重置密码）
    public static void resetPassword(Activity context, String mobile, String newPwd, String userName, String randCode, BaseRequestAgent.ResponseListener<RegisterResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"mobile", "newPwd", "userName", "randCode"}, new String[]{mobile, newPwd, userName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetLoginPwd, RegisterResponse.class, context, responseListener);
    }

    //找回密码（校验身份）
    public static void checkUserInfo(Activity context, String certNo, String mobile, String randCode, String realName, BaseRequestAgent.ResponseListener<RandCodeResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"certNo", "mobile", "realName", "randCode"}, new String[]{certNo, mobile, realName, randCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPwd_checkUserInfo, RandCodeResponse.class, context, responseListener);
    }

    //设置交易密码
    public static void setPayPwd(Activity context, String confirmPwd, String idPerson, String newPwd, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"confirmPwd", "idPerson", "newPwd"}, new Object[]{confirmPwd, StringUtils.string2Long(idPerson), newPwd});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_setPayPwd, BaseBean.class, context, responseListener);
    }

    //找回交易密码（重置密码）
    public static void resetPayPwd(Activity context, String confirmPwd, String idPerson, String newPwd, String phone, String smsCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"confirmPwd", "idPerson", "newPwd", "phone", "smsCode"}, new Object[]{confirmPwd, StringUtils.string2Long(idPerson), newPwd, phone, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_resetPayPwd, BaseBean.class, context, responseListener);
    }


    //校验交易密码
    public static void verifyPayPwd(Activity context, String idPerson, String tradPwd, BaseRequestAgent.ResponseListener<PayPwdResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "tradPwd"}, new Object[]{StringUtils.string2Long(idPerson), tradPwd});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_verifyPayPwd, PayPwdResponse.class, context, responseListener);
    }

    //修改手机号
    public static void updatePhone(Activity context, String idPerson, String phone, String randCode, String smsCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "phone", "randCode", "smsCode"}, new Object[]{StringUtils.string2Long(idPerson), phone, randCode, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_updatePhone, BaseBean.class, context, responseListener);
    }

    //查询绑卡列表
    public static void getBankCardInfo(Activity context, String idPerson, BaseRequestAgent.ResponseListener<BankCardListResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_getBankInfo, BankCardListResponse.class, context, responseListener);
    }

    //还款首页
    public static void getRepayment(Activity context, String idPerson, BaseRequestAgent.ResponseListener<RepaymentResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getRepaymentInfo, RepaymentResponse.class, context, responseListener);
    }

    //还款明细
    public static void getInstalmentDetails(Activity context, String idCredit, boolean isCurrent, String numInstalment, String productType, BaseRequestAgent.ResponseListener<InstalmentDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit", "isCurrent", "numInstalment", "productType"}, new Object[]{idCredit, isCurrent, numInstalment == null ? null : Integer.parseInt(numInstalment), productType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getInstalmentDetails, InstalmentDetailResponse.class, context, responseListener);
    }

    //交易查询
    public static void searchContract(Activity context, String creditStatus, String creditType, String idPerson, String loanDate, int page, int pageSize, String timeType, BaseRequestAgent.ResponseListener<ContractResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"creditStatus", "creditType", "idPerson", "loanDate", "page", "pageSize", "timeType"}, new Object[]{creditStatus, creditType, idPerson, loanDate, page, pageSize, timeType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_listContract, ContractResponse.class, context, responseListener);
    }

    //交易详细
    public static void getContractDetails(Activity context, String idCredit, String creditType, BaseRequestAgent.ResponseListener<TransactionDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit", "creditType"}, new Object[]{idCredit, creditType});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_getContractDetails, TransactionDetailResponse.class, context, responseListener);
    }

    //分期明细
    public static void getListInstalment(Activity context, String idCredit, BaseRequestAgent.ResponseListener<ListInstalmentResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idCredit"}, new Object[]{idCredit});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_listInstalment, ListInstalmentResponse.class, context, responseListener);
    }

    //删除银行卡
    public static void deleteBankInfo(Activity context, String code, String id, String idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"code", "id", "idPerson"}, new Object[]{code, id, StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_deleteBankInfo, BaseBean.class, context, responseListener);
    }


    //设置默认代扣卡
    public static void setDefaultCard(Activity context, String code, String id, String idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"code", "id", "idPerson"}, new Object[]{code, id, StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_setDefaultCard, BaseBean.class, context, responseListener);
    }

    //初始化金融产品接口
    public static void initProduct(Activity context, String availableCyLimit, BaseRequestAgent.ResponseListener<ProductResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"availableCyLimit"}, new Object[]{StringUtils.string2Double(availableCyLimit)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_getProducts, ProductResponse.class, context, responseListener);
    }

    //分期产品取现费用计算
    public static void repayCost(Activity context, int idProduct, int loan, BaseRequestAgent.ResponseListener<RepayCostResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idProduct", "loan"}, new Object[]{idProduct, loan});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_repaycost, RepayCostResponse.class, context, responseListener);
    }

    //随借随还取现费用计算
    public static void getCostFee(Activity context, BaseRequestAgent.ResponseListener<CostFeeResponse> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.personCenter_enchashment_enchashment, CostFeeResponse.class, context, responseListener);
    }

    //话费流量充值产品
    public static void goodsCallTraffics(Activity context, BaseRequestAgent.ResponseListener<RechargeResponse> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.goods_callTraffics, RechargeResponse.class, context, responseListener);
    }

    //手机归属地查询
    public static void goodsCallTrafficsSegment(Activity context, String phone, BaseRequestAgent.ResponseListener<SegmentResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"phone"}, new Object[]{phone});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.goods_callTraffics_segment, SegmentResponse.class, context, responseListener);
    }

    //分期取现月供明细
    public static void rpmDetail(Activity context, String idPerson, int idProduct, int loan, BaseRequestAgent.ResponseListener<RpmDetailResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "idProduct", "loan"}, new Object[]{StringUtils.string2Long(idPerson), idProduct, loan});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_rpmDetail, RpmDetailResponse.class, context, responseListener);
    }

    //取现合同生成
    public static void addEnchashmentCredit(Activity context, String insuranceFee, String bankName, String bankNo, String creditAmount, String creditType, String idPerson, String idProduct, String phone, String randCode, String smsCode, BaseRequestAgent.ResponseListener<EnchashmentCreditResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"insuranceFee", "bankName", "bankNo", "creditAmount", "creditType", "idPerson", "idProduct", "phone", "randCode", "smsCode"}, new Object[]{StringUtils.string2Int(insuranceFee), bankName, bankNo, StringUtils.string2Int(creditAmount), creditType, StringUtils.string2Long(idPerson), StringUtils.string2Long(idProduct), phone, randCode, smsCode});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_addEnchashmentCredit, EnchashmentCreditResponse.class, context, responseListener);
    }

    //取现记录查询
    public static void qxRecords(Activity context, String idPerson, BaseRequestAgent.ResponseListener<CashRecordsResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_enchashment_qxRecords, CashRecordsResponse.class, context, responseListener);
    }

    //创建充值订单
    public static void createRechargeOrder(Activity context, String idPerson, String mobile, long productId, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "mobile", "productId"}, new Object[]{StringUtils.string2Long(idPerson), mobile, productId});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.order_createRechargeOrder, BaseBean.class, context, responseListener);
    }

    //确认充值订单
    public static void confirmRechargeOrder(Activity context, String idPerson, String mobile, long productId, String orderNo, int payType, String smsCode, String smsMobile, BaseRequestAgent.ResponseListener<ConfirmOrderResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "mobile", "productId", "orderNo", "payType", "smsCode", "smsMobile"}, new Object[]{StringUtils.string2Long(idPerson), mobile, productId, orderNo, payType, smsCode, smsMobile});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.order_confirmRechargeOrder, ConfirmOrderResponse.class, context, responseListener);
    }

    //第三方支付成功调用充值
    public static void thirdPayRecharge(Activity context, String idPerson, long orderDetailId, String orderNo, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "orderDetailId", "orderNo"}, new Object[]{idPerson, orderDetailId, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.order_thirdPayRecharge, BaseBean.class, context, responseListener);
    }

    //还款预下单
    public static void createRepaymentOrder(Activity context, String idPerson, long amount, String clientIp, String payChannel, String productType, BaseRequestAgent.ResponseListener<RepaymentActionResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "amount", "clientIp", "payChannel", "productType", "source"}, new Object[]{StringUtils.string2Long(idPerson), amount, clientIp, payChannel, productType, "Android"});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_repayment_createRepaymentOrder, RepaymentActionResponse.class, context, responseListener);
    }

    //获取支持的银行列表
    public static void getUsableBankList(Activity context, BaseRequestAgent.ResponseListener<BankListResponse> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.personCenter_bankCard_getUsableBankList, BankListResponse.class, context, responseListener);
    }

    //绑卡识别(卡bin)
    public static void identifyCard(Activity context, String bankPerson, String cardNo, String idPerson, String ident, BaseRequestAgent.ResponseListener<IdentifyCardResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankPerson", "cardNo", "idPerson", "ident"}, new Object[]{bankPerson, cardNo, StringUtils.string2Long(idPerson), ident});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_identifyCard, IdentifyCardResponse.class, context, responseListener);
    }

    //签约代扣协议(获取验证码)
    public static void agreementApply(Activity context, String bankCode, String bankNo, String idPerson, String phone, BaseRequestAgent.ResponseListener<AgreementApplyResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankCode", "bankNo", "idPerson", "phone"}, new Object[]{bankCode, bankNo, StringUtils.string2Long(idPerson), phone});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_agreementApply, AgreementApplyResponse.class, context, responseListener);
    }

    //新增银行卡
    public static void addBankCard(Activity context, String bankBindPhone, String bankName, String bankNo, String bankPerson, String code, String idPerson, String ident, String isDefault, String orderNo, String payType, String smsSeq, BaseRequestAgent.ResponseListener<AgreementApplyResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"bankBindPhone", "bankName", "bankNo", "bankPerson", "code", "idPerson", "ident", "isDefault", "orderNo", "payType", "smsSeq"}, new Object[]{bankBindPhone, bankName, bankNo, bankPerson, code, StringUtils.string2Long(idPerson), ident, StringUtils.string2Int(isDefault), orderNo, payType, smsSeq});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_bankCard_addBankCard, AgreementApplyResponse.class, context, responseListener);
    }

    //还款结果查询
    public static void payQuery(Activity context, String payId, BaseRequestAgent.ResponseListener<PayQueryResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"payId"}, new Object[]{payId});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.pay_query, PayQueryResponse.class, context, responseListener);
    }

    //异常反馈记录
    public static void queryQuestionInfo(Activity context, String source, String status, int pageNum, String userId, BaseRequestAgent.ResponseListener<FeedBackResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"token", "source", "status", "pageNum", "userId"}, new Object[]{SharePrefUtil.getAppToken(), source, status, pageNum, StringUtils.string2Long(userId)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.helpfeedback_queryQuestionInfo, FeedBackResponse.class, context, responseListener);
    }

    //反馈
    public static void addQuestionMessage(String files, List<String> photoList, int type, String content, String ident, String name,
                                          String nickname, String phone, String userId, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(
                new String[]{"type", "content", "ident", "name", "nickname", "phone", "token", "userId"},
                new Object[]{type, content, ident, name, nickname, phone, SharePrefUtil.getAppToken(), userId});
        FileUpload.uploadFileForOwnPlatformApi(files, photoList, requestParams2, ApiUrl.helpfeedback_addQuestionMessage, BaseBean.class, responseListener);
    }

    //联系人类型
    public static void getContactTypeInfo(Activity context, BaseRequestAgent.ResponseListener<ContactsResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"type"}, new Object[]{2});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.apersonCenter_account_getContactTypeInfo, ContactsResponse.class, context, responseListener);
    }

    //补充其他联系人
    public static void addOtherContact(Activity context, String name, String relation, String idPerson, String phone, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"name", "relation", "idPerson", "phone"}, new Object[]{name, relation, StringUtils.string2Long(idPerson), phone});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_addOtherContact, BaseBean.class, context, responseListener);
    }

    //添加现居住地址
    public static void addLiveAddress(Activity context, String idPerson, String phone, String name, String email, String province, String city, String region, String street, String building, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "phone", "name", "email", "province", "city", "region", "street", "building"},
                new Object[]{StringUtils.string2Long(idPerson), phone, name, email, province, city, region, street, building});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_addLiveAddress, BaseBean.class, context, responseListener);
    }

    //获取省市区
    public static void getAddListJson(Activity context, BaseRequestAgent.ResponseListener<AddressBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{}, new Object[]{});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_address_getAddListJson, AddressBean.class, context, responseListener);
    }

    //获取居住地址信息
    public static void getLiveAddress(Activity context, String idPerson, BaseRequestAgent.ResponseListener<LivingAddressBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_address_getLiveAddress, LivingAddressBean.class, context, responseListener);
    }

    //获取联系人信息
    public static void getOtherContact(Activity context, String idPerson, BaseRequestAgent.ResponseListener<ContactsBean> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson"}, new Object[]{StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.personCenter_account_getOtherContact, ContactsBean.class, context, responseListener);
    }

    //获取优惠券列表
    public static void getCouponList(Activity context, String personId, BaseRequestAgent.ResponseListener<CouponListResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"personId"}, new Object[]{StringUtils.string2Long(personId)});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.act_getCourtesyCardList, CouponListResponse.class, context, responseListener);
    }

    //领取优惠券
    public static void receiveCoupon(Activity context, String personId, String userId, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"personId", "userId"}, new Object[]{personId, userId});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.act_receiveCourtesyCard, BaseBean.class, context, responseListener);
    }

    //获取活动信息
    public static void getActivityInfo(BaseActivity context, String personId, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, String> requestParam2 = BaseRequestAgent.getRequestParams(new String[]{"personId"}, new String[]{personId});
        RequestAgent.getInstance().sendGetRequest(requestParam2, ApiUrl.anniversary_getActivityInfo, BaseBean.class, context, responseListener);

    }

    //获取商品收藏列表
    public static void getCollectionList(Activity context, String idPerson, int pageNum, int pageSize, BaseRequestAgent.ResponseListener<CollectionResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "pageNum", "pageSize"}, new Object[]{"SC", idPerson, pageNum, pageSize});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.collections_goodsSkus_all, CollectionResponse.class, context, responseListener);
    }

    //获取我的订单列表
    public static void getOrderList(Activity context, String channel, String idPerson, String pageNum, String pageSize, String status, BaseRequestAgent.ResponseListener<OrderListResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "pageNum", "pageSize", "status"}, new Object[]{channel, idPerson, pageNum, pageSize, status});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.orderApp_myListOrder, OrderListResponse.class, context, responseListener);
    }

    //删除商品收藏
    public static void deleteCollection(Activity context, String idPerson, List<String> skuCodes, int status, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "skuCodes", "status"}, new Object[]{"SC", idPerson, skuCodes, status});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.collections_goodsSkus_collect, BaseBean.class, context, responseListener);
    }

    //获取用户收货地址列表
    public static void getAddressList(Activity context, String idPerson, String addressType, BaseRequestAgent.ResponseListener<AddressListResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "addressType"}, new Object[]{StringUtils.string2Long(idPerson), addressType});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.personCenter_address_getAddress, AddressListResponse.class, context, responseListener);
    }

    //新增收货地址
    public static void addAddress(Activity context, String address, String addressType, String city, String custName, String idPerson, String isDefault, String phone, String province, String region, String street, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"address", "addressType", "city", "custName", "idPerson", "isDefault", "phone", "province", "region", "street"}, new Object[]{address, addressType, city, custName, StringUtils.string2Long(idPerson), StringUtils.string2Int(isDefault), phone, province, region, street});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.personCenter_address_addAddress, BaseBean.class, context, responseListener);
    }

    //修改收货地址
    public static void updateAddress(Activity context, String address, String addressType, String city, String custName, String id, String idPerson, String isDefault, String phone, String province, String region, String street, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"address", "addressType", "city", "custName", "id", "idPerson", "isDefault", "phone", "province", "region", "street"}, new Object[]{address, addressType, city, custName, StringUtils.string2Long(id), StringUtils.string2Long(idPerson), StringUtils.string2Int(isDefault), phone, province, region, street});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.personCenter_address_updateAddress, BaseBean.class, context, responseListener);
    }

    //删除收货地址
    public static void deleteAddress(Activity context, String id, String idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"id", "idPerson"}, new Object[]{StringUtils.string2Long(id), StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.personCenter_address_deleteAddress, BaseBean.class, context, responseListener);
    }

    //设置默认收货地址
    public static void setDefaultAddress(Activity context, String id, String idPerson, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"id", "idPerson"}, new Object[]{StringUtils.string2Long(id), StringUtils.string2Long(idPerson)});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.personCenter_address_setDefaultAddress, BaseBean.class, context, responseListener);
    }

    //获取订单详情
    public static void getOrderDetail(Activity context, String channel, String idPerson, String orderNo, BaseRequestAgent.ResponseListener<OrderDetailResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo"}, new Object[]{channel, idPerson, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_orderDetail, OrderDetailResponse.class, context, responseListener);
    }

    //删除订单
    public static void deleteOrder(Activity context, String channel, String idPerson, String orderNo, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo"}, new Object[]{channel, idPerson, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_deleteOrder, BaseBean.class, context, responseListener);
    }

    //取消订单
    public static void cancelOrder(Activity context, String channel, String idPerson, String orderNo, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo"}, new Object[]{channel, idPerson, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_cancelOrder, BaseBean.class, context, responseListener);
    }

    //订单跟踪
    public static void getOrderTrace(Activity context, String channel, String idPerson, String orderNo, String src, BaseRequestAgent.ResponseListener<OrderTraceResponse> responseListener) {
        Map<String, Object> requestParam = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo", "src"}, new Object[]{channel, idPerson, orderNo, src});
        RequestAgent.getInstance().sendPostRequest(requestParam, ApiUrl.order_orderLogistics, OrderTraceResponse.class, context, responseListener);
    }

    //充值申请退款
    public static void applyToRefundForRecharge(Activity context, String channel, String idPerson, String orderNo, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo"}, new Object[]{channel, idPerson, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParam, ApiUrl.order_rechargeApplyForRefund, BaseBean.class, context, responseListener);
    }

    //确认支付
    public static void confirmPay(Activity context, String channel, String idPerson, String orderNo, String randomCode, String smsCode, String smsMobile, BaseRequestAgent.ResponseListener<ConfirmPayResponse> responseListener) {
        Map<String, Object> requestParam = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo", "randomCode", "smsCode", "smsMobile"}, new Object[]{channel, idPerson, orderNo, randomCode, smsCode, smsMobile});
        RequestAgent.getInstance().sendPostRequest(requestParam, ApiUrl.orderApp_confirmPay, ConfirmPayResponse.class, context, responseListener);
    }

    //确认收货
    public static void confirmReceive(Activity context, String channel, String idPerson, String orderNo, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "orderNo"}, new Object[]{channel, idPerson, orderNo});
        RequestAgent.getInstance().sendPostRequest(requestParam, ApiUrl.order_confirmReceipt, BaseBean.class, context, responseListener);
    }

    //搜索热词刷新
    public static void refreshHotWords(Activity context, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        RequestAgent.getInstance().sendPostRequest(null, ApiUrl.sc_goods_search_hotword_refresh, BaseBean.class, context, responseListener);
    }

    //一级类目查询子类目(商品分类)
    public static void getChildrenShopTypes(Activity context, int shopTypeId, BaseRequestAgent.ResponseListener<ShopTypesBean> resonseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"shopTypeId"}, new Object[]{shopTypeId});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.sc_goods_shopTypes_childrenShopTypes, ShopTypesBean.class, context, resonseListener);
    }

    //商品介绍
    public static void getSkuIntroduction(Activity context, String idPerson, String channel, String skuCode, BaseRequestAgent.ResponseListener<SkuIntroductionResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"idPerson", "channel", "skuCode"}, new Object[]{StringUtils.string2Long(idPerson), channel, skuCode});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.sc_goods_sku_instruction, SkuIntroductionResponse.class, context, responseListener);
    }

    //商品介绍
    public static void getCommodityDetail(Activity context, String skuCode, String channel, BaseRequestAgent.ResponseListener<CommodityDetailResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"skuCode", "channel"}, new Object[]{skuCode, channel});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.sc_goods_sku_detail, CommodityDetailResponse.class, context, responseListener);
    }

    //商品库存检查
    public static void queryCommodityStock(Activity context, String province, String city, String region, String skuCode, BaseRequestAgent.ResponseListener<BaseBean> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"province", "city", "region", "skuCode"}, new Object[]{province, city, region, skuCode});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.sc_goods_sku_stock, BaseBean.class, context, responseListener);
    }

    public static void queryCommodityInfo(Activity context, String channel, String idPerson, String province, String city, String region, String skuCode, BaseRequestAgent.ResponseListener<CommodityInfoResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "province", "city", "region", "skuCode"}, new Object[]{channel, StringUtils.string2Long(idPerson), province, city, region, skuCode});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.sc_goods_sku_choose, CommodityInfoResponse.class, context, responseListener);
    }

    //商品SKU搜索
    public static void getGoodsSearch(Activity context, String channel, String idPerson, String keyword, String orderSort, int pageNumber, int pageSize, int shopTypeId, BaseRequestAgent.ResponseListener responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "keyword", "orderSort", "pageNumber", "pageSize"}, new Object[]{channel, idPerson, keyword, orderSort, pageNumber, pageSize});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.sc_goods_search_goodsSearch, GoodsSearchResponse.class, context, responseListener);
    }

    //商品库存检查
    public static void getAppDownPayAndMonthPay(Activity context, String channel, String idPerson, int downPaymentRate, String skuCode, BaseRequestAgent.ResponseListener<DownPayMonthPayResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "downPaymentRate", "skuCode"}, new Object[]{channel, StringUtils.string2Long(idPerson), downPaymentRate, skuCode});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_getAppDownPayAndMonthPay, DownPayMonthPayResponse.class, context, responseListener);
    }

    //商城首页
    public static void getShoppingIndex(Activity context, BaseRequestAgent.ResponseListener<IndexResponse> responseListener) {
        Map<String, Object> requestParams2 = BaseRequestAgent.getRequestParamsObject(new String[]{}, new Object[]{});
        RequestAgent.getInstance().sendPostRequest(requestParams2, ApiUrl.sc_goods_skus_index, IndexResponse.class, context, responseListener);
    }
    //创建订单
    public static void createOrderSc(Activity context, String channel, String idPerson, int downPaymentRate, SkuInfo skuInfo, BaseRequestAgent.ResponseListener<CreateOrderResponse> responseListener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "idPerson", "downPaymentRate", "skuInfo"}, new Object[]{channel, StringUtils.string2Long(idPerson), downPaymentRate, skuInfo});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_createOrderSc, CreateOrderResponse.class, context, responseListener);
    }

    //订单确认
    public static void confirmOrderSc(Activity context, String channel, int courtesyCardId, int downPaymentRate, String idPerson, String idProduct, int installDate, int insuranceFee, int payType, CreateOrderResponse.ReceiverJoBean receiverJoBean, int reservingDate, SkuInfo skuInfo, String userComments, String userMobile, String userName, BaseRequestAgent.ResponseListener<ConfirmOrderScResponse> listener) {
        Map<String, Object> requestParam2 = BaseRequestAgent.getRequestParamsObject(new String[]{"channel", "courtesyCardId", "downPaymentRate", "idPerson", "idProduct", "installDate", "insuranceFee", "payType", "receiverJo", "reservingDate", "skuInfo", "userComments", "userMobile", "userName"}, new Object[]{channel, courtesyCardId, downPaymentRate, idPerson, idProduct, installDate, insuranceFee, payType, receiverJoBean, reservingDate, skuInfo, userComments, userMobile, userName});
        RequestAgent.getInstance().sendPostRequest(requestParam2, ApiUrl.order_confirmOrderSc, CreateOrderResponse.class, context, listener);
    }
}





