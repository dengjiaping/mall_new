package com.giveu.shoppingmall.widget.dialog;

/**
 * Created by 513419 on 2017/8/9.
 */


import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.rvadapter.RvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.CityBean;
import com.giveu.shoppingmall.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择省市区的dialog
 */
public class ChooseCityDialog extends CustomDialog {
    private String curP, curC, curR, curS;//当前选中的省，市，区

    private ViewPager vpAddress;
    private ArrayList<String> tabList;
    private AddressAdapter addressAdapter;
    private TabLayout tabLayout;

    private ArrayList<ArrayList<CityBean>> cityList;

    public ChooseCityDialog(Activity context) {
        super(context, R.layout.dialog_choose_city, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tabList = new ArrayList<>();
        cityList = new ArrayList<>();
        ArrayList<CityBean> itemList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            CityBean cityBean = new CityBean();
            cityBean.cityName = "城市" + i;
            itemList.add(cityBean);
        }
        cityList.add(itemList);
        vpAddress = (ViewPager) contentView.findViewById(R.id.vp_address);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tb_tag);
        ImageView ivClose = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tabList.add("请选择");
        addressAdapter = new AddressAdapter(mAttachActivity, tabList, cityList);
        addressAdapter.setOnAddressChooseListener(new OnAddressChooseListener() {
            @Override
            public void onChoose(String address) {
                int currentItem = vpAddress.getCurrentItem();
                //选择的是第一个tab的地址，清空除第一个tab外的数据，并再增加一个数据
                if (currentItem == 0 && tabList.size() >= 1) {
                    curP = address;
                    //重复选择不作处理
                    if (!tabList.get(currentItem).equals(address)) {
                        tabList.remove(currentItem);
                        tabList.add(currentItem, address);
                        //清空之后的信息
                        tabList.subList(1, tabList.size()).clear();
                        cityList.subList(1, cityList.size()).clear();
                        tabList.add("请选择");
                        ArrayList<CityBean> itemList = new ArrayList<>();
                        for (int i = 0; i < 20; i++) {
                            CityBean cityBean = new CityBean();
                            cityBean.cityName = "城市" + i;
                            itemList.add(cityBean);
                        }
                        cityList.add(itemList);
                        addressAdapter.notifyDataSetChanged();
                    }
                    vpAddress.setCurrentItem(currentItem + 1);
                } else {
                    //设置市区街道信息
                    switch (currentItem) {
                        case 1:
                            curC = address;
                            break;
                        case 2:
                            curR = address;
                            break;
                        case 3:
                            curS = address;
                            break;
                    }
                    //如果是viewpager最后一个并且当前viewpager页面小于3，那么需要新增区或街道列表
                    if (currentItem + 1 == tabList.size() && currentItem < 3) {
                        //重复选择不作处理
                        if (!tabList.get(currentItem).equals(address)) {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            tabList.add("请选择");
                            ArrayList<CityBean> itemList = new ArrayList<>();
                            for (int i = 0; i < 20; i++) {
                                CityBean cityBean = new CityBean();
                                cityBean.cityName = "城市" + i;
                                itemList.add(cityBean);
                            }
                            cityList.add(itemList);
                            addressAdapter.notifyDataSetChanged();
                        }
                        vpAddress.setCurrentItem(currentItem + 1);
                    } else if (currentItem < tabList.size()) {
                        //重复选择不作处理
                        if (!tabList.get(currentItem).equals(address) && currentItem < 3) {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            tabList.subList(currentItem + 1, tabList.size()).clear();
                            cityList.subList(currentItem + 1, cityList.size()).clear();
                            tabList.add("请选择");
                            ArrayList<CityBean> itemList = new ArrayList<>();
                            for (int i = 0; i < 20; i++) {
                                CityBean cityBean = new CityBean();
                                cityBean.cityName = "城市" + i;
                                itemList.add(cityBean);
                            }
                            cityList.add(itemList);
                        } else {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            if (tabList.size() == 3) {
                                curS = "";
                            }
                            if (listener != null) {
                                listener.onConfirm(curP, curC, curR, curS);
                            }

                        }
                        addressAdapter.notifyDataSetChanged();
                        vpAddress.setCurrentItem(currentItem + 1);
                    }
                }
            }
        });
        tabLayout.setupWithViewPager(vpAddress);
        vpAddress.setAdapter(addressAdapter);
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = (int) (DensityUtils.getHeight() * (0.6));
            attributes.width = DensityUtils.getWidth();
            attributes.gravity = Gravity.BOTTOM;
            window.setAttributes(attributes);
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        }
    }

    public List<String> getProvince() {
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
        void onConfirm(String province, String city, String region, String street);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    private class AddressAdapter extends PagerAdapter {

        private Activity activity;
        private ArrayList<String> tabList;
        private ArrayList<ArrayList<CityBean>> cityList;

        public AddressAdapter(Activity activity, ArrayList<String> tabList, ArrayList<ArrayList<CityBean>> cityList) {
            this.activity = activity;
            this.tabList = tabList;
            this.cityList = cityList;
        }

        @Override
        public int getCount() {
            return tabList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            View view = View.inflate(activity, R.layout.vp_item_address, null);
            RecyclerView rvAddress = (RecyclerView) view.findViewById(R.id.rv_address);
            if (position < cityList.size()) {
                final ArrayList<CityBean> detailList = cityList.get(position);
                RvCommonAdapter<CityBean> addressAdapter = new RvCommonAdapter<CityBean>(activity, R.layout.rv_item_address, detailList) {
                    @Override
                    protected void convert(final ViewHolder holder, final CityBean item, final int position) {
                        holder.setText(R.id.tv_address, item.cityName);
                        if (item.isChoose) {
                            holder.setTextColor(R.id.tv_address, ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
                        } else {
                            holder.setTextColor(R.id.tv_address, ContextCompat.getColor(mAttachActivity, R.color.color_282828));
                        }
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    listener.onChoose(item.cityName);
                                }
                                item.isChoose = true;
                                for (int i = 0; i < detailList.size(); i++) {
                                    CityBean cityBean = detailList.get(i);
                                    if (cityBean.isChoose && !cityBean.cityName.equals(item.cityName)) {
                                        cityBean.isChoose = false;
                                        break;
                                    }
                                }
                                notifyDataSetChanged();
                            }
                        });
                    }
                };
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rvAddress.setLayoutManager(layoutManager);
                rvAddress.setAdapter(addressAdapter);
            }

            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabList.get(position);
        }

        private OnAddressChooseListener listener;

        public void setOnAddressChooseListener(OnAddressChooseListener listener) {
            this.listener = listener;
        }
    }

    public interface OnAddressChooseListener {
        void onChoose(String address);
    }
}