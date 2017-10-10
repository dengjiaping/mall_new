package com.giveu.shoppingmall.index.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.index.view.dialog.ChooseDialog;
import com.giveu.shoppingmall.model.bean.response.CreateOrderResponse;
import com.giveu.shoppingmall.model.bean.response.Item;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.dialog.DateSelectDialog;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 524202 on 2017/9/5.
 * 大家电配送
 */

public class ConfirmHouseHoldActivity extends BaseActivity {
    private ChooseDialog<CreateOrderResponse.ReservingListBean> sendTimeDlg = null;
    private ChooseDialog<CreateOrderResponse.ReservingListBean> installTimeDlg = null;
    private long sendTimesId;
    private long installTimesId;

    @BindView(R.id.house_hold_send_time)
    TextView sendTimeView;
    @BindView(R.id.house_hold_install_time)
    TextView installTimeView;
    @BindView(R.id.house_hold_send_layout)
    RelativeLayout rlSendTimeLayout;
    @BindView(R.id.house_hold_install_layout)
    RelativeLayout rlInstallTimeLayout;

    private List<CreateOrderResponse.ReservingListBean> sendTimesList;
    private List<CreateOrderResponse.ReservingListBean> installTimesList;


    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_house_hold_layout);

        sendTimesList = (List<CreateOrderResponse.ReservingListBean>) getIntent().getSerializableExtra("sendTime");
        installTimesList = (List<CreateOrderResponse.ReservingListBean>) getIntent().getSerializableExtra("installTime");
        sendTimesId = getIntent().getLongExtra("sendTimesId", 0);
        installTimesId = getIntent().getLongExtra("installTimesId", 0);

        baseLayout.setTitle("大家电预约配送");


        if (sendTimesList != null) {

            sendTimeView.setText("请选择");
            for (CreateOrderResponse.ReservingListBean bean : sendTimesList) {
                if (sendTimesId == bean.id) {
                    sendTimeView.setText(bean.date + "[" + bean.week + "]");
                }
            }

            sendTimeDlg = new ChooseDialog<CreateOrderResponse.ReservingListBean>(this, sendTimesList) {
                @Override
                public void convertView(ViewHolder holder, final CreateOrderResponse.ReservingListBean item, int position, long checkIndex) {
                    holder.setText(R.id.dialog_choose_item_text, item.date + "[" + item.week + "]");
                    holder.setChecked(R.id.dialog_choose_item_text, item.id == checkIndex);
                    holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendTimesId = item.id;
                            sendTimeView.setText(item.date + "[" + item.week + "]");
                            sendTimeDlg.dismiss();
                        }
                    });
                }
            };
            rlSendTimeLayout.setVisibility(View.VISIBLE);
        }

        if (installTimesList != null) {
            installTimeView.setText("请选择");
            for (CreateOrderResponse.ReservingListBean bean : installTimesList) {
                if (installTimesId == bean.id) {
                    installTimeView.setText(bean.date + "[" + bean.week + "]");
                }
            }
            installTimeDlg = new ChooseDialog<CreateOrderResponse.ReservingListBean>(this, installTimesList) {
                @Override
                public void convertView(ViewHolder holder, final CreateOrderResponse.ReservingListBean item, int position, long checkIndex) {
                    holder.setText(R.id.dialog_choose_item_text, item.date + "[" + item.week + "]");
                    holder.setChecked(R.id.dialog_choose_item_text, item.id == checkIndex);
                    holder.setOnClickListener(R.id.dialog_choose_item_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            installTimesId = item.id;
                            installTimeView.setText(item.date + "[" + item.week + "]");
                            installTimeDlg.dismiss();
                        }
                    });
                }
            };
            rlInstallTimeLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void setData() {

    }

    public static void startIt(Context context) {
        Intent intent = new Intent(context, ConfirmHouseHoldActivity.class);
        context.startActivity(intent);
    }

    public static void startItForResult(Context context, int requestCode, List<CreateOrderResponse.ReservingListBean> sendTimesList, List<CreateOrderResponse.ReservingListBean> installTimesList, long sendTimesId, long installTimesId) {
        Intent intent = new Intent(context, ConfirmHouseHoldActivity.class);
        if (CommonUtils.isNotNullOrEmpty(sendTimesList)) {
            intent.putExtra("sendTime", (Serializable) sendTimesList);
        }
        if (CommonUtils.isNotNullOrEmpty(installTimesList)) {
            intent.putExtra("installTime", (Serializable) installTimesList);
        }

        intent.putExtra("sendTimesId", sendTimesId);
        intent.putExtra("installTimesId", installTimesId);

        ((Activity) context).startActivityForResult(intent, requestCode);
    }


    @OnClick({R.id.house_hold_install_layout, R.id.house_hold_send_layout, R.id.house_hold_confirm})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.house_hold_send_layout:
                sendTimeDlg.show(sendTimesId);
                break;
            case R.id.house_hold_install_layout:
                installTimeDlg.show(installTimesId);
                break;
            case R.id.house_hold_confirm:
                Intent intent = new Intent();
                intent.putExtra("sendTimesId", sendTimesId);
                intent.putExtra("installTimesId", installTimesId);
                setResult(100, intent);
                finish();
                break;
            default:
                break;
        }
    }

}
