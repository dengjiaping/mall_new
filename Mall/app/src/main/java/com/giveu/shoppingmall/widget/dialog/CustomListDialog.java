package com.giveu.shoppingmall.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.CustomDialog;
import com.giveu.shoppingmall.base.ParentAdapter;

import java.util.List;

/**
 * Created by zhengfeilong on 16/5/16.
 */
public class CustomListDialog extends CustomDialog {
    AdapterView.OnItemClickListener onItemClickListener;
    ItemAdapter itemAdapter;

    public CustomListDialog(Activity context, AdapterView.OnItemClickListener onItemClickListener) {
        super(context, R.layout.custom_list_dialog, R.style.date_dialog_style, Gravity.CENTER, false);

        this.onItemClickListener = onItemClickListener;
    }

    @Override
    protected void initView(View contentView) {
        ListView lv = (ListView) contentView.findViewById(R.id.lv);

        itemAdapter = new ItemAdapter(mAttachActivity);
        lv.setAdapter(itemAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dismiss();

                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    public void setData(List<CharSequence> stringList){
        itemAdapter.setItemList(stringList);
    }
    public List<CharSequence> getData(){
        return itemAdapter.getItemList();
    }

    class ItemAdapter extends ParentAdapter<CharSequence> {

        public ItemAdapter(Context context) {
            super(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = View.inflate(mContext, R.layout.custom_list_dialog_item, null);
            }
            TextView tv = getViewFromViewHolder(convertView, R.id.tv);

            CharSequence item = getItem(position);
            tv.setText(item);
            return convertView;
        }
    }


}
