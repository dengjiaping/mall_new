package com.giveu.shoppingmall.utils;

/**
 * Created by 513419 on 2017/7/4.
 */

/**
 * 合同状态，合同类型的值和文本转换的工具
 */
public class TypeUtlis {


    /**
     * 根据文本值转换成合同状态值
     *
     * @param text
     * @return
     */

    //产品类型
    public static final String CASH = "c";//取现随借随还
    public static final String CERDIT_PRODUCT = "o";//分期产品
    public static final String MONEY_PRODUCT = "v";//豪有钱
    public static final String STUDENT_PRODUCT = "s";//学生贷
    public static final String COMMODITY_PRODUCT = "g";//商品贷
    /**
     * accessToken : fc6b196780757656d4b248c753848982
     * activeDate : 2017/6/23
     * availableCyLimit : 50000
     * availablePosLimit : 50000
     * availableRechargeLimit : 500
     * creditCount : null
     * cyLimit : 50000
     * endDate : 2020/6/1
     * globleLimit : 100000
     * idPerson : 10000923
     * ident : 371082199201058672
     * name : 唐兴
     * nickName : 佳文
     * phone : 18109491314
     * posLimit : 0
     * repayAmount : null
     * repayDate : null
     * status : 1
     * userId : 12
     * userName : 1856989654
     * userPic : user_09.jpg
     */

    public String accessToken;
    public String activeDate;
    public int availableCyLimit;
    public int availablePosLimit;
    public int availableRechargeLimit;
    public Object creditCount;
    public int cyLimit;
    public String endDate;
    public int globleLimit;
    public int idPerson;
    public String ident;
    public String name;
    public String nickName;
    public String phone;
    public int posLimit;
    public String repayAmount;
    public String repayDate;
    public int status;
    public String userId;
    public String userName;
    public String userPic;

    public static String getCreditStatusValue(String text) {
        String value = "";
        if (StringUtils.isNull(text)) {
            return "";
        }
        switch (text) {
            case "未结清":
                value = "a";
                break;
            case "已结清":
                value = "k";
                break;

            case "全部":
                value = "";
                break;
        }
        return value;
    }

    /**
     * 根据合同状态值转换成文本
     *
     * @param value
     * @return
     */
    public static String getCreditStatusText(String value) {
        String text = "";
        if (StringUtils.isNull(value)) {
            return "";
        }
        switch (value) {
            case "a":
                text = "未结清";
                break;
            case "k":
                text = "已结清";
                break;

            case "":
                text = "全部";
                break;
        }
        return text;
    }

    /**
     * 根据合同类型文本转换成合同类型值
     *
     * @param text
     * @return
     */
    public static String getCreditTypeValue(String text) {
        String value = "";
        if (StringUtils.isNull(text)) {
            return "";
        }
        switch (text) {
            case "取现":
                value = "sh";
                break;
            case "现金分期":
                value = "sc";
                break;
            case "商城消费分期":
                value = "sf";
                break;
            case "商城一次性消费":
                value = "sy";
                break;
            case "线下消费":
                value = "ss";
                break;
            case "常规取现分期":
                value = "sq";
                break;
            case "大额取现分期":
                value = "sd";
                break;
            case "全部":
                value = "";
                break;
        }
        return value;
    }

    /**
     * 根据文本值获取对应的代码
     *
     * @param text
     * @return
     */
    public static String getCreditTypeTotalValue(String text) {
        String value = "";
        if (StringUtils.isNull(text)) {
            return "";
        }
        switch (text) {
            case "取现":
                value = "sh";
                break;
            case "现金分期":
                value = "sq,sd";
                break;
            case "商城消费":
                value = "sf,sy";
                break;
            case "线下消费":
                value = "ss";
                break;
            case "全部":
                value = "";
                break;
        }
        return value;
    }


    /**
     * 根据合同类型值转换成合同类型文本
     *
     * @param value
     * @return
     */
    public static String getCreditTypeText(String value) {
        String text = "";
        if (StringUtils.isNull(value)) {
            return "";
        }
        switch (value) {
            case "sh":
                text = "取现";
                break;
            case "sc":
                text = "现金分期";
                break;
            case "sf":
                text = "商城消费分期";
                break;
            case "sy":
                text = "商城一次性消费";
                break;
            case "ss":
                text = "线下消费";
                break;
            case "sq":
                text = "常规取现分期";
                break;
            case "sd":
                text = "大额取现分期";
                break;
            case "":
                text = "全部";
                break;
        }
        return text;
    }

    /**
     * 根据合同类型转换成消费类型
     * 现金分期、商城消费分期、常规取现分期、大额取现分期 返回1
     * 商城一次性消费、线下消费 返回2
     * 随借随还取现 返回3
     *
     * @param creditType 合同类型
     *                   sh:取现，sf:商城消费分期，sy:商城一次性消费，
     *                   ss:线下消费，sq:常规取现分期，sd:大额取现分期
     */
    public static int getConsumType(String creditType) {
        int consumeType = 1;
        if (StringUtils.isNull(creditType)) {
            return consumeType;
        }
        switch (creditType) {
            case "sf":
            case "ss":
            case "sq":
            case "sd":
                consumeType = 1;
                break;
            case "sy":
                consumeType = 2;
                break;
            case "sh":
                consumeType = 3;
        }
        return consumeType;
    }


    /**
     * 将【v--豪有钱】【s--学生贷】【g--商品贷】归类为：o：分期产品
     *
     * @param type
     * @return
     */
    public static String convertProductType(String type) {
        String productType = type;
        if (StringUtils.isNull(type)) {
            return "";
        }
        switch (type) {
            case CERDIT_PRODUCT:
            case MONEY_PRODUCT:
            case STUDENT_PRODUCT:
            case COMMODITY_PRODUCT:
                productType = CERDIT_PRODUCT;
                break;

            case CASH:
                productType = CASH;
                break;

            default:
                break;

        }
        return productType;
    }

    /**
     * 根据字符串返回产品类型
     *
     * @param type
     * @return
     */
    public static String getProductType(String type) {
        String productType = type;
        if (StringUtils.isNull(type)) {
            return "";
        }
        switch (type) {
            case CASH:
                productType = "取现随借随还";
                break;

            case CERDIT_PRODUCT:
                productType = "分期产品";
                break;

            default:
                break;

        }
        return productType;
    }

    /**
     * 根据值获取对应的运营商
     * @param value
     * @return
     */
    public static String getOperatorStr(int value){
        String operatorStr ="未知运营商";
        switch (value){
            case 0:
                operatorStr ="中国移动";
                break;
            case 1:
                operatorStr ="中国联通";
                break;
            case 2:
                operatorStr ="中国电信";
                break;
            default:
                break;
        }
        return  operatorStr;
    }

}
