package com.giveu.shoppingmall.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.BaseAdapter;

import com.giveu.shoppingmall.R;

import java.util.List;

/**
 *	项目中用ParentAdapter替代BaseAdapter
 */
public abstract class ParentAdapter<T> extends BaseAdapter {
	public Context mContext;
	public List<T> itemList = null;

	public Context getContext() {
		return mContext;
	}

	public ParentAdapter(Context context) {
		this.mContext = context;
	}

	public List<T> getItemList() {
		return itemList;
	}

	/**
	 * already command notifyDataSetChanged() in this method;
	 * @param itemList
	 */
	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
		notifyDataSetChanged();
	}

	/**
	 * already command notifyDataSetChanged() in this method;
	 * @param itemList
	 */
	public void addItemList(List<T> itemList) {
		if (itemList != null) {
			if (this.itemList != null) {
				this.itemList.addAll(itemList);
			} else {
				this.itemList = itemList;
			}
			notifyDataSetChanged();
		}
	}

	public void addItemList(List<T> itemList, boolean isUp) {
		if (itemList != null) {
			if (this.itemList != null) {
				if(isUp){
					for (int x = itemList.size() - 1; x >= 0; x--) {
						this.itemList.add(0, itemList.get(x));
					}
				}else{
					this.itemList.addAll(itemList);
				}
			} else {
				this.itemList = itemList;
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		return itemList == null ? 0 : itemList.size();
	}

	@Override
	public T getItem(int position) {
		if (itemList != null && position >=0 && position < itemList.size()) {
			return itemList.get(position);
		}else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	/**
	 * itemList清空list集合，并notifyDataSetChanged();
	 */
    public void clear() {
        itemList.clear();
        notifyDataSetChanged();
    }
    /**
	 * itemList移除指定位置的对象，并notifyDataSetChanged();
	 */
    public void remove(int index) {
    	itemList.remove(index);
        notifyDataSetChanged();
    }
    /**
	 * itemList移除对象，并notifyDataSetChanged();
	 */
    public void remove(Object obj) {
    	itemList.remove(obj);
        notifyDataSetChanged();
    }
    
    public static <T extends View> T getViewFromViewHolder(View convertView, int id) {
		SparseArray<View> viewHolder = (SparseArray<View>) convertView.getTag(R.id.sparseArrayViewholderTag);
		if (viewHolder == null) {
			viewHolder = new SparseArray<View>();
			convertView.setTag(R.id.sparseArrayViewholderTag, viewHolder);
		} 

		View childView = viewHolder.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHolder.put(id, childView);
		}

		return (T) childView;
	}
    
	
}
