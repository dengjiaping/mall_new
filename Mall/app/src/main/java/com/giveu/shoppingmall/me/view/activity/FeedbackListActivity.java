package com.giveu.shoppingmall.me.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.me.view.fragment.FeedBackListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 处理记录（已处理未处理）
 */

public class FeedbackListActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    FeedBackListFragment fragment1 = null;
    FeedBackListFragment fragment2 = null;
    @BindView(R.id.rb_in_processed)
    RadioButton rbInProcessed;
    @BindView(R.id.rb_already_processed)
    RadioButton rbAlreadyProcessed;
    @BindView(R.id.rg_feedback)
    RadioGroup rgFeedback;

    public static void startIt(Activity mActivity) {
        Intent intent = new Intent(mActivity, FeedbackListActivity.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void setListener() {
        super.setListener();
        rgFeedback.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_in_processed:
                        //处理中
                        viewpager.setCurrentItem(0);
                        break;
                    case R.id.rb_already_processed:
                        //已处理
                        viewpager.setCurrentItem(1);
                        break;
                }
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0){
                    case 0:
                        rbInProcessed.setChecked(true);
                        break;
                    case 1:
                        rbAlreadyProcessed.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_feedback_list);
        baseLayout.setTitle("处理记录");
    }

    @Override
    public void setData() {
        if (fragment1 == null) {
            fragment1 = new FeedBackListFragment();
        }
        if (fragment2 == null) {
            fragment2 = new FeedBackListFragment();
        }
        List<Fragment> fragments = new ArrayList<Fragment>();
        fragments.add(fragment1);
        fragments.add(fragment2);
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    public class FragAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;

        public FragAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            mFragments = fragments;
        }

        @Override
        public Fragment getItem(int arg0) {
            return mFragments.get(arg0);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

    }
}