package com.giveu.shoppingmall.cash.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.mynet.BaseBean;
import com.android.volley.mynet.BaseRequestAgent;
import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.cash.adpter.AddressManageAdapter;
import com.giveu.shoppingmall.model.ApiImpl;
import com.giveu.shoppingmall.model.bean.response.AddressListResponse;
import com.giveu.shoppingmall.utils.Const;
import com.giveu.shoppingmall.utils.LoginHelper;
import com.giveu.shoppingmall.widget.ClickEnabledTextView;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 513419 on 2017/6/26.
 */

public class AddressManageActivity extends BaseActivity {
    @BindView(R.id.ptrlv)
    PullToRefreshListView ptrlv;
    @BindView(R.id.tv_add_address)
    ClickEnabledTextView tvAddAddress;
    private AddressManageAdapter addressManageAdapter;
    List<AddressListResponse> addressList;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, AddressManageActivity.class);
        activity.startActivity(intent);
    }

    public static void startItForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, AddressManageActivity.class);
        intent.putExtra("canClick", true);
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_address_manage);
        baseLayout.setTitle("选择收货地址");
        addressList = new ArrayList<>();
        addressManageAdapter = new AddressManageAdapter(mBaseContext, addressList);
        ptrlv.setAdapter(addressManageAdapter);
        ptrlv.setMode(PullToRefreshBase.Mode.BOTH);
        ptrlv.setPullLoadEnable(false);
        tvAddAddress.setClickEnabled(true);
    }

    @Override
    public void setData() {
        ApiImpl.getAddressList(mBaseContext, LoginHelper.getInstance().getIdPerson(), "5", new BaseRequestAgent.ResponseListener<AddressListResponse>() {
            @Override
            public void onSuccess(AddressListResponse response) {
                if (response != null && response.data != null) {
                    addressList.clear();
                    addressList.addAll(response.data);
                    addressManageAdapter.notifyDataSetChanged();
                    ptrlv.setPullRefreshEnable(true);
                    ptrlv.onRefreshComplete();
                }
            }

            @Override
            public void onError(BaseBean errorBean) {
                CommonLoadingView.showErrorToast(errorBean);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Const.ADDRESSMANAGE == requestCode && RESULT_OK == resultCode) {
            //添加成功或修改成功回来刷新页面
            setData();
        }
    }

    @Override
    public void setListener() {
        super.setListener();
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullLoadEnable(false);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                setData();
                ptrlv.setPullRefreshEnable(false);
            }
        });
        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddressActivity.startItForResult(mBaseContext, Const.ADDRESSMANAGE);
            }
        });
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = getIntent();
                boolean canClick = intent.getBooleanExtra("canClick", false);
                if (canClick) {
                    //订单确认页过来，需要点击回调返回数据
                    if (addressManageAdapter != null && position > 0) {
                        Intent data = new Intent();
                        data.putExtra("address", addressManageAdapter.getItem(position - 1));
                        setResult(RESULT_OK, data);
                        finish();
                    }
                }
            }
        });
    }

}
