package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.adapter.MessageAdapter;
import com.giveu.shoppingmall.me.view.fragment.MessageFragment;
import com.giveu.shoppingmall.model.bean.response.BulletinListResponse;
import com.giveu.shoppingmall.widget.NoScrollViewPager;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshBase;
import com.giveu.shoppingmall.widget.pulltorefresh.PullToRefreshListView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by 101900 on 2017/8/15.
 */

public class MessageActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    NoScrollViewPager viewPager;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


//    private MessageAdapter messageAdapter;
//    private ArrayList<BulletinListResponse> bulletinList;
    private String[] tabsTitle = {"通知", "私信"};
    private ArrayList<MessageFragment> fragmentList;
    private MessageFragmentAdapter fragmentAdapter;

    public static void startIt(Activity activity) {
        Intent intent = new Intent(activity, MessageActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
        baseLayout.setTitle("消息中心");
/*        bulletinList = new ArrayList<>();
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
        lvMessage.setPullLoadEnable(false);*/
    }

    @Override
    public void setData() {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            @Override
            public boolean queueIdle() {
                initFragment();
                tabLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        //通知
        MessageFragment notificationFragment = new MessageFragment();
        fragmentList.add(notificationFragment);

        //私信
        MessageFragment privateLetterFragment = new MessageFragment();
        fragmentList.add(privateLetterFragment);

        fragmentAdapter = new MessageFragmentAdapter(getSupportFragmentManager(), fragmentList, tabsTitle);

        viewPager.setAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }


    private class MessageFragmentAdapter extends FragmentPagerAdapter {

        ArrayList<MessageFragment> fragmentList;
        String[] tabsTitle;

        public MessageFragmentAdapter(FragmentManager fm, ArrayList<MessageFragment> fragmentList, String[] tabsTitle) {
            super(fm);
            this.fragmentList = fragmentList;
            this.tabsTitle = tabsTitle;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabsTitle[position];
        }
    }


}
