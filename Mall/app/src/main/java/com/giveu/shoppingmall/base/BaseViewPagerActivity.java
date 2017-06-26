package com.giveu.shoppingmall.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.widget.viewpager.PagerSlidingTabStrip;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * activity里面包含多个fragment，fragment可以滑动切换
 */
public abstract class BaseViewPagerActivity extends BaseActivity {
	ViewHolder viewhodler = null;
	MyFragmentAdapter myFragmentAdapter;
	private List<BaseFragment> frags = null;
	private String[] tabsText = null;
	View base_viewpager = null;


	@Override
	public void initView(Bundle savedInstanceState) {
		base_viewpager = View.inflate(this, R.layout.activity_base_viewpager, null);

		viewhodler = new ViewHolder(base_viewpager);
		myFragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
		viewhodler.viewpager.setAdapter(myFragmentAdapter);
		viewhodler.viewpager.setOffscreenPageLimit(2);
		viewhodler.pagerSlidingTabStrip.setViewPager(viewhodler.viewpager);
		viewhodler.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}

			@Override
			public void onPageSelected(int position) {
/*				BaseFragment item = (BaseFragment) myFragmentAdapter.getItem(position);
				if ( !item.isDataInitiated() ){
					item.initWithDataDelay();
				}*/
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

	}



	public void setViewPagerInContentView(int layoutResID, int viewpagerLayoutId) {
		super.setContentView(layoutResID);
		View view = findViewById(viewpagerLayoutId);
		if (view != null && view instanceof LinearLayout){
			LinearLayout viewpagerLayout = (LinearLayout) view;
			viewpagerLayout.removeAllViews();
			viewpagerLayout.addView(base_viewpager);
		}
	}

	private class MyFragmentAdapter extends FragmentStatePagerAdapter {

		public MyFragmentAdapter(FragmentManager fm) {
			super(fm);

			frags = getFragments();
			tabsText = getTabTexts();
		}

		@Override
		public Fragment getItem(int position) {
			return frags.get(position);
		}

		@Override
		public int getCount() {
			return frags.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return tabsText[position];
		}
	}

	public abstract String[] getTabTexts();

	public abstract List<BaseFragment> getFragments();




	static class ViewHolder {
		@BindView(R.id.pager_sliding_tab_strip)
		PagerSlidingTabStrip pagerSlidingTabStrip;
		@BindView(R.id.viewpager)
		ViewPager viewpager;

		ViewHolder(View view) {
			ButterKnife.bind(this, view);
		}
	}

	public ViewHolder getBaseViewPagerHodler() {
		return viewhodler;
	}




}
