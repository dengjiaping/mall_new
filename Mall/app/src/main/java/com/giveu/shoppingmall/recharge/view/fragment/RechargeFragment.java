package com.giveu.shoppingmall.recharge.view.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.mynet.BaseBean;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseFragment;
import com.giveu.shoppingmall.base.lvadapter.LvCommonAdapter;
import com.giveu.shoppingmall.base.lvadapter.ViewHolder;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.widget.NoScrollGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.giveu.shoppingmall.R.id.gv_recharge;

/**
 * 充值模块
 * Created by 508632 on 2016/12/13.
 */

public class RechargeFragment extends BaseFragment {

    LvCommonAdapter<BaseBean> rechargeAdapter;
    @BindView(R.id.rb_bill)
    RadioButton rbBill;
    @BindView(R.id.rb_flow)
    RadioButton rbFlow;
    @BindView(R.id.rg_recharge)
    RadioGroup rgRecharge;
    @BindView(R.id.et_recharge)
    EditText etRecharge;
    @BindView(R.id.iv_recharge)
    LinearLayout ivRecharge;
    @BindView(R.id.ll_recharge)
    LinearLayout llRecharge;
    @BindView(R.id.tv_strings)
    TextView tvStrings;
    @BindView(R.id.view_thread)
    View viewThread;
    @BindView(R.id.tv_text)
    TextView tvText;
    @BindView(gv_recharge)
    NoScrollGridView gvRecharge;
    Unbinder unbinder;
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
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    protected void setListener() {
        etRecharge.addTextChangedListener(textWatcher);
        ivRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etRecharge.setText("");
            }
        });
        gvRecharge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int checkId, long l) {

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
        BaseBean bean = new BaseBean();
        List<BaseBean> data = new ArrayList<>();
        data.add(bean);
        data.add(bean);
        data.add(bean);
        data.add(bean);
        rechargeAdapter = new LvCommonAdapter<BaseBean>(mBaseContext, R.layout.gv_recharge_item, data) {
            @Override
            protected void convert(ViewHolder viewHolder, BaseBean item, int position) {

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

                //     setMyData(rechargeResponse);
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
                newStr = newStr.replace(" ", "");
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < newStr.length(); ) {
                    if (i == 0) {
                        sb.append(newStr.length() > 3 ? newStr.substring(0, 3) : newStr);
                        i += 3;
                        continue;
                    } else if (i > 0) {
                        sb.append(" ");
                        if (i + 4 <= newStr.length()) {
                            sb.append(newStr.substring(i, i + 4));
                        } else {
                            sb.append(newStr.substring(i, newStr.length()));
                        }
                        i += 4;
                    }
                }
                etRecharge.removeTextChangedListener(textWatcher);
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
                //  getData(phone);
            } else {
                isVailable = false;
                //   setMyData(rechargeResponse);
            }
        }
    };


    /**
     * 请求数据
     */

    // RechargeResponse rechargeResponse = null;

//    private void getData(String phone) {
//
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
////        }
//
//    }

    //设置显示数据
//    private void setMyData(RechargeResponse data) {
//        if (data != null) {
//            rechargeAdapter = new RechargeAdapter(mBaseContext);
//            gv_recharge.setAdapter(rechargeAdapter);
//            phoneArea = rechargeResponse.phoneArea + rechargeResponse.calls.operatorName.substring(2, 4);
//            tv_phoneArea.setText(phoneArea);
//
//            if (!isVailable) {
//                for (RechargeResponse.ListBean.ProductsBean products : data.calls.products) {
//                    products.status = 0;
//                }
//                for (RechargeResponse.ListBean.ProductsBean products : data.flows.products) {
//                    products.status = 0;
//                }
//
//                tv_phoneArea.setVisibility(View.INVISIBLE);
//                gv_recharge.setEnabled(false);
//                tv_gridviewTip.setVisibility(View.GONE);
//            } else {//绿色
//                for (RechargeResponse.ListBean.ProductsBean products : data.calls.products) {
//                    products.status = 1;
//                }
//                for (RechargeResponse.ListBean.ProductsBean products : data.flows.products) {
//                    products.status = 1;
//                }
//                gv_recharge.setEnabled(true);
//                tv_phoneArea.setVisibility(View.VISIBLE);
//                tv_gridviewTip.setVisibility(View.VISIBLE);
//            }
//
//            switch (tabIndex) {
//                case 0:
//                    rechargeAdapter.setItemList(data.calls.products);
//                    oper = data.calls.operatorType;
//
//                    break;
//                case 1:
//                    rechargeAdapter.setItemList(data.flows.products);
//                    oper = data.flows.operatorType;
//                    break;
//            }
//
//        }
//    }
}
