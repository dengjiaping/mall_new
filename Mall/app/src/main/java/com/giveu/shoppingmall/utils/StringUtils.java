package com.giveu.shoppingmall.utils;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.EditText;
import android.widget.TextView;

import com.giveu.shoppingmall.base.BaseApplication;
import com.giveu.shoppingmall.base.web.BaseWebViewActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StringUtils {


    /**
     * @param string
     * @return
     */
    public static boolean isNotNull(String string) {
        if (null != string && !"".equals(string.trim())) {
            return true;
        }
        return false;
    }

    /**
     * @param string
     * @return
     */
    public static boolean isNull(String string) {
        if (null == string || "".equals(string.trim())) {
            return true;
        }
        return false;
    }

    /**
     * 验证是否是11位数字
     *
     * @param phoneNum
     * @return
     */
    public static boolean checkPhoneNumberAndTipError(String phoneNum, boolean showToast) {
        if (!Validator.isMobile(phoneNum)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入正确的手机号码");
            }
            return false;
        }

        return true;
    }

    /**
     * 验证是否是19位数字
     *
     * @param password
     * @return
     */
    public static boolean isCardNum(String password) {
        Pattern p = Pattern.compile("^0?\\d{19}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }


    /**
     * 保存地址json至本地
     *
     * @param context
     * @param addressJson
     */
    public static void saveAddress(Context context, String addressJson) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = context.openFileOutput("addressJson", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(addressJson);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取地址Json
     *
     * @param context
     * @return
     */
    public static String loadAddress(Context context) {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = context.openFileInput("addressJson");//文件名
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }

    /**
     * 验证邮箱格式是否正确
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static SpannableString getColorSpannable(CharSequence str1, final String str2, int str1ColorId, final int str2ColorId) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(str1ColorId)), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(str2ColorId)), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSizeSpannable(CharSequence str1, final String str2, int str1Dip, final int str2Dip) {
        String str3 = str1 + str2;
        SpannableString msp = new SpannableString(str3);
        msp.setSpan(new AbsoluteSizeSpan(str1Dip, true), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(str2Dip, true), str1.length(), str3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSizeAndColorSpannable(CharSequence str1, final String str2, final String str3, final String str4, int str1Dip, final int str2Dip, int str1ColorId, final int str2ColorId) {
        String str5 = str1 + str2 + str3 + str4;
        SpannableString msp = new SpannableString(str5);
        msp.setSpan(new AbsoluteSizeSpan(str2Dip, true), 0, str1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(str1Dip, true), str1.length(), (str1 + str2).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new AbsoluteSizeSpan(str2Dip, true), (str1 + str2).length(), (str1 + str2 + str3 + str4).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(str1ColorId)), 0, (str1 + str2 + str3).length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp.setSpan(new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(str2ColorId)), (str1 + str2 + str3).length(), str5.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msp;
    }

    public static SpannableString getSizeAndColorSpannable(CharSequence str, int colorId, int size) {
        SpannableString msg = new SpannableString(str);
        msg.setSpan(new ForegroundColorSpan(BaseApplication.getInstance().getResources().getColor(colorId)), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msg.setSpan(new AbsoluteSizeSpan(size, true), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return msg;
    }

    /**
     * 保留小数
     *
     * @return
     */
    public static String format2(Float value) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(value);
    }

    /**
     * 保留小数
     *
     * @return
     */
    public static String format2(String value) {
        try {
            double dv = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("0.00");
            df.setRoundingMode(RoundingMode.HALF_UP);
            return df.format(dv);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 昵称是否含有特殊字符
     */
    public static boolean checkUserNameAndTipError(String nickname, boolean showToast) {
        if (TextUtils.isEmpty(nickname)) {
            if (showToast) {
                ToastUtils.showShortToast("请填写姓名");
            }
            return false;
        }
        if (nickname.length() < 2 || nickname.length() > 18) {
            if (showToast) {
                ToastUtils.showShortToast("请输入不少于2位的中文姓名");
            }
            return false;
        }
        if (nickname.matches("^[A-Z|a-z]*$")) {
            if (showToast) {
                ToastUtils.showShortToast("请输入中文姓名");
            }
            return false;
        } else if (!nickname.matches("[\\u4e00-\\u9fa5]{1,14}[\\?•·・∙]{0,1}[\\u4e00-\\u9fa5]{1,13}+$")) {
            if (showToast) {
                ToastUtils.showShortToast("仅支持2-18个中文字符和姓名中间的圆点");
            }
            return false;
        }
        return true;
    }

    /**
     * 检查登录密码如果不合法提示错误信息并返回false
     *
     * @param pwd
     * @return
     */
    public static boolean checkLoginPwdAndTipError(String pwd, boolean showToast) {
        if (TextUtils.isEmpty(pwd)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入密码");
            }
            return false;
        }

        if (pwd.length() < 8 || pwd.length() > 16) {
            if (showToast) {
                ToastUtils.showShortToast("请输入8~16位密码");
            }
            return false;
        }

        if (!Validator.isLoginPassword(pwd)) {
            if (showToast) {
                ToastUtils.showShortToast("密码要是数字和字母的组合");
            }
            return false;
        }

        return true;
    }

    /**
     * 检查“交易密码”如果不合法提示错误信息并返回false
     *
     * @param pwd
     * @return
     */
    public static boolean checkTransactionPwdAndTipError(String pwd) {
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.showShortToast("请输入交易密码");
            return false;
        }
        if (!Validator.isTransactionPassword(pwd)) {
            ToastUtils.showShortToast("请输入6位数字的交易密码");
            return false;
        }
        return true;
    }

    public static boolean checkIdCardAndTipError(String idcard, boolean showToast) {
        if (TextUtils.isEmpty(idcard)) {
            if (showToast) {
                ToastUtils.showShortToast("请输入身份证！");
            }
            return false;
        }
        if (idcard.length() != 15 && idcard.length() != 18) {
            if (showToast) {
                ToastUtils.showShortToast("请输入正确位数的身份证！");
            }
            return false;
        }
        return true;
    }

    /**
     * 解析 输入的文字里有无url
     *
     * @param rawUrl
     * @return
     */
    public static String getReloveUrl(String rawUrl) {
        String includeUrl = null;
        if (!rawUrl.contains("://")) {
            rawUrl = "http://" + rawUrl;
        }
        Pattern urlPattern = Pattern.compile(BaseWebViewActivity.urlRegularExpression, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = urlPattern.matcher(rawUrl);
        while (urlMatcher.find()) {
            includeUrl = rawUrl.subSequence(urlMatcher.start(), urlMatcher.end()).toString();
            /**
             * 最后有表情 [] 过滤掉
             */
            if (StringUtils.isNotNull(includeUrl) && includeUrl.length() > 1) {
                String lastChar = includeUrl.charAt(includeUrl.length() - 1) + "";
                if ("[".equals(lastChar)) {
                    includeUrl = includeUrl.substring(0, includeUrl.length() - 1);
                }
            }
        }
        return includeUrl;
    }

    /**
     * 将null转成“”
     */
    public static String nullToEmptyString(String str) {
        return str == null ? "" : str;
    }


    public static String getTextFromView(EditText etName) {
        return etName == null ? "" : etName.getText().toString().trim();
    }

    public static String getTextFromView(TextView etName) {
        return etName == null ? "" : etName.getText().toString().trim();
    }

    /**
     * 将"2016-11-26T00:00:00"变成2016/11/26
     *
     * @return
     */
    public static String billFormatDate(String promiseRepayDate) {
        String result = "";
        if (isNotNull(promiseRepayDate)) {
            String[] ts = promiseRepayDate.split("T");
            if (ts.length == 2) {
                String[] split = ts[0].split("-");
                if (split.length == 3) {
                    result = split[0] + "/" + split[1] + "/" + split[2];
                }
            }
        }
        return result;
    }

    public static String formatDate(long orgianlDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(new Date(orgianlDate));
    }

    /**
     * 将"2016-11-26T00:00:00"变成2016-11-26 00:00
     *
     * @return
     */
    public static String cashFormatDate(String promiseRepayDate) {
        String result = "";
        if (isNotNull(promiseRepayDate)) {
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                result = df1.format(df2.parse(promiseRepayDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return result;
    }

    public static String convertTime(String timeStamp) {
        String result = "";
        if (isNotNull(timeStamp)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            try {
                result = format.format(new Date(Long.parseLong(timeStamp)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String formatRestTime(long restTime) {
        String result = "";
        if (restTime != 0) {
            SimpleDateFormat format = new SimpleDateFormat("mm分ss秒");
            try {
                result = format.format(new Date(restTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    public static String formatRestTimeToDay(long restTime) {
        String result = "";
        if (restTime != 0) {
            SimpleDateFormat format = new SimpleDateFormat("dd天 HH小时");
            try {
                result = format.format(new Date(restTime));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //2016/11/26转成11/26
    public static String transactionSearchDate(String sourceDate) {
        String repayDate = "";
        if (StringUtils.isNotNull(sourceDate)) {
            repayDate = sourceDate;
            if (repayDate.length() > 2) {
                repayDate = repayDate.substring(repayDate.indexOf("/") + 1, repayDate.length());
            }
        }
        return repayDate;
    }

    public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == ' ') {
                c[i] = '\u3000';
            } else if (c[i] < '\177') {
                c[i] = (char) (c[i] + 65248);

            }
        }
        return new String(c);
    }

    //解析数据传参类型转换异常捕获（转double）
    public static double string2Double(String doubleStr) {
        double D = 0;
        try {
            D = Double.parseDouble(doubleStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return D;
    }

    //解析数据传参类型转换异常捕获（转int）
    public static int string2Int(String intStr) {
        int I = 0;
        try {
            I = Integer.parseInt(intStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return I;
    }

    //解析数据传参类型转换异常捕获（转long）
    public static long string2Long(String longStr) {
        long L = 0;
        try {
            L = Long.parseLong(longStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return L;
    }

    /**
     * 解决TextView自动换行文字排版参差不齐的原因
     *
     * @param input
     * @return
     */
    public static String ToAllFullWidthString(String input) {
        try {
            if (!TextUtils.isEmpty(input)) {
                char[] c = input.toCharArray();
                for (int i = 0; i < c.length; i++) {
                    if (c[i] == 12288) {
                        c[i] = (char) 32;
                        continue;
                    }
                    if (c[i] > 65280 && c[i] < 65375)
                        c[i] = (char) (c[i] - 65248);
                }
                return new String(c);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 通过传入textView解决中英混排
     * @param tv
     */
    public static void autoSplitText(final TextView tv) {
        tv.post(new Runnable() {
            @Override
            public void run() {
                final String rawText = tv.getText().toString(); //原始文本
                final Paint tvPaint = tv.getPaint(); //paint，包含字体等信息
                final float tvWidth = tv.getWidth() - tv.getPaddingLeft() - tv.getPaddingRight(); //控件可用宽度

                //将原始文本按行拆分
                String[] rawTextLines = rawText.replaceAll("\r", "").split("\n");
                StringBuilder sbNewText = new StringBuilder();
                for (String rawTextLine : rawTextLines) {
                    if (tvPaint.measureText(rawTextLine) <= tvWidth) {
                        //如果整行宽度在控件可用宽度之内，就不处理了
                        sbNewText.append(rawTextLine);
                    } else {
                        //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                        float lineWidth = 0;
                        for (int cnt = 0; cnt != rawTextLine.length(); ++cnt) {
                            char ch = rawTextLine.charAt(cnt);
                            lineWidth += tvPaint.measureText(String.valueOf(ch));
                            if (lineWidth <= tvWidth) {
                                sbNewText.append(ch);
                            } else {
                                sbNewText.append("\n");
                                lineWidth = 0;
                                --cnt;
                            }
                        }
                    }
                    sbNewText.append("\n");
                }

                //把结尾多余的\n去掉
                if (!rawText.endsWith("\n")) {
                    sbNewText.deleteCharAt(sbNewText.length() - 1);
                }
                tv.setText(sbNewText.toString());
            }
        });
    }
        /**
         * 字符串中是否包含表情或特殊符号
         */
    public static boolean contailEmoji(String string) {
        Pattern p = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(string);
        return m.find();
    }
}
