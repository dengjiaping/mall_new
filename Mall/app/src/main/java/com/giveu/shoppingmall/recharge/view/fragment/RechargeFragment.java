package com.giveu.shoppingmall.recharge.view.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.model.bean.response.RechargeResponse;
import com.giveu.shoppingmall.recharge.view.dialog.ChargeOrderDialog;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.giveu.shoppingmall.R.id.et_recharge;
import static com.giveu.shoppingmall.R.id.gv_recharge;

/**
 * 充值模块
 * Created by 508632 on 2016/12/13.
 */

public class RechargeFragment extends BaseFragment {

    LvCommonAdapter<RechargeResponse.ListBean.ProductsBean> rechargeAdapter;
    @BindView(R.id.rb_bill)
    RadioButton rbBill;
    @BindView(R.id.rb_flow)
    RadioButton rbFlow;
    @BindView(R.id.rg_recharge)
    RadioGroup rgRecharge;
    @BindView(et_recharge)
    EditText etRecharge;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(gv_recharge)
    NoScrollGridView gvRecharge;
    @BindView(R.id.iv_clear)
    ImageView ivClear;
    @BindView(R.id.iv_mail_list)
    ImageView ivMailList;
    private String phoneArea;
    //用户名
    private String username;
    //运营商
    private String oper;
    //产品id
    private String pid;

    // PersonInfoResponse.UserInfoBean userInfoBean = null;
    //  List<RechargeBean> list;
    private int tabIndex;//0=话费 1=流量
    private boolean isVailable = false;//是否可点击

    //RechargePreferntialResponse rechargePreferntialResponse = null;
    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(mBaseContext, R.layout.fragment_main_recharge, null);
        baseLayout.setTitle("手机充值");
        ButterKnife.bind(this, view);
        gvRecharge.setEnabled(false);
        return view;
    }

    @Override
    protected void setListener() {
        etRecharge.addTextChangedListener(textWatcher);
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清除输入的手机号
                etRecharge.setText("");
            }
        });
        ivMailList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转通讯录
                startActivityForResult(new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI), 0);
            }
        });

        gvRecharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Activity activity, String phoneArea, final String product, final String number, String price, String inputDenomination) {
            public void onItemClick(AdapterView<?> adapterView, View view, final int checkId, long l) {
                ChargeOrderDialog dialog = new ChargeOrderDialog(mBaseContext,"广东移动",rechargeAdapter.getItem(checkId).productName,etRecharge.getText().toString(),rechargeAdapter.getItem(checkId).salesPrice,rechargeAdapter.getItem(checkId).denomination);
                dialog.showDialog();
                //优惠券
//                final String productName = rechargeAdapter.getItem(checkId).productName;
//                final ChargeOrderDialog dialog = new ChargeOrderDialog();
//                ApiImpl.rechargePreferntial(mBaseContext, productName, "", new ResponseListener<RechargePreferntialResponse>() {
//                    @Override
//                    public void onSuccess(RechargePreferntialResponse response) {
//
//                        rechargePreferntialResponse = response;
//                        pid = rechargeAdapter.getItem(checkId).productId;
//                        dialog.showDialog(rechargePreferntialResponse, mBaseContext, tabIndex, username, phoneArea, oper, pid, productName, et_recharge.getText().toString(), "￥" + rechargeAdapter.getItem(checkId).salesPrice, rechargeAdapter.getItem(checkId).denomination);
//                    }
//
//                    @Override
//                    public void onError(BaseBean errorBean) {
//                        CommonLoadingView.showErrorToast(errorBean);
//                    }
//                });

            }
        });
    }

    @Override
    public void initWithDataDelay() {

        rechargeResponse = new RechargeResponse();
        RechargeResponse.ListBean.ProductsBean productsBean1 = new RechargeResponse.ListBean.ProductsBean("1", "30元", "30");
        RechargeResponse.ListBean.ProductsBean productsBean2 = new RechargeResponse.ListBean.ProductsBean("1", "50元", "49.98");
        RechargeResponse.ListBean.ProductsBean productsBean3 = new RechargeResponse.ListBean.ProductsBean("1", "100元", "99.98");
        List<RechargeResponse.ListBean.ProductsBean> list1 = new ArrayList<>();
        list1.add(productsBean1);
        list1.add(productsBean2);
        list1.add(productsBean3);
        RechargeResponse.ListBean.ProductsBean productsBean4 = new RechargeResponse.ListBean.ProductsBean("1", "30M", "30");
        RechargeResponse.ListBean.ProductsBean productsBean5 = new RechargeResponse.ListBean.ProductsBean("1", "50M", "49.98");
        RechargeResponse.ListBean.ProductsBean productsBean6 = new RechargeResponse.ListBean.ProductsBean("1", "100M", "99.98");
        List<RechargeResponse.ListBean.ProductsBean> list2 = new ArrayList<>();
        list2.add(productsBean4);
        list2.add(productsBean5);
        list2.add(productsBean6);
        rechargeResponse.calls = new RechargeResponse.ListBean("中国联通", "CUCC", list1);
        rechargeResponse.flows = new RechargeResponse.ListBean("中国联通", "CUCC", list2);
        rechargeAdapter = new LvCommonAdapter<RechargeResponse.ListBean.ProductsBean>(mBaseContext, R.layout.gv_recharge_item, list1) {
            @Override
            protected void convert(ViewHolder viewHolder, RechargeResponse.ListBean.ProductsBean item, int position) {
                TextView tv1 = viewHolder.getView(R.id.tv_recharge_t1);
                TextView tv2 = viewHolder.getView(R.id.tv_recharge_t2);
                tv1.setText(item.productName);
                tv2.setText("售价￥"+item.salesPrice);
                LinearLayout ll_recharge_item = viewHolder.getView(R.id.ll_recharge_item);
                switch (item.status) {
                    case 0:
                        tv1.setTextColor(getContext().getResources().getColor(R.color.color_edittext));
                        tv2.setTextColor(getContext().getResources().getColor(R.color.color_edittext));
                        ll_recharge_item.setBackgroundResource(R.drawable.shape_recharge_default);
                        break;
                    case 1:
                        tv1.setTextColor(getContext().getResources().getColor(R.color.title_color));
                        tv2.setTextColor(getContext().getResources().getColor(R.color.android_default_dialog_title));
                        ll_recharge_item.setBackgroundResource(R.drawable.shape_recharge_press);
                        break;
                    default:
                        break;
                }
            }
        };
        gvRecharge.setAdapter(rechargeAdapter);
//        rechargePreferntialResponse = new RechargePreferntialResponse();
//
//
//
//        if (PersonInfoResponse.getInstance() != null) {
//            if (PersonInfoResponse.getInstance().userInfo != null) {
//                userInfoBean = PersonInfoResponse.getInstance().userInfo;
//                username = userInfoBean.name;
//                StringBuffer defaultStr = new StringBuffer();
//                defaultStr.append(userInfoBean.mobile);
//                defaultStr.insert(3, " ");
//                defaultStr.insert(8, " ");
//                et_recharge.setText(defaultStr);
//                et_recharge.setSelection(et_recharge.getText().length());
//                getData(userInfoBean.mobile);
//            }
//        }

        rgRecharge.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.rb_bill:
                        tabIndex = 0;
                        break;
                    case R.id.rb_flow:
                        tabIndex = 1;
                        break;
                    default:
                        break;
                }

                setMyData(rechargeResponse);
            }
        });

    }

    @Override
    protected boolean translateStatusBar() {
        return true;
    }

    //对手机号的格式指定
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s == null) {
                return;
            }
            //判断是否是在中间输入，需要重新计算
            boolean isMiddle = (start + count) < (s.length());
            //在末尾输入时，是否需要加入空格
            boolean isNeedSpace = false;
            if (!isMiddle && isNeedSpace(s.length())) {
                isNeedSpace = true;
            }
            if (isMiddle || isNeedSpace) {
                String newStr = s.toString();
                etRecharge.removeTextChangedListener(textWatcher);
                StringBuilder sb = phoneNumberFormat(newStr);
                etRecharge.setText(sb);

                //如果是在末尾的话,或者加入的字符个数大于零的话（输入或者粘贴）
                if (!isMiddle || count > 1) {
                    etRecharge.setSelection(sb.length());
                } else if (isMiddle) {
                    //如果是删除
                    if (count == 0) {
                        //如果删除时，光标停留在空格的前面，光标则要往前移一位
                        if (isNeedSpace(start - before + 1)) {
                            etRecharge.setSelection((start - before) > 0 ? start - before : 0);
                        } else {
                            etRecharge.setSelection((start - before + 1) > sb.length() ? sb.length() : (start - before + 1));
                        }
                    }
                    //如果是增加
                    else {
                        if (isNeedSpace(start - before + count)) {
                            etRecharge.setSelection((start + count - before + 1) < sb.length() ? (start + count - before + 1) : sb.length());
                        } else {
                            etRecharge.setSelection(start + count - before);
                        }
                    }
                }
                etRecharge.addTextChangedListener(textWatcher);
            }

        }

        private boolean isNeedSpace(int length) {
            if (length < 4) {
                return false;
            } else if (length == 4) {
                return true;
            } else return (length + 1) % 5 == 0;
        }

        //1表示显示地区 0表示隐藏
        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 13) {
                String phone = editable.toString().replace(" ", "");
                CommonUtils.closeSoftKeyBoard(mBaseContext);

                isVailable = true;
                getData(phone);
            } else {
                isVailable = false;
                setMyData(rechargeResponse);
            }
        }
    };


    /**
     * 请求数据
     */

    RechargeResponse rechargeResponse = null;

    private void getData(String phone) {


        setMyData(rechargeResponse);
//        ApiImpl.rechargeRequest(null, phone, new ResponseListener<RechargeResponse>() {
//            @Override
//            public void onSuccess(RechargeResponse response) {
//                rechargeResponse = response.data;
//                setMyData(rechargeResponse);
//
//            }
//
//            @Override
//            public void onError(BaseBean errorBean) {
//                CommonLoadingView.showErrorToast(errorBean);
//            }
//        });

    }

    //设置显示数据
    private void setMyData(RechargeResponse data) {
        if (data != null) {
            // rechargeAdapter = new RechargeAdapter(mBaseContext);
            gvRecharge.setAdapter(rechargeAdapter);

            if (!isVailable) {
                for (RechargeResponse.ListBean.ProductsBean products : data.calls.products) {
                    products.status = 0;
                }
                for (RechargeResponse.ListBean.ProductsBean products : data.flows.products) {
                    products.status = 0;
                }

                gvRecharge.setEnabled(false);
            } else {//绿色
                for (RechargeResponse.ListBean.ProductsBean products : data.calls.products) {
                    products.status = 1;
                }
                for (RechargeResponse.ListBean.ProductsBean products : data.flows.products) {
                    products.status = 1;
                }
                gvRecharge.setEnabled(true);
            }

            switch (tabIndex) {
                case 0:
                    rechargeAdapter.setData(data.calls.products);
                    oper = data.calls.operatorType;

                    break;
                case 1:
                    rechargeAdapter.setData(data.flows.products);
                    oper = data.flows.operatorType;
                    break;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            // ContentProvider展示数据类似一个单个数据库表
            // ContentResolver实例带的方法可实现找到指定的ContentProvider并获取到ContentProvider的数据
            ContentResolver reContentResolverol = mBaseContext.getContentResolver();
            // URI,每个ContentProvider定义一个唯一的公开的URI,用于指定到它的数据集
            Uri contactData = data.getData();
            // 查询就是输入URI等参数,其中URI是必须的,其他是可选的,如果系统能找到URI对应的ContentProvider将返回一个Cursor对象.
            Cursor cursor = mBaseContext.managedQuery(contactData, null, null, null, null);
            cursor.moveToFirst();
            // 获得DATA表中的名字
            username = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            // 条件为联系人ID
            String contactId = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.Contacts._ID));
            // 获得DATA表中的电话号码，条件为联系人ID,因为手机号码可能会有多个
            Cursor phone = reContentResolverol.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
                            + contactId, null, null);
            while (phone.moveToNext()) {
                //填入号码
                String usernumber = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                StringBuilder sb = phoneNumberFormat(usernumber);
                etRecharge.setText(sb);
                etRecharge.setSelection(sb.length());
            }

            phone.close();

        }
    }

    /**
     * 手机号码格式化，空格隔开
     *
     * @return
     */
    public StringBuilder phoneNumberFormat(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < phoneNumber.length(); ) {
            if (i == 0) {
                sb.append(phoneNumber.length() > 3 ? phoneNumber.substring(0, 3) : phoneNumber);
                i += 3;
                continue;
            } else if (i > 0) {
                sb.append(" ");
                if (i + 4 <= phoneNumber.length()) {
                    sb.append(phoneNumber.substring(i, i + 4));
                } else {
                    sb.append(phoneNumber.substring(i, phoneNumber.length()));
                }
                i += 4;
            }
        }
        return sb;
    }
}
