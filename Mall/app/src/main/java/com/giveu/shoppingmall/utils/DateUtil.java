package com.giveu.shoppingmall.utils;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.text.TextUtils;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    interface Pattern{
        String yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";
        String HHmmss = "HH:mm:ss";
        String HHmm = "HH:mm";
        String yyyyMMddHHmmss = "yyyyMMddHHmmss";
        String yyyy_MM_dd = "yyyy-MM-dd";
        String yyyyMMdd = "yyyyMMdd";
        String yyyyMM = "yyyyMM";
        String zh_yyyy_MM_dd = "yyyy年MM月dd日";
    }


    public static String getzh_yyyy_MM_dd(long timeMillis) {
        return getTimeStrByPattern(Pattern.zh_yyyy_MM_dd, new Date(timeMillis));
    }


    public static String getCurrentTime2yyyyMMddHHmmss() {
        return getTimeStrByPattern(Pattern.yyyyMMddHHmmss, new Date());
    }

    public static String getCurrentTime2yyyyMMdd() {
        return getTimeStrByPattern(Pattern.yyyyMMdd, new Date());
    }
    public static String getCurrentTime2yyyyMM() {
        return getTimeStrByPattern(Pattern.yyyyMM, new Date());
    }

    public static String getyyyy_MM_dd(Date date) {
        return getTimeStrByPattern(Pattern.yyyy_MM_dd, date);
    }

    public static String getTimeStrByPattern(String patternStr, Date date) {
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat(patternStr);
            return dateFormat.format(date);
        }catch (Exception e){
            e.printStackTrace();
        }
       return "";
    }

    public static String getyyyy_MM_dd(long time) {
        return getyyyy_MM_dd(new Date(time));
    }

    public static String getYyyy_MM_dd_HHmmss(long time) {
        return getTimeStrByPattern(Pattern.yyyy_MM_dd_HHmmss, new Date(time));
    }

    public static String getHHmm(long time) {
        return getTimeStrByPattern(Pattern.HHmm, new Date(time));
    }


    private static boolean isUse;// 是否已经调用过了
    /**
     * 选择日期对话框：
     */
    public static void showDatePickerDialog(final Context context, String initBirth, final OnSelectDateListener onSelectDateListener) {
        if (initBirth == null || "".equals(initBirth)) {
            initBirth = getyyyy_MM_dd(System.currentTimeMillis());
        }
        int chooseYear1 = 0;
        int chooseMonth1 = 0;
        int chooseDay1 = 0;
        try {
            chooseYear1 = Integer.parseInt(initBirth.substring(0, 4));
            if ("0".equals(initBirth.charAt(5) + "")) {
                chooseMonth1 = Integer.parseInt(initBirth.substring(6, 7));
            } else {
                chooseMonth1 = Integer.parseInt(initBirth.substring(5, 7));
            }
            if ("0".equals(initBirth.charAt(8) + "")) {
                chooseDay1 = Integer.parseInt(initBirth.substring(9, 10));
            } else {
                chooseDay1 = Integer.parseInt(initBirth.substring(8, 10));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        isUse = false;

        /**
         * 系统日期选择的dialog
         */
        DatePickerDialog datePicker = new DatePickerDialog(context, new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String mYear = year + "";
                String month = addZreoIfLessThanTen(monthOfYear + 1);
                String day = addZreoIfLessThanTen(dayOfMonth);
                if (!isUse) {
                    if (onSelectDateListener != null) {
                        onSelectDateListener.onSelectDate(mYear, month, day);
                    }
                    isUse = true;
                }
            }
        }, chooseYear1, (chooseMonth1 - 1), chooseDay1);
        datePicker.show();
    }


    public interface OnSelectDateListener {
         void onSelectDate(String year, String month, String day);
    }

    /**
     * 如果i小于10，添�?后生成string
     *
     * @param i
     * @return
     */
    public static String addZreoIfLessThanTen(int i) {
        String string = "";
        if (i < 10) {
            string = "0" + i;
        } else {
            string = i + "";
        }
        return string;
    }

    /**
     * 比较两个日期，并返回天数，如果结束日期小于当前日期，endTimes < startTimes返回-1
     *
     * @param startTime 当前日期
     * @param endTime   结束日期
     * @return
     */
    public static int scaleDate(String startTime, String endTime) {
        long startTimes = 0;
        long endTimes = 0;
        try {
            if (!TextUtils.isEmpty(startTime)) {
                startTimes = new SimpleDateFormat(Pattern.yyyy_MM_dd_HHmmss).parse(startTime).getTime();
            }
            if (!TextUtils.isEmpty(endTime)) {
                endTimes = new SimpleDateFormat(Pattern.yyyy_MM_dd_HHmmss).parse(endTime).getTime();
            }

        } catch (Exception e) {

        }
        if (endTimes < startTimes) { // 结束日期 小于当前日期 活动结束，返回 -1
            return -1;
        } else {
            long times = endTimes - startTimes;
            int day = (int) (times / 24 / 60 / 60 / 1000);
            day = day == 0 ? 1 : day;
            return day;
        }
    }

    public static long parseYyyy_MM_ddStr2Millis(String str){
        long startTimes = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                startTimes = new SimpleDateFormat(Pattern.yyyy_MM_dd).parse(str).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startTimes;
    }
    public static long parseYyyy_MM_dd_HHmmssStr2Millis(String str){
        long startTimes = 0;
        try {
            if (!TextUtils.isEmpty(str)) {
                startTimes = new SimpleDateFormat(Pattern.yyyy_MM_dd_HHmmss).parse(str).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return startTimes;
    }


}
