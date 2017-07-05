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
    public static String getCreditStatusValue(String text) {
        String value = "";
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

    public static String getCreditTypeTotalValue(String text) {
        String value = "";
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
            case "c":
                productType = "取现随借随还";
                break;

            case "o":
                productType = "分期产品";
                break;

            default:
                break;

        }
        return productType;
    }
}
