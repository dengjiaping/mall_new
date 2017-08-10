package com.giveu.shoppingmall.widget.dialog;

/**
 * Created by 513419 on 2017/8/9.
 */


import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.rvadapter.RvCommonAdapter;
import com.giveu.shoppingmall.base.rvadapter.ViewHolder;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 选择省市区的dialog
 */
public class ChooseCityDialog extends CustomDialog {
    private String curP, curC, curA;//当前选中的省，市，区

    private ViewPager vpAddress;
    private ArrayList<String> tabList;
    private AddressAdapter addressAdapter;
    private TabLayout tbTag;

    public ChooseCityDialog(Activity context) {
        super(context, R.layout.dialog_choose_city, R.style.customerDialog, Gravity.CENTER, false);
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        tabList = new ArrayList<>();
        vpAddress = (ViewPager) contentView.findViewById(R.id.vp_address);
        tbTag = (TabLayout) contentView.findViewById(R.id.tb_tag);
        tabList.add("1");
        final TabLayout.Tab tab = tbTag.newTab().setText("请选择");
        tbTag.addTab(tab);
        tbTag.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() <= vpAddress.getChildCount()) {
                    vpAddress.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vpAddress.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position <= tbTag.getTabCount() && tbTag.getTabAt(position) != null) {
                    tbTag.getTabAt(position).select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        addressAdapter = new AddressAdapter(mAttachActivity, tabList);
        addressAdapter.setOnAddressChooseListener(new OnAddressChooseListener() {
            @Override
            public void onChoose(String address) {
                int currentItem = vpAddress.getCurrentItem();
                //选择的是第一个tab的地址，清空除第一个tab外的数据，并再增加一个数据
                if (currentItem == 0 && addressAdapter.getCount() > 1) {
                    tbTag.removeAllTabs();
                    final TabLayout.Tab tab = tbTag.newTab().setText(address);
                    tbTag.addTab(tab);
                    final TabLayout.Tab tab1 = tbTag.newTab().setText("请选择");
                    tbTag.addTab(tab1);
                    if(tabList.size()>1) {
                        tabList.subList(1, tabList.size()).clear();
                    }
                    tabList.add("2");
                    addressAdapter.notifyDataSetChanged();
                    vpAddress.setCurrentItem(currentItem + 1);
                    LogUtil.e("tbTagSize = " + tbTag.getTabCount());
                    LogUtil.e("tabListSzie = " + tabList.size());
                } else {
                    LogUtil.e("tabListSzie = onChoose");
                    if (tbTag.getTabAt(currentItem) != null) {
                        tbTag.getTabAt(currentItem).setText(address);
                        if (currentItem + 1 == addressAdapter.getCount() && currentItem < 3) {
                            final TabLayout.Tab tab = tbTag.newTab().setText("请选择");
                            tbTag.addTab(tab);
                            tabList.add("2");
                            addressAdapter.notifyDataSetChanged();
                            vpAddress.setCurrentItem(currentItem + 1);
                        } else if (currentItem < addressAdapter.getCount()) {
                            vpAddress.setCurrentItem(currentItem + 1);
                        }
                    }
                }
            }
        });
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
        void onConfirm(String p, String c, String a);
    }

    public void setOnConfirmListener(OnConfirmListener listener) {
        this.listener = listener;
    }

    private class AddressAdapter extends PagerAdapter {

        private Activity activity;
        private ArrayList<String> addressList;

        public AddressAdapter(Activity activity, ArrayList<String> addressList) {
            this.activity = activity;
            this.addressList = addressList;
        }

        @Override
        public int getCount() {
            return addressList.size();
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
            ArrayList<String> detailList = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                detailList.add("城市" + i);
            }
            RvCommonAdapter<String> addressAdapter = new RvCommonAdapter<String>(activity, R.layout.rv_item_address, detailList) {
                @Override
                protected void convert(ViewHolder holder, final String s, int position) {
                    holder.setText(R.id.tv_address, s);
                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (listener != null) {
                                listener.onChoose(s);
                            }
                        }
                    });
                }
            };
            LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvAddress.setLayoutManager(layoutManager);
            rvAddress.setAdapter(addressAdapter);
            container.addView(view);
            return view;
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