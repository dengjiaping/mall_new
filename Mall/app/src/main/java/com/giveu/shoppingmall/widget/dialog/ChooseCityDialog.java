package com.giveu.shoppingmall.widget.dialog;

/**
 * Created by 513419 on 2017/8/9.
 */


import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.WheelCityAdapter;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.wheelview.OnWheelChangedListener;
import com.giveu.shoppingmall.widget.wheelview.WheelView;

import java.util.List;


/**
 * 选择省市区的dialog
 */
public class ChooseCityDialog extends CustomDialog {
    public WheelView wl_province, wl_city, wl_county;
    WheelCityAdapter cityAdapter1, cityAdapter2, cityAdapter3;
    private boolean hideCounty = false;
    private String curP, curC, curA;//当前选中的省，市，区
    private TextView tv_title;


    public ChooseCityDialog(Activity context) {
        super(context, R.layout.dialog_choose_city, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tv_title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView tv_ok = (TextView) contentView.findViewById(R.id.tv_right);
        TextView tv_cancel_time = (TextView) contentView.findViewById(R.id.tv_left);
        initWheelView(contentView);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if (listener != null) {
                    listener.onConfirm(curP, curC, curA);
                }
            }
        });
        tv_cancel_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


    protected void initWheelView(View view) {
        wl_province = (WheelView) view.findViewById(R.id.wl1);
        wl_city = (WheelView) view.findViewById(R.id.wl2);
        wl_county = (WheelView) view.findViewById(R.id.wl3);
        wl_county.setVisibility(View.VISIBLE);

        cityAdapter1 = new WheelCityAdapter(mAttachActivity);
        cityAdapter1.setTextSize(15);
        wl_province.setViewAdapter(cityAdapter1);

        cityAdapter2 = new WheelCityAdapter(mAttachActivity);
        cityAdapter2.setTextSize(15);
        wl_city.setViewAdapter(cityAdapter2);

        cityAdapter3 = new WheelCityAdapter(mAttachActivity);
        cityAdapter3.setTextSize(15);
        wl_county.setViewAdapter(cityAdapter3);
        if (hideCounty) {
            wl_county.setVisibility(View.GONE);
        }

        List<String> province = getProvinceBean();
        if (CommonUtils.isNotNullOrEmpty(province)) {
            curP = province.get(0);
        }

        cityAdapter1.setData(province);

        List<String> cityByProvince = getCityByProvince(curP);
        if (CommonUtils.isNotNullOrEmpty(cityByProvince)) {
            curC = cityByProvince.get(0);
        }
        cityAdapter2.setData(cityByProvince);

        List<String> countyByProvinceCity = getCountyByProvinceCity(curP, curC);
        if (CommonUtils.isNotNullOrEmpty(countyByProvinceCity)) {
            curA = countyByProvinceCity.get(0);
        }
        cityAdapter3.setData(countyByProvinceCity);

        wl_province.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String p = cityAdapter1.getData().get(newValue);
                curP = p;
                cityAdapter2.setData(getCityByProvince(p));
                if (CommonUtils.isNotNullOrEmpty(cityAdapter2.getData())) {
                    wl_city.setCurrentItem(0);
                    updateAreaWheel(p, cityAdapter2.getData().get(0));
                }
            }
        });
        wl_city.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (newValue < cityAdapter2.getData().size()) {
                    String c = cityAdapter2.getData().get(newValue);
                    updateAreaWheel(curP, c);
                }
            }
        });
        wl_county.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                if (newValue < cityAdapter3.getData().size()) {
                    curA = cityAdapter3.getData().get(newValue);
                }
            }
        });
    }

    /**
     * 更新区的数据
     */
    private void updateAreaWheel(String curP, String curC) {
        this.curP = curP;
        this.curC = curC;

        cityAdapter3.setData(getCountyByProvinceCity(curP, curC));
        if (CommonUtils.isNotNullOrEmpty(cityAdapter3.getData())) {
            wl_county.setCurrentItem(0);
            this.curA = cityAdapter3.getData().get(0);
        }
    }

    public void setTitle(String title) {
        if (StringUtils.isNotNull(title)) {
            tv_title.setText(title);
        }
    }

    public List<String> getProvinceBean() {
        return null;
    }

    public List<String> getCityByProvince(String provinceName) {
        return null;
    }

    public List<String> getCountyByProvinceCity(String provinceName, String cityName) {
        return null;
    }

    private OnConfirmListener listener;

    public interface OnConfirmListener {
        void onConfirm(String p, String c, String a);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }
}