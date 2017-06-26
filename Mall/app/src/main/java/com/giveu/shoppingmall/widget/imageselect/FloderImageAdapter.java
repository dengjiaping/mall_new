package com.giveu.shoppingmall.widget.imageselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.ParentAdapter;
import com.giveu.shoppingmall.utils.ImageUtils;

public class FloderImageAdapter extends ParentAdapter<ImageFloder> {

    private Context context;
    private int selectIndex;

    public FloderImageAdapter(Context context) {
        super(context);
        this.context = context;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.list_dir_item, null);

            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            holder.txt_name = (TextView) convertView.findViewById(R.id.txt_name);
            holder.txt_count = (TextView) convertView.findViewById(R.id.txt_count);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemList() != null && getItemList().size() > 0) {
            ImageFloder bean = getItemList().get(position);
            if (bean != null) {
                fillData(bean, holder, position);
            }
        }
        return convertView;
    }

    /**
     * 填充数据
     *
     * @param holder
     */
    private void fillData(final ImageFloder bean, final ViewHolder holder, int position) {
        ImageUtils.loadImage(ImageUtils.ImageLoaderType.file, bean.getFirstImagePath(), holder.image);
        if (bean.isAllPic) {
            holder.txt_name.setText("所有图片");
            holder.txt_count.setVisibility(View.GONE);
        } else {
            holder.txt_name.setText(bean.getName());
            holder.txt_count.setText("(" + bean.getCount() + ")");
            holder.txt_count.setVisibility(View.VISIBLE);
        }
        if (position == selectIndex) {
            holder.iv_select.setVisibility(View.VISIBLE);
        } else {
            holder.iv_select.setVisibility(View.INVISIBLE);
        }
    }

    class ViewHolder {
        ImageView image;
        ImageView iv_select;
        TextView txt_name;
        TextView txt_count;
    }
}
