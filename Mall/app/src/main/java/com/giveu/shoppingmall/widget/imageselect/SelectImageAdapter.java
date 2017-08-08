package com.giveu.shoppingmall.widget.imageselect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.ParentAdapter;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectImageAdapter extends ParentAdapter<ImageItem> {

    private Context context;
    private int width;
    private LayoutParams params;
    List<ImageItem> list = new ArrayList<>();
    public SelectImageAdapter(Context c) {
        super(c);

        this.context = c;
        width = DensityUtils.getWidth() / 3;
        params = new LayoutParams(width, width);
    }

    public List<ImageItem> getImageList(){
        return list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.image_select_view_grid_item, null);

            holder.image_check = (ImageView) convertView.findViewById(R.id.image_check);
            holder.image_photo = (ImageView) convertView.findViewById(R.id.image_photo);
            holder.image = (ImageView) convertView.findViewById(R.id.iv_imageselect);
            holder.check = (CheckBox) convertView.findViewById(R.id.check_isselected);
            holder.container = (RelativeLayout) convertView.findViewById(R.id.container);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItemList() != null && getItemList().size() > 0) {
            ImageItem bean = getItemList().get(position);
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

    private void fillData(final ImageItem bean, final ViewHolder holder, final int position) {
        if (bean.isTakeCamera) {
            holder.image.setVisibility(View.INVISIBLE);
            holder.check.setVisibility(View.INVISIBLE);
            holder.image_photo.setVisibility(View.VISIBLE);
        } else {
            holder.check.setVisibility(View.VISIBLE);
            holder.image_photo.setVisibility(View.INVISIBLE);
            holder.image.setVisibility(View.VISIBLE);
            ImageUtils.loadImage(ImageUtils.ImageLoaderType.file, bean.imagePath, holder.image);
            if (ImageSelectViewUtil.isContainsImage(ImageInfo.selectImageItems, bean)) {
                holder.check.setChecked(true);
                holder.image.setColorFilter(Color.parseColor("#77000000"));
            } else {
                holder.check.setChecked(false);
                holder.image.setColorFilter(null);
            }
            holder.image_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ImageSelectViewUtil.isContainsImage(list, bean)) {
                        holder.check.setChecked(false);
                        holder.image.setColorFilter(null);
                        ImageSelectViewUtil.removeImage(ImageInfo.selectImageItems, bean);
                    } else {
                        if (ImageInfo.selectImageItems.size() >= ImageInfo.MAX_SELECT_SIZE) {
                            ToastUtils.showShortToast("你最多可以选择" + ImageInfo.imageSize + "张图片");
                            return;
                        }
                        if (!ImageInfo.isImage(bean.imagePath)) {
                            ToastUtils.showShortToast("图片已损坏");
                            return;
                        }
                        holder.check.setChecked(true);
                        holder.image.setColorFilter(Color.parseColor("#77000000"));
                        list.add(bean);
                      //  ImageInfo.selectImageItems.add(bean);
                    }

                    if (onClickSelectImageListener != null) {
                        onClickSelectImageListener.onSelectImage();
                    }
                }
            });
        }

        holder.container.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (bean.isTakeCamera) {
                    if (ImageInfo.selectImageItems.size() >= ImageInfo.MAX_SELECT_SIZE) {
                        ToastUtils.showShortToast("你最多可以选择" + ImageInfo.imageSize + "张图片");
                        return;
                    }
                    if (onClickSelectImageListener != null) {
                        onClickSelectImageListener.onClickCamera();
                    }
                } else {
                    if (onClickSelectImageListener != null) {
                        onClickSelectImageListener.onClickShowBigImage(position);
                    }
                }
            }
        });
        holder.container.setLayoutParams(params);
    }

    class ViewHolder {
        ImageView image_check, image_photo, image;
        CheckBox check;
        RelativeLayout container;
    }


    private OnClickSelectImageListener onClickSelectImageListener;

    public void setOnClickSelectImageListener(OnClickSelectImageListener onClickSelectImageListener) {
        this.onClickSelectImageListener = onClickSelectImageListener;
    }


    public interface OnClickSelectImageListener {
        /**
         * 点击照相
         */
        public void onClickCamera();

        /**
         * 显示大图
         *
         * @param poistion
         */
        public void onClickShowBigImage(int poistion);

        /**
         * 选择图片
         */
        public void onSelectImage();
    }
}
