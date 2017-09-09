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
import com.giveu.shoppingmall.utils.AddressUtils;
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
    private ArrayList<AddressBean> proviceList;//这个是服务器返回的数据，包含省市区街道list

    private ArrayList<ArrayList<CityBean>> cityList;//这个是选择省后，<>存储的市区街道列表，cityList的size和tabList的size一致
    private int currentItem;
    private boolean needStreet = true;

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
        //获取省份并存储省列表至cityList，与此同时tabList添加一个，因为viewpager的size与tabList的一致
        ArrayList<CityBean> itemList = new ArrayList<>();
        for (AddressBean addressBean : proviceList) {
            CityBean cityBean = new CityBean();
            cityBean.cityName = addressBean.name;
            itemList.add(cityBean);
        }
        //初始化省
        cityList.add(itemList);
        tabList.add("请选择");
        //刷新viewpager
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
        //选择省或市或区或街道后的回调即点击recyclerview的item的回调
        addressAdapter.setOnAddressChooseListener(new OnAddressChooseListener() {
            @Override
            public void onChoose(int position, String address) {
                //记录选中的是当前第几个页面
                currentItem = vpAddress.getCurrentItem();
                //选中的是一个页面
                if (currentItem == 0 && tabList.size() >= 1) {
                    //记录当前选中的省份，清空市区街道信息
                    curP = address;
                    curC = "";
                    curR = "";
                    curS = "";
                    pPosition = position;
                    //重复选择不作处理，只处理选中item与上次不同的情况
                    if (!tabList.get(currentItem).equals(address)) {
                        //这个操作实际是替换，比如上次选中的是北京，这次是上海，那么北京替换为上海
                        tabList.remove(currentItem);
                        tabList.add(currentItem, address);
                        //选中的是第一个页面，那么需要清空除第一个页面的其他页面
                        tabList.subList(1, tabList.size()).clear();
                        cityList.subList(1, cityList.size()).clear();
                        //清空后，因为已经选择了省份，接下来需要选择市，所以tabList需要增加一个
                        tabList.add("请选择");
                        //获取该省份对应下的市
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
                    //选中的不是第一个页面，设置市区街道信息
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
                            if (needStreet && CommonUtils.isNotNullOrEmpty(proviceList.get(pPosition).array.get(cPosition).array.get(rPosition).array)) {
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
                    //如果是viewpager最后一个并且itemList不为空（即该市或该区下还有子列表）
                    if (currentItem + 1 == tabList.size() && CommonUtils.isNotNullOrEmpty(itemList)) {
                        //重复选择不作处理，只处理选中item与上次不同的情况
                        if (!tabList.get(currentItem).equals(address)) {
                            //这个操作实际是替换，比如上次选中的是北京，这次是上海，那么北京替换为上海
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            //因为已经选择了item，并且该item下还有子列表，所以tabList需要增加一个
                            tabList.add("请选择");
                            cityList.add(itemList);
                            addressAdapter.notifyDataSetChanged();
                        }
                        //跳转至下一个选择
                        vpAddress.setCurrentItem(currentItem + 1);
                    } else if (currentItem < tabList.size()) {//防止越界
                        //重复选择不作处理，只处理选中item与上次不同的情况，并且该item下还有子列表
                        if (!tabList.get(currentItem).equals(address) && CommonUtils.isNotNullOrEmpty(itemList)) {
                            //这个操作实际是替换，比如上次选中的是北京，这次是上海，那么北京替换为上海
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            //选中的是第currentItem个页面，那么需要清空currentItem个页面后的其他页面
                            tabList.subList(currentItem + 1, tabList.size()).clear();
                            cityList.subList(currentItem + 1, cityList.size()).clear();
                            //因为已经选择了item，并且该item下还有子列表，所以tabList需要增加一个
                            tabList.add("请选择");
                            cityList.add(itemList);
                            addressAdapter.notifyDataSetChanged();
                        } else {
                            //这个操作实际是替换，比如上次选中的是北京，这次是上海，那么北京替换为上海
                            tabList.remove(currentItem);
                            tabList.add(currentItem, address);
                            //避免显示null，赋值""
                            curP = initString(curP);
                            curC = initString(curC);
                            curR = initString(curR);
                            curS = initString(curS);
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
                            //已经没有下级列表了，选择地址结束
                            if (listener != null && size == tabList.size()) {
                                listener.onConfirm(curP, curC, curR, curS);
                            }
                            addressAdapter.notifyDataSetChanged();
                        }
                        if (CommonUtils.isNotNullOrEmpty(itemList)) {
                            vpAddress.setCurrentItem(currentItem + 1);
                        }
                    }
                }
            }
        });
        tabLayout.setupWithViewPager(vpAddress);
        vpAddress.setAdapter(addressAdapter);
        vpAddress.setOffscreenPageLimit(3);
        AddressUtils.getAddressList(mAttachActivity, new AddressUtils.OnAddressLitener() {
            @Override
            public void onSuccess(ArrayList<AddressBean> addressList) {
                initProvince(addressList);
            }

            @Override
            public void onFail() {

            }
        });
    }

    /**
     * 是否显示街道信息
     * @param needStreet
     */
    public void setNeedStreet(boolean needStreet) {
        this.needStreet = needStreet;
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
            view.setTag(position);
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
                                //单选，所以需要重置非本次选择的isChoose
                                for (int i = 0; i < detailList.size(); i++) {
                                    CityBean cityBean = detailList.get(i);
                                    if (cityBean.isChoose && !cityBean.cityName.equals(item.cityName)) {
                                        cityBean.isChoose = false;
                                        break;
                                    }
                                }
                                //刷新ui
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
            View view = (View) object;
            if (view != null) {
                int tag = (int) view.getTag();
                if (tag <= currentItem) {
                    return POSITION_UNCHANGED;
                } else {
                    return POSITION_NONE;
                }
            } else {
                return POSITION_NONE;
            }
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