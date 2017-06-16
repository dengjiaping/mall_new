package com.giveu.shoppingmall.index.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.index.adapter.WelcomePagerAdapter;
import com.giveu.shoppingmall.utils.sharePref.SharePrefUtil;

import java.util.ArrayList;
import java.util.List;


public class WelcomeActivity extends BaseActivity{

	@Override
	public void initView(Bundle savedInstanceState) {
		setContentView(R.layout.welcome);
		baseLayout.setTitleBarAndStatusBar(false, false);
		startViewPager();
	}

	@Override
	public void setListener() {

	}

	@Override
	public void setData() {

	}


	protected void startViewPager() {
		ViewPager viewpager = (ViewPager) this.findViewById(R.id.viewpager);
		LinearLayout ll_dot = (LinearLayout) findViewById(R.id.ll_dot);

		WelcomePagerAdapter welcomePagerAdapter = new WelcomePagerAdapter(new WelcomePagerAdapter.WelcomePagerAdapterListener() {
			@Override
			public void clickInto() {
				SharePrefUtil.setNeedWelcome(false);
				MainActivity.startIt(mBaseContext);

				finish();
			}
		},getLayoutInflater(), ll_dot);

		List<Integer> imgUrls = new ArrayList<Integer>();
		imgUrls.add(R.drawable.welcome_01);
		imgUrls.add(R.drawable.welcome_02);
		imgUrls.add(R.drawable.welcome_03);

		welcomePagerAdapter.setData(imgUrls);
		viewpager.setAdapter(welcomePagerAdapter);
		viewpager.setOnPageChangeListener(welcomePagerAdapter.new MyOnPageChangeListener());
	}


}
