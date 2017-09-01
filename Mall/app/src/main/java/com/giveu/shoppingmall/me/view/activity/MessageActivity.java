package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.MessageAdapter;
import com.giveu.shoppingmall.model.bean.response.BulletinListResponse;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 101900 on 2017/8/15.
 */

public class MessageActivity extends BaseActivity {
    @BindView(R.id.lv_message)
    PullToRefreshListView lvMessage;
    private MessageAdapter messageAdapter;
    private ArrayList<BulletinListResponse> bulletinList;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, MessageActivity.class);
        activity.startActivity(intent);
    }
    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
        baseLayout.setTitle("消息中心");
        bulletinList = new ArrayList<>();
        BulletinListResponse response = new BulletinListResponse();
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        bulletinList.add(response);
        messageAdapter = new MessageAdapter(this, bulletinList);
        lvMessage.setAdapter(messageAdapter);
        lvMessage.setMode(PullToRefreshBase.Mode.BOTH);
        lvMessage.setPullLoadEnable(false);
    }

    @Override
    public void setData() {

    }

}
