package com.giveu.shoppingmall.index.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.model.bean.response.ShoppingBean;
import com.giveu.shoppingmall.index.view.activity.ShoppingListActivity;
import com.giveu.shoppingmall.utils.ImageUtils;

import java.util.List;

/**
 * Created by 524202 on 2017/9/4.
 */

public class ShopClassifyContentAdapter extends RecyclerView.Adapter<ShopClassifyContentAdapter.TitleHolder> {

    private List<ShoppingBean> datas;
    private Context mContext;

    public ShopClassifyContentAdapter(Context context, List<ShoppingBean> lists) {
        mContext = context;
        datas = lists;
    }

    @Override
    public int getItemViewType(int position) {
        return datas.get(position).getType();
    }

    @Override
    public TitleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
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
    public void onBindViewHolder(TitleHolder holder, final int position) {
        int type = getItemViewType(position);
        holder.textView.setText(datas.get(position).getTypesBean().getName());
        if (type == 1) {
            ContentHolder contentHolder = (ContentHolder) holder;
            String iconUrl = datas.get(position).getTypesBean().getIconSrc();
            if (iconUrl != null) {

                ImageUtils.loadImage(iconUrl, R.drawable.defalut_img_400_400, contentHolder.image);

            }
            contentHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShoppingListActivity.startIt((Activity) mContext, datas.get(position).getTypesBean().getId());
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

        TitleHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_content_text);
        }
    }

    private class ContentHolder extends TitleHolder {
        LinearLayout layout;
        ImageView image;

        ContentHolder(View itemView) {
            super(itemView);
            layout = (LinearLayout) itemView.findViewById(R.id.item_content_ll);
            image = (ImageView) itemView.findViewById(R.id.item_content_image);
        }
    }
}
