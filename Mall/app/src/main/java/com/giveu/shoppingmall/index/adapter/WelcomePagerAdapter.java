package com.giveu.shoppingmall.index.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


public class WelcomePagerAdapter extends PagerAdapter implements View.OnClickListener{
	List<RadioButton> rbList = null;
	List<Integer> data = null;
	LayoutInflater myInflater = null;
	LinearLayout ll_dot = null;

	public WelcomePagerAdapter(WelcomePagerAdapterListener welcomePagerAdapterListener, LayoutInflater myInflater2, LinearLayout ll_dot2) {
		rbList = new ArrayList<RadioButton>();
		myInflater = myInflater2;
		ll_dot = ll_dot2;
		this.welcomePagerAdapterListener = welcomePagerAdapterListener;
	}

	public void setData(List<Integer> data) {
		this.data = data;
		
		if(!CommonUtils.isNullOrEmpty(data)){
			initDots(data.size());
		}
	}
	
	public List<Integer> getData() {
		return data;
	}
	
	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
		object = null;

	}

	@Override
	public int getItemPosition(Object object) {

		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Integer curId = data.get(position % data.size());
		
		View itemView = myInflater.inflate(R.layout.welcome_viewpager_item, null);
	
		TextView tv_into = (TextView) itemView.findViewById(R.id.tv_into);
		tv_into.setOnClickListener(this);
		ImageView iv = (ImageView) itemView.findViewById(R.id.iv);
		iv.setImageResource(curId);
		
		if (position == data.size()-1) {
			tv_into.setVisibility(View.VISIBLE);
		}else{
			tv_into.setVisibility(View.GONE);
		}
		
		container.addView(itemView);
		return itemView;
	}

	public void initDots(int number) {
		for (int i = 0; i < number; i++) {
			LinearLayout child = (LinearLayout) myInflater.inflate(R.layout.welcome_viewpager_dot, null);
			RadioButton rb = (RadioButton) child.findViewById(R.id.rb);
			rbList.add(rb);
			ll_dot.addView(child);
		}
		if (rbList.get(0) != null) {
			rbList.get(0).setChecked(true);
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {
			clearChecked();
			int curPosition = (position) % data.size();
			rbList.get(curPosition).setChecked(true);
		}

		public void clearChecked() {
			for (RadioButton rb : rbList) {
				if (rb != null) {
					rb.setChecked(false);
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_into:
			if(welcomePagerAdapterListener != null){
				welcomePagerAdapterListener.clickInto();
			}
			break;
		}
	}
	
	public WelcomePagerAdapterListener welcomePagerAdapterListener;
	public interface WelcomePagerAdapterListener{
		void clickInto();
	}
	
	
	

}
