package com.giveu.shoppingmall.index.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.index.bean.ShoppingBean;
import com.giveu.shoppingmall.index.view.activity.CommodityDetailActivity;

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class GodsAdapter extends RecyclerView.Adapter {

    private List<ShoppingBean> datas;
    private Context mContext;

    public GodsAdapter(Context context, List<ShoppingBean> lists) {
        mContext = context;
        datas = lists;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == 0) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_classify_title_item, null);
            return new TitleHolder(view);
        } else if (viewType == 1) {
            view = LayoutInflater.from(mContext).inflate(R.layout.adapter_shopping_classify_content_item, null);
            return new ContentHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int type = getItemViewType(position);
        if (type == 0) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.textView.setText(datas.get(position).getTitle());
        } else if (type == 1) {
            final ContentHolder contentHolder = (ContentHolder) holder;
            contentHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommodityDetailActivity.startIt(mContext, false);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class TitleHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TitleHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_text);
        }
    }

    class ContentHolder extends RecyclerView.ViewHolder {
        LinearLayout layout;

        public ContentHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.item_content_ll);
        }
    }
}
