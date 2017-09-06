package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.utils.LogUtil;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.utils.ToastUtils;
import com.giveu.shoppingmall.utils.WheelGetTimeUtil;
import com.giveu.shoppingmall.widget.wheelview.OnWheelChangedListener;
import com.giveu.shoppingmall.widget.wheelview.WheelView;
import com.giveu.shoppingmall.widget.wheelview.adapter.NumericWheelAdapter;

import java.util.Calendar;


/**
 * 选择日期dialog
 */
public class DateSelectDialog extends CustomDialog {
    private String selectDate = "";
    private WheelView wl_year, wl_month, w1_day;
    private TextView tvTitle;
    private String currentTime;
    private int currentYear;
    private int currentMonth;
    private int currentDay;
    private int currentSelectYear, currentSelectMonth, currentSelectDay;
    private final int MIN_YEAR = 2000;
    private final int MIN_MONTH = 1;
    private final int MIN_DAY = 1;
    private int maxDay;
    public int mYear, mMonth;
    private boolean isShowDay = true;
    private boolean isLimitSelect = true;
    private NumericWheelAdapter numericWheelAdapterStart3;

    public DateSelectDialog(Activity context) {
        super(context, R.layout.dialogl_date_select, R.style.date_dialog_style, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        DisplayMetrics metrics = new DisplayMetrics();
        mAttachActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        LogUtil.e(MIN_YEAR + "");
        LogUtil.e(isShowDay + "");

        currentTime = WheelGetTimeUtil.getYMDTimezh(System.currentTimeMillis());
        currentYear = Integer.valueOf(currentTime.substring(0, 4));
        currentMonth = Integer.valueOf(currentTime.substring(5, 7));
        currentDay = Integer.valueOf(currentTime.substring(8, 10));
        currentSelectYear = currentYear;
        currentSelectMonth = currentMonth;
        currentSelectDay = currentDay;
        mYear = currentYear;
        mMonth = currentMonth;
        selectDate = currentTime;
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_right);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_left);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_title);
        initWheelView(contentView);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectYear = wl_year.getCurrentItem() + MIN_YEAR;//年
                currentSelectMonth = wl_month.getCurrentItem() + MIN_MONTH;//月
                currentSelectDay = w1_day.getCurrentItem() + MIN_DAY;//日
                selectDate = String.valueOf(currentSelectYear) + String.format("%02d", currentSelectMonth) + String.format("%02d", currentSelectDay);

                long selectTimeMillis = WheelGetTimeUtil.getTimeStamp(selectDate, "yyyyMMdd");
                if (selectTimeMillis > System.currentTimeMillis() && isLimitSelect) {
                    selectErrorDate();
                    return;
                }

                selectRightDate(String.valueOf(currentSelectYear), String.format("%02d", currentSelectMonth), String.format("%02d", currentSelectDay));
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    /**
     * 是否做选择时间的限制
     *
     * @param limit
     */
    public void setLimitSelect(boolean limit) {
        isLimitSelect = limit;
    }

    private void selectRightDate(String year, String month, String day) {
        dismiss();
        if (listener != null) {
            listener.onSelectDate(year, month, day);
        }
    }

    private void selectErrorDate() {
        ToastUtils.showShortToast("请选择正确的日期");
    }

    protected void initWheelView(View view) {
        final Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDay = c.get(Calendar.DATE);

        wl_year = (WheelView) view.findViewById(R.id.wl1);
        wl_month = (WheelView) view.findViewById(R.id.wl2);
        w1_day = (WheelView) view.findViewById(R.id.wl3);

        NumericWheelAdapter numericWheelAdapterStart1 = new NumericWheelAdapter(mAttachActivity, MIN_YEAR, 2030);
        numericWheelAdapterStart1.setLabel("年");
        wl_year.setViewAdapter(numericWheelAdapterStart1);
        wl_year.setCyclic(true);//是否可循环滑动

        NumericWheelAdapter numericWheelAdapterStart2 = new NumericWheelAdapter(mAttachActivity, MIN_MONTH, 12, "%02d");
        numericWheelAdapterStart2.setLabel("月");
        wl_month.setViewAdapter(numericWheelAdapterStart2);
        wl_month.setCyclic(true);

        //初始最大天数
        updateDayWheel(currentYear, currentMonth);
        numericWheelAdapterStart3 = new NumericWheelAdapter(mAttachActivity, MIN_DAY, maxDay, "%02d");
        numericWheelAdapterStart3.setLabel("日");
        w1_day.setViewAdapter(numericWheelAdapterStart3);
        w1_day.setCyclic(true);
        wl_year.setCurrentItem(curYear - MIN_YEAR);
        wl_month.setCurrentItem(curMonth - MIN_MONTH);
        w1_day.setCurrentItem(curDay - MIN_DAY);
        w1_day.setVisibility(View.GONE);
        wl_year.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year = MIN_YEAR + newValue;
                mYear = year;
                updateDayWheel(year, mMonth);
                numericWheelAdapterStart3.setMaxValue(maxDay);
            }
        });
        wl_month.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month = newValue + 1;
                mMonth = month;
                updateDayWheel(mYear, month);
                numericWheelAdapterStart3.setMaxValue(maxDay);
            }
        });
    }

    /**
     * 是否显示日
     * 默认是不显示的，只显示年月
     */
    public void showDay() {
        isShowDay = true;
        w1_day.setVisibility(View.VISIBLE);
    }

    public void hideDay() {
        isShowDay = false;
        w1_day.setVisibility(View.GONE);
    }

    public String getCurrentYearAndMonth(String dividerFlag) {
        if (dividerFlag == null || dividerFlag.isEmpty()) {
            return currentSelectYear + "年" + String.format("%02d", (currentSelectMonth)) + "月";
        } else {
            return currentSelectYear + dividerFlag + String.format("%02d", currentSelectMonth);
        }
    }

    public String getCurrentYearAndMonthAndDay(String dividerFlag) {
        if (dividerFlag == null || dividerFlag.isEmpty()) {
            return currentSelectYear + "年" + String.format("%02d", currentSelectMonth) + "月" + String.format("%02d", currentSelectDay) + "日";
        } else {
            return currentSelectYear + dividerFlag + String.format("%02d", currentSelectMonth) + dividerFlag + String.format("%02d", currentSelectDay);
        }
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        if (StringUtils.isNotNull(title)) {
            tvTitle.setText(title);
        }
    }


    public void setOriginalDate(int year, int month, int day) {
        if (wl_year != null && wl_month != null && w1_day != null) {
            wl_year.setCurrentItem(year - MIN_YEAR);
            wl_month.setCurrentItem(month - MIN_MONTH);
            w1_day.setCurrentItem(day - MIN_DAY);
            currentSelectYear = year;
            currentSelectMonth = month;
            currentSelectDay = day;
        }
    }

    public void setOriginalDate() {
        if (wl_year != null && wl_month != null && w1_day != null) {
            wl_year.setCurrentItem(currentYear - MIN_YEAR);
            wl_month.setCurrentItem(currentMonth - MIN_MONTH);
            w1_day.setCurrentItem(currentDay - MIN_DAY);
            currentSelectYear = currentYear;
            currentSelectMonth = currentMonth;
            currentSelectDay = currentDay;
        }
    }

    /**
     * 更新天数的数据
     */
    private void updateDayWheel(int year, int month) {
        //日期的判断（闰年以及每年日期数的不同）

        boolean leayyear = false;
        if (year % 4 == 0 && year % 100 != 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (month) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    maxDay = 31;
                    break;
                case 2:
                    if (leayyear) {
                        maxDay = 29;
                    } else {
                        maxDay = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    maxDay = 30;
                    break;
            }
        }
    }

    public void setOnDateSelectListener(OnDateSelectListener listener) {
        this.listener = listener;
    }

    private OnDateSelectListener listener;

    public interface OnDateSelectListener {
        void onSelectDate(String year, String month, String day);
    }
}
