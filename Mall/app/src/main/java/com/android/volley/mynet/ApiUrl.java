package com.android.volley.mynet;

import com.giveu.shoppingmall.base.DebugConfig;

public interface ApiUrl {

    /**
     * 线上地址前缀
     */
    String BASE_URL = DebugConfig.getBaseUrl();
    String H5_BASE_URL = DebugConfig.getH5BaseUrl();

    interface WebUrl {
        String zhi_ma_xin_yong = "http://zhima.dafysz.com/Home/SignIn?idenNo=%s&name=%s&idCredit=0&deviceType=0";//芝麻信用
        String NEWInsuredNotice = H5_BASE_URL + "index.html#/NEW/NEWInsuredNotice";// 保险包计划20161229
        String mobileHealthService = H5_BASE_URL + "index.html#/NEW/mobileHealthService";//手机健康服务
        String noWorryReturnNotice = H5_BASE_URL + "index.html#/NEW/noWorryReturnNotice";//:权益包20160105
        String creditAuthor = H5_BASE_URL + "index.html#/NEW/creditAuthor";//:征信授权条款
        String yun_ying_shang = H5_BASE_URL + "index.html#/startCollect";//运营商
    }

    //广告页
    String activity_getImageInfo = BASE_URL + "activity/getImageInfo";
    String token_getToken = BASE_URL + "token/getToken";
    //修改密码
    String sales_account_changePwd = BASE_URL + "sales/account/changePwd";
    //验证密码
    String sales_account_checkPwd = BASE_URL + "sales/account/checkPwd";
    //登录
    String sales_account_login = BASE_URL + "sales/account/login";
    //意见反馈
    String sales_account_feedback = BASE_URL + "sales/account/feedback";

    //商品门店
    String sales_creditApply_getSellerPlace = BASE_URL + "sales/creditApply/getSellerPlace";
    //获取城市区域
    String sales_base_getRegions = BASE_URL + "sales/base/getRegions";
    //获取学校信息
    String sales_base_getUnivercity = BASE_URL + "sales/base/getUnivercity";
    //获得相关基本信息
    String sales_base_getBaseInfo = BASE_URL + "sales/base/getBaseInfo";
    //获得省市信息列表
    String sales_base_provinceCityInfo = BASE_URL + "sales/base/provinceCityInfo";
    //保存其他信息
    String sales_creditApply_saveOtherInfo = BASE_URL + "sales/creditApply/saveOtherInfo";
    //保存基本信息
    String sales_creditApply_saveCreditBaseInfo = BASE_URL + "sales/creditApply/saveCreditBaseInfo";
    //保存单位信息
    String sales_creditApply_saveCreditCompanyInfo = BASE_URL + "sales/creditApply/saveCreditCompanyInfo";
    //保存学校信息
    String sales_creditApply_saveCreditSchoolInfo = BASE_URL + "sales/creditApply/saveCreditSchoolInfo";
    //查询分期产品
    String sales_creditApply_searchProduct = BASE_URL + "sales/creditApply/searchProduct";
    //获取产品/商品类型
    String sales_creditApply_getProductType = BASE_URL + "sales/creditApply/getProductType";
    //获取产品（商品）型号
    String sales_creditApply_getGoodsModel = BASE_URL + "sales/creditApply/getGoodsModel";
    //获取品牌型号（商品品牌）
    String sales_creditApply_getBrandsModel = BASE_URL + "sales/creditApply/getBrandsModel";
    //获取商品小类
    String sales_creditApply_getGoodsSubType = BASE_URL + "sales/creditApply/getGoodsSubType";
    //保存银行卡信息
    String sales_creditApply_saveBankInfo = BASE_URL + "sales/creditApply/saveBankInfo";
    //获取合同需要上传的照片
    String sales_creditApply_getRequiredPhotos = BASE_URL + "sales/creditApply/getRequiredPhotos";
    //上传影像证明
    String sales_creditApply_uploadOnePhoto = BASE_URL + "sales/creditApply/uploadOnePhoto";
    // 保存联系人信息
    String sales_creditApply_saveCreditLinkInfo = BASE_URL + "sales/creditApply/saveCreditLinkInfo";

    //生成订单
    String sales_creditApply_createCredit = BASE_URL + "sales/creditApply/createCredit";
    //提交合同
    String sales_creditApply_submitCredit = BASE_URL + "sales/creditApply/submitCredit";
    //查看合同详情的微信二维码
    String sales_credit_bindWechat = BASE_URL + "sales/credit/bindWechat";
    //获取保存的信息（订单详情）
    String sales_creditApply_getBaseInfo = BASE_URL + "sales/creditApply/getBaseInfo";
    //取消合同
    String sales_credit_cancelCredit = BASE_URL + "sales/credit/cancelCredit";
    //纠错备注
    String sales_credit_addErrorRepairRemark = BASE_URL + "sales/credit/addErrorRepairRemark";
    //查询合同列表
    String sales_credit_search = BASE_URL + "sales/credit/search";
    //查询合同详情
    String sales_credit_creditDetail = BASE_URL + "sales/credit/creditDetail";
    //PDF预览列表
    String sales_credit_pdfView = BASE_URL + "sales/credit/pdfView";
    //获取客户信息
    String sales_credit_customerInfo = BASE_URL + "sales/credit/customerInfo";
    //获取每月还款列表
    String sales_overdueCredit_eduRepaymentPlan = BASE_URL + "sales/overdueCredit/eduRepaymentPlan";
    //确认注册
    String sales_credit_confirmRegiste = BASE_URL + "sales/credit/confirmRegiste";
    //银行卡补正
    String credit_modiyBankInfo = BASE_URL + "sales/credit/modiyBankInfo";

    //更新商品串码
    String sales_credit_updateIMIE = BASE_URL + "sales/credit/updateIMIE";
    //获取图片列表
    String sales_credit_getPhotos = BASE_URL + "sales/credit/getPhotos";
    //获取补传照片类型
    String sales_creditApply_getSupplementPhotos = BASE_URL + "sales/creditApply/getSupplementPhotos";
    String sales_credit_downloadFile = BASE_URL + "sales/credit/downloadFile";

    //获取合同协议地址
    String sales_overdueCredit_getPageUrl = BASE_URL + "sales/overdueCredit/getPageUrl";
    //逾期合同查询
    String sales_overdueCredit_search = BASE_URL + "sales/overdueCredit/search";
    //文件质量查询
    String sales_fileQuality_search = BASE_URL + "sales/fileQuality/search";
    //关键错误补签

    String sales_creditDetail_retroactive = BASE_URL + "sales/creditDetail/retroactive";
    String sales_creditDetail_getSignAddress = BASE_URL + "sales/creditDetail/getSignAddress";
    //身份证地址解析
    String sales_creditApply_getIdAdddress = BASE_URL + "sales/creditApply/getIdAdddress";
    //确认补传
    String sales_creditApply_confirmSupplement = BASE_URL + "sales/creditApply/confirmSupplement";
    //获取相邻城市
    String sales_base_getAdjacentCities = BASE_URL + "sales/base/getAdjacentCities";
    //删除影像证明图片
    String sales_creditApply_delPhoto = BASE_URL + "sales/creditApply/delPhoto";
    //获取保险金额
    String sales_creditApply_getServisesFee = BASE_URL + "sales/creditApply/getServisesFee";
    //验证是否是信用卡
    String sales_credit_checkCreditCard = BASE_URL + "sales/credit/checkCreditCard";
    //保存通讯录
    String sales_account_saveAddressBook = BASE_URL + "sales/account/saveAddressBook";
    //保存头像
    String sales_account_savePortrait = BASE_URL + "sales/account/savePortrait";
    //我的团队
    String sales_account_getMyTeam = BASE_URL + "sales/account/getMyTeam";
    //我的门店
    String sales_credit_performanceRs = BASE_URL + "sales/credit/performanceRs";
    // 获取门店城市信息
    String sales_base_getCityList = BASE_URL + "sales/base/getCityList";
    //个人基本数据
    String sales_account_getInfo = BASE_URL + "sales/account/getInfo";
    //保存签到
    String api_Attendance_SaveAttendance = "/api/Attendance/SaveAttendance";
    String sales_creditDetail_saveAttendance = BASE_URL + "sales/creditDetail/saveAttendance";
    //团队列表
    String api_Attendance_UserList = "/api/Attendance/UserList";
    //未签到列表
    String api_Attendance_AttendanceListNotSign = "/api/Attendance/AttendanceListNotSign";
    //签到列表
    String api_Attendance_AttendanceList = "/api/Attendance/AttendanceList";
    //签到列表SA
    String api_Attendance_AttendanceListForSA = "/api/Attendance/AttendanceListForSA";
    //公用请求蜂鸟接口
    String common_url = BASE_URL + "sales/public/commonsMethod";
    //公用请求蜂鸟path对应的key
    String COMMON_KEY = "commonsMethod_path";

    //消息列表
    String api_Attendance_BulletinList = "/api/Attendance/BulletinList";
    //修改消息状态
    String api_Attendance_UpdateBulletin = "/api/Attendance/UpdateBulletin";
    //保存极光设备号
    String api_Account_SaveDeviceNumber = "/api/Account/SaveDeviceNumber";
}
