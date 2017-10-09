package com.giveu.shoppingmall.widget.dialog;

/**
 * Created by 513419 on 2017/8/9.
 */


import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.AddressPagerAdapter;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.model.bean.response.Province;
import com.giveu.shoppingmall.utils.AddressUtils;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择省市区的dialog
 */
public class ChooseCityDialog extends CustomDialog {
    private ViewPager vpAddress;
    private TabLayout stCity;
    private AddressPagerAdapter pagerAdapter;
    private ImageView ivClose;
    private String originalProvince;
    private String originalCity;
    private String originalRegion;
    private String originalStreet;
    private boolean needStreet;
    private ArrayList<Province> mAddressList;

    public ChooseCityDialog(Activity context) {
        super(context, R.layout.dialog_choose_city, R.style.customerDialog, Gravity.BOTTOM, true);
    }

    @Override
    protected void initView(View contentView) {
        vpAddress = (ViewPager) contentView.findViewById(R.id.vp_address);
        stCity = (TabLayout) contentView.findViewById(R.id.tb_tag);
        ivClose = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        pagerAdapter = new AddressPagerAdapter(mAttachActivity, vpAddress);
        vpAddress.setAdapter(pagerAdapter);
        vpAddress.setOffscreenPageLimit(3);
        stCity.setupWithViewPager(vpAddress);
        //pagerAdapter的回调
        pagerAdapter.setOnAddressChooseListener(new AddressPagerAdapter.OnAddressChooseListener() {
            @Override
            public void onChoose(String province, String city, String region, String street) {
                if (listener != null) {
                    //dialog的回调
                    listener.onConfirm(province, city, region, street);
                }
                //延迟消失时为了显示选择完成时可以看到选择了哪项
                vpAddress.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 100);
            }
        });
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        AddressUtils.getAddressList(mAttachActivity, new AddressUtils.OnAddressLitener() {
            @Override
            public void onSuccess(ArrayList<Province> addressList) {
                mAddressList = addressList;
                setData(addressList);
            }

            @Override
            public void onFail() {

            }
        });
    }

    protected void createDialog() {
        super.createDialog();
        Window dialogWindow = getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            // 设置对话框宽高
            lp.width = DensityUtils.getWidth();
            lp.height = (int) (DensityUtils.getHeight() * (0.56));
            dialogWindow.setAttributes(lp);
            dialogWindow.setGravity(Gravity.BOTTOM);
            dialogWindow.setWindowAnimations(R.style.dialogWindowAnim); // 添加动画
        }
    }


    /**
     * 初始化数据
     *
     * @param provinces
     */
    public void setData(ArrayList<Province> provinces) {
        this.mAddressList = provinces;
        pagerAdapter.setData(provinces);
        findProvice(provinces);
    }

    /**
     * 根据GPS返回的省份查找服务器返回的省份
     *
     * @param provinces
     */
    private void findProvice(ArrayList<Province> provinces) {
        if (StringUtils.isNull(originalProvince) || originalProvince.length() < 2) {
            return;
        }
        if (CommonUtils.isNotNullOrEmpty(provinces)) {
            for (Province province : provinces) {
                if (province.name.startsWith(originalProvince.substring(0, 2))) {
                    originalProvince = province.name;
                    findCity(province.array);
                    break;
                }
            }
        }

    }

    /**
     * 根据GPS返回的城市查找服务器返回的城市
     *
     * @param cities
     */
    private void findCity(List<Province.ArrayCity> cities) {
        if (StringUtils.isNull(originalCity) || originalCity.length() < 2) {
            return;
        }
        if (CommonUtils.isNotNullOrEmpty(cities)) {
            for (Province.ArrayCity arrayCity : cities) {
                if (arrayCity.name.startsWith(originalCity.substring(0, 2))) {
                    originalCity = arrayCity.name;
                    findRegion(arrayCity.array);
                    break;
                }
            }

        }
    }

    /**
     * 根据GPS返回的区查找服务器返回的区
     *
     * @param regions
     */
    private void findRegion(List<Province.ArrayRegion> regions) {
        if (StringUtils.isNull(originalRegion) || originalRegion.length() < 2) {
            return;
        }
        if (CommonUtils.isNotNullOrEmpty(regions)) {
            for (Province.ArrayRegion arrayRegion : regions) {
                if (arrayRegion.name.startsWith(originalRegion.substring(0, 2))) {
                    originalRegion = arrayRegion.name;
                    findStreets(arrayRegion.array);
                    break;
                }
            }
        }

    }

    /**
     * 根据GPS返回的街道查找服务器返回的街道
     *
     * @param streets
     */
    private void findStreets(List<String> streets) {
        if (CommonUtils.isNotNullOrEmpty(streets) && needStreet) {
            if (StringUtils.isNull(originalStreet) || originalStreet.length() < 2) {
                return;
            }
            for (String s : streets) {
                if (s.startsWith(originalStreet.substring(0, 2))) {
                    originalStreet = s;
                    if (listener != null) {
                        //dialog的回调
                        listener.onConfirm(originalProvince, originalCity, originalRegion, originalStreet);
                    }
                    break;
                }
            }
        } else {
            if (listener != null) {
                //dialog的回调
                listener.onConfirm(originalProvince, originalCity, originalRegion, originalStreet);
            }
        }
    }


    public void setOriginalAddress(String originalProvince, String originalCity, String originalRegion, String originalStreet) {
        this.originalProvince = originalProvince;
        this.originalCity = originalCity;
        this.originalRegion = originalRegion;
        this.originalStreet = originalStreet;
        if (CommonUtils.isNotNullOrEmpty(mAddressList)) {
            findProvice(mAddressList);
        }
    }


    private OnConfirmListener listener;

    public void setNeedStreet(boolean needStreet) {
        this.needStreet = needStreet;
        pagerAdapter.setNeedSteet(needStreet);
    }

    public interface OnConfirmListener {
        void onConfirm(String province, String city, String region, String street);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

}