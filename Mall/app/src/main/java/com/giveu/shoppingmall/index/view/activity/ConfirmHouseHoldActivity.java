package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.dialog.DateSelectDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/5.
 * 大家电配送
 */

public class ConfirmHouseHoldActivity extends BaseActivity {
    private static String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    private final static int TYPE_SEND = 0;
    private final static int TYPE_INSTALL = 1;
    private DateSelectDialog dialog;
    private int type = TYPE_SEND;

    @BindView(R.id.house_hold_send_time)
    TextView sendTimeView;
    @BindView(R.id.house_hold_install_time)
    TextView installTimeView;

    private String selectSendTime;
    private String selectInstallTime;

    private int selectYear;
    private int selectMonth;
    private int selectDay;

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_hold_layout);

        selectSendTime = getIntent().getStringExtra("sendTime");
        selectInstallTime = getIntent().getStringExtra("installTime");
        if (selectSendTime != null){
            sendTimeView.setText(selectSendTime);
        }
        if (selectInstallTime != null){
            installTimeView.setText(selectInstallTime);
        }

        baseLayout.setTitle("大家电预约配送");

        dialog = new DateSelectDialog(this);
        dialog.showDay();
        dialog.setLimitSelect(false);
        dialog.setOnDateSelectListener(new DateSelectDialog.OnDateSelectListener() {
            @Override
            public void onSelectDate(String year, String month, String day) {

                selectYear = Integer.parseInt(year);
                selectMonth = Integer.parseInt(month);
                selectDay = Integer.parseInt(day);
                String week = getWeekDay(year + "-" + month + "-" + day);

                StringBuilder formatTime = new StringBuilder();
                formatTime.append(year).append("年")
                        .append(month).append("月")
                        .append(day).append("日")
                        .append(" [").append(week).append("]");
                if (type == TYPE_SEND) {
                    selectSendTime = formatTime.toString();
                    sendTimeView.setText(formatTime);
                } else if (type == TYPE_INSTALL) {
                    selectInstallTime = formatTime.toString();
                    installTimeView.setText(formatTime);
                }
            }
        });

        dialog.setOriginalDate();
    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmHouseHoldActivity.class);
        context.startActivity(intent);
    }

    public static void startItForResult(Context context, int requestCode, String sendTime, String installTime) {
        Intent intent = new Intent(context, ConfirmHouseHoldActivity.class);
        if (StringUtils.isNotNull(sendTime)) {
            intent.putExtra("sendTime", sendTime);
        }

        if (StringUtils.isNotNull(installTime)) {
            intent.putExtra("installTime", installTime);
        }
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @OnClick({R.id.house_hold_install_layout, R.id.house_hold_send_layout, R.id.house_hold_confirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.house_hold_send_layout:
                type = TYPE_SEND;
                dialog.show();
                break;
            case R.id.house_hold_install_layout:
                type = TYPE_INSTALL;
                dialog.show();
                break;
            case R.id.house_hold_confirm:
                Intent intent = new Intent();
                intent.putExtra("time_send", selectSendTime);
                intent.putExtra("time_install", selectInstallTime);
                setResult(100, intent);
                finish();
                break;
            default:
                break;
        }
    }

    private String getWeekDay(String formatTime) {
        int index;

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(formatTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        index = c.get(Calendar.DAY_OF_WEEK);
        return weeks[index - 1];
    }

}
