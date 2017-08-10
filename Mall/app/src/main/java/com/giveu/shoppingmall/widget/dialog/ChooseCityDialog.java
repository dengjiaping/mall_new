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
import com.giveu.shoppingmall.model.bean.response.AddressBean;
import com.giveu.shoppingmall.model.bean.response.CityBean;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.util.ArrayList;


/**
 * 选择省市区的dialog
 */
public class ChooseCityDialog extends CustomDialog {
    private String curP, curC, curR, curS;//当前选中的省，市，区
    private int pPosition, cPosition, rPosition, sPosition;

    private ViewPager vpAddress;
    private ArrayList<String> tabList;
    private AddressAdapter addressAdapter;
    private TabLayout tabLayout;
    private ArrayList<AddressBean> proviceList;

    private ArrayList<ArrayList<CityBean>> cityList;

    public ChooseCityDialog(Activity context) {
        super(context, R.layout.dialog_choose_city, R.style.customerDialog, Gravity.CENTER, false);
    }

    /**
     * 初始化省列表
     *
     * @param proviceList
     */
    public void initProvince(ArrayList<AddressBean> proviceList) {
        if (CommonUtils.isNullOrEmpty(proviceList)) {
            return;
        }
        this.proviceList = proviceList;
        ArrayList<CityBean> itemList = new ArrayList<>();
        for (AddressBean addressBean : proviceList) {
            CityBean cityBean = new CityBean();
            cityBean.cityName = addressBean.name;
            itemList.add(cityBean);
        }
        //初始化省
        cityList.add(itemList);
        tabList.add("请选择");
        addressAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        vpAddress = (ViewPager) contentView.findViewById(R.id.vp_address);
        tabLayout = (TabLayout) contentView.findViewById(R.id.tb_tag);
        ImageView ivClose = (ImageView) contentView.findViewById(R.id.iv_dialog_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tabList = new ArrayList<>();
        cityList = new ArrayList<>();
        addressAdapter = new AddressAdapter(mAttachActivity, tabList, cityList);
        addressAdapter.setOnAddressChooseListener(new OnAddressChooseListener() {
            @Override
            public void onChoose(int position, String address) {
                int currentItem = vpAddress.getCurrentItem();
                //选择的是第一个tab的地址，清空除第一个tab外的数据，并再增加一个数据
                if (currentItem == 0 && tabList.size() >= 1) {
                    curP = address;
                    curC = "";
                    curR = "";
                    curS = "";
                    pPosition = position;
                    //重复选择不作处理
                    if (!tabList.get(currentItem).equals(address)) {
                        tabList.remove(currentItem);
                        tabList.add(currentItem, address);
                        //清空之后的信息
                        tabList.subList(1, tabList.size()).clear();
                        cityList.subList(1, cityList.size()).clear();
                        tabList.add("请选择");
                        ArrayList<CityBean> itemList = new ArrayList<>();
                        for (AddressBean.ArrayCity arrayCity : proviceList.get(pPosition).array) {
                            CityBean cityBean = new CityBean();
                            cityBean.cityName = arrayCity.name;
                            itemList.add(cityBean);
                        }
                        cityList.add(itemList);
                        addressAdapter.notifyDataSetChanged();
                    }
                    vpAddress.setCurrentItem(currentItem + 1);
                } else {
                    ArrayList<CityBean> itemList = new ArrayList<>();

                    //设置市区街道信息
                    switch (currentItem) {
                        case 1:
                            cPosition = position;
                            curC = address;
                            curR = "";
                            curS = "";
                            //根据市获取区列表
                            if (CommonUtils.isNotNullOrEmpty(proviceList.get(pPosition).array.get(cPosition).array)) {
                                for (AddressBean.ArrayCity.ArrayRegion arrayRegion : proviceList.get(pPosition).array.get(cPosition).array) {
                                    CityBean cityBean = new CityBean();
                                    cityBean.cityName = arrayRegion.name;
                                    itemList.add(cityBean);
                                }
                            }
                            break;
                        case 2:
                            //根据区获取街道列表
                            rPosition = position;
                            curR = address;
                            curS = "";
                            if (CommonUtils.isNotNullOrEmpty(proviceList.get(pPosition).array.get(cPosition).array.get(rPosition).array)) {
                                for (String streetStr : proviceList.get(pPosition).array.get(cPosition).array.get(rPosition).array) {
                                    CityBean cityBean = new CityBean();
                                    cityBean.cityName = streetStr;
                                    itemList.add(cityBean);
                                }
                            }
                            break;
                        case 3:
                            sPosition = position;
                            curS = address;
                            break;
                    }
                    //如果是viewpager最后一个并且itemList不为空
                    if (currentItem + 1 == tabList.size() && CommonUtils.isNotNullOrEmpty(itemList)) {
                        //重复选择不作处理
                        if (!tabList.get(currentItem).equals(address)) {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            tabList.add("请选择");
                            cityList.add(itemList);
                            addressAdapter.notifyDataSetChanged();
                        }
                        //跳转至下一个选择
                        vpAddress.setCurrentItem(currentItem + 1);
                    } else if (currentItem < tabList.size()) {
                        //重复选择不作处理
                        if (!tabList.get(currentItem).equals(address) && CommonUtils.isNotNullOrEmpty(itemList)) {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            tabList.subList(currentItem + 1, tabList.size()).clear();
                            cityList.subList(currentItem + 1, cityList.size()).clear();
                            tabList.add("请选择");
                            cityList.add(itemList);
                        } else {
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            //避免显示null，赋值“”
                            initString(curP);
                            initString(curC);
                            initString(curR);
                            initString(curS);
                            if (listener != null) {
                                //避免快速点击以致弹框消失
                                int size = 0;
                                if (StringUtils.isNotNull(curP)) {
                                    size++;
                                }
                                if (StringUtils.isNotNull(curC)) {
                                    size++;
                                }
                                if (StringUtils.isNotNull(curR)) {
                                    size++;
                                }
                                if (StringUtils.isNotNull(curS)) {
                                    size++;
                                }
                                if (size == tabList.size()) {
                                    listener.onConfirm(curP, curC, curR, curS);
                                }
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
        vpAddress.setOffscreenPageLimit(3);
    }

    private String initString(String str) {
        if (str == null) {
            str = "";
        }
        return str;
    }

    @Override
    protected void createDialog() {
        super.createDialog();
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams attributes = window.getAttributes();
            attributes.height = (int) (DensityUtils.getHeight() * (0.56));
            attributes.width = DensityUtils.getWidth();
            attributes.gravity = Gravity.BOTTOM;
            window.setAttributes(attributes);
            window.setWindowAnimations(R.style.dialogWindowAnim); //设置窗口弹出动画
        }
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
                LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                RvCommonAdapter<CityBean> addressAdapter = new RvCommonAdapter<CityBean>(activity, R.layout.rv_item_address, detailList) {
                    @Override
                    protected void convert(final ViewHolder holder, final CityBean item, final int position) {
                        holder.setText(R.id.tv_address, item.cityName);
                        if (item.isChoose) {
                            holder.setTextColor(R.id.tv_address, ContextCompat.getColor(mAttachActivity, R.color.color_00bbc0));
                            holder.setVisible(R.id.iv_choose, true);
                        } else {
                            holder.setTextColor(R.id.tv_address, ContextCompat.getColor(mAttachActivity, R.color.color_282828));
                            holder.setVisible(R.id.iv_choose, false);
                        }
                        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listener != null) {
                                    listener.onChoose(position, item.cityName);
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
        void onChoose(int pos, String address);
    }
}