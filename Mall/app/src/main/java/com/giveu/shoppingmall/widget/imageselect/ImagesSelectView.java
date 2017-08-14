package com.giveu.shoppingmall.widget.imageselect;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.DensityUtils;
import com.giveu.shoppingmall.utils.FileUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 选择图片
 */
public class ImagesSelectView extends LinearLayout {
    /**
     * 图片名字
     */
    public String identity_pic = "identity_pic";
    /**
     * 选择图片
     */
    public static final int REQUEST_CODE_SELECT_PHOTO = 200;
    /**
     * 实名认证查看大图
     */
    public static final int REQUEST_CODE_BIGIMAGE = 201;

    public Activity mContext;
    public Resources resources;
    /**
     * 分割索引
     */
    private int divide_index;
    /**
     * 默认图片
     */
    public int default_image;

    /**
     * 圖片的最大個數
     */
    public int maxSize = 9;
    /**
     * 选择图片列表
     */
    public static ArrayList<ImageItem> imageSelectList = new ArrayList<ImageItem>();
    /**
     * 第一张文字
     */
    public TextView txt1;
    /**
     * 第2张文字
     */
    public TextView txt2;
    /**
     * 图片集合
     */
    public List<ImageView> imageList = new ArrayList<ImageView>();
    /**
     * 是 fragment 设置fragment
     */
    public Fragment mFragment;
    /**
     * 第一张图片文字
     */
    private String imageTxt1;
    /**
     * 第二张图片文字
     */
    private String imageTxt2;
    int leftMargin;
    int rightMargin;


    public ImagesSelectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = (Activity) context;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        mInflater.inflate(R.layout.image_select_view, this, true);
        init(attrs);
    }

    /**
     * 初始化操作
     */
    private void init(AttributeSet attrs) {
        resources = mContext.getResources();
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.ImageSelectView);
        int count = ta.getIndexCount();
        for (int i = 0; i < count; i++) {
            int itemId = ta.getIndex(i);
            switch (itemId) {
                case R.styleable.ImageSelectView_divide_index://
                    divide_index = ta.getInteger(itemId, 0);
                    break;
                case R.styleable.ImageSelectView_default_image://
                    default_image = ta.getResourceId(itemId, -1);
                    break;
                case R.styleable.ImageSelectView_image_txt1://
                    imageTxt1 = ta.getString(itemId);
                    break;
                case R.styleable.ImageSelectView_image_txt2://
                    imageTxt2 = ta.getString(itemId);
                    break;
                case R.styleable.ImageSelectView_image_max_size://
                    maxSize = ta.getInteger(itemId, 9);
                    break;
                case R.styleable.ImageSelectView_leftMargin://
                    leftMargin = ta.getInteger(itemId, 0);
                    break;
                case R.styleable.ImageSelectView_rightMargin://
                    rightMargin = ta.getInteger(itemId, 0);
                    break;
            }
        }
        ta.recycle();
        initView();
    }

    /**
     * 找控件
     */
    public void initView() {
        setOnSelectImageListener(new ModeDefault());
        /**
         * 文字控件
         */
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        if (StringUtils.isNotNull(imageTxt1)) {
            txt1.setText(imageTxt1);
        }
        if (StringUtils.isNotNull(imageTxt2)) {
            txt2.setText(imageTxt2);
        }

        /**
         * 显示图片
         */
        for (int x = 0; x < maxSize; x++) {

            ImageView imageView = (ImageView) findViewById(getResId("image" + (x + 1)));
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 4) / 3;
            params.height = DensityUtils.dip2px(75);
            imageView.setLayoutParams(params);

            RelativeLayout rl = (RelativeLayout) findViewById(getResId("rl" + (x + 1)));
            LayoutParams paramsRl = (LayoutParams) rl.getLayoutParams();
            paramsRl.width = (DensityUtils.getWidth() - DensityUtils.dip2px(15) * 4) / 3;
            paramsRl.height = DensityUtils.dip2px(75);
            rl.setLayoutParams(paramsRl);

            /**
             * 删除图片
             */
            ImageView delete = (ImageView) findViewById(getResId("delete" + (x + 1)));
            delete.setVisibility(View.INVISIBLE);

            imageView.setTag(R.id.select_image_rl, rl);
            imageView.setTag(R.id.select_image_delete, delete);

            if (x <= divide_index) {
                rl.setVisibility(View.VISIBLE);
            } else {
                rl.setVisibility(View.GONE);
            }
            imageView.setVisibility(View.VISIBLE);
            imageList.add(imageView);

            final int index = x;
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.closeSoftKeyBoard(mContext);
                    if (onSelectImageListener != null) {
                        onSelectImageListener.onSelectImage(index);
                    }
                }
            });
            delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onSelectImageListener != null) {
                        onSelectImageListener.onDeleteImage(index);
                    }
                }
            });
        }
    }

    /**
     * 设置图片
     */
    public void reSetImage(List<ImageItem> list) {
        if (list.isEmpty()) {
            for (int x = 0; x < imageList.size(); x++) {
                RelativeLayout rl = (RelativeLayout) imageList.get(x).getTag(R.id.select_image_rl);
                ImageView delete = (ImageView) imageList.get(x).getTag(R.id.select_image_delete);
                if (x <= divide_index) {
                    rl.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    imageList.get(x).setImageResource(default_image);
                } else {
                    rl.setVisibility(View.GONE);
                }
            }
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
        } else {
            for (int x = 0; x < imageList.size(); x++) {
                RelativeLayout rl = (RelativeLayout) imageList.get(x).getTag(R.id.select_image_rl);
                ImageView delete = (ImageView) imageList.get(x).getTag(R.id.select_image_delete);
                if (x < list.size()) {
                    rl.setVisibility(View.VISIBLE);
//                   delete.setVisibility(LoginView.VISIBLE);
                    delete.setVisibility(View.VISIBLE);
                    ImageUtils.loadImage(ImageUtils.ImageLoaderType.file, list.get(x).imagePath, imageList.get(x));
                } else if (x == list.size()) {
                    rl.setVisibility(View.VISIBLE);
                    delete.setVisibility(View.GONE);
                    imageList.get(x).setImageResource(default_image);
                } else {
                    rl.setVisibility(View.GONE);
                }
            }
            if (list.size() == 1) {
                txt1.setVisibility(View.GONE);
                txt2.setVisibility(View.VISIBLE);
            } else {
                txt1.setVisibility(View.GONE);
                txt2.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 跳转到选择图片界面
     */
    private void skipForImageSelect() {
        ImageInfo.MAX_SELECT_SIZE = maxSize;
        ImageInfo.selectImageItems.clear();
        ImageInfo.selectImageItems.addAll(imageSelectList);
        //6.0以上系统需先申请存储权限和相机权限
        if (onPermissionListener != null) {
            onPermissionListener.requestPermission();
        } else {
            skipForResult();
        }
//        Intent it = new Intent(mAttachActivity, ImageSelectActivity.class);
//        mFragment.startActivityForResult(it, REQUEST_CODE_SELECT_PHOTO);
    }

    /**
     * 处理权限
     *
     * @param onPermissionListener
     */
    public void setOnPermissionListener(OnPermissionListener onPermissionListener) {
        this.onPermissionListener = onPermissionListener;
    }

    public void skipForResult() {
        SkipUtils.skipForResult(mContext, mFragment, ImageSelectActivity.class, REQUEST_CODE_SELECT_PHOTO);
    }


    private OnPermissionListener onPermissionListener;


    public interface OnPermissionListener {
        void requestPermission();
    }

    /**
     * 全部处理方式
     */
    public class ModeDefault implements onSelectImageListener {

        @Override
        public void onSelectImage(final int index) {
            if (index < imageSelectList.size()) {
                SkipUtils.skipActivityForResult(mContext, Act_ImagePreActivity.class, SkipUtils.Params5.getInstance(index, ImageSelectActivity.CODE_IMAGE_PREVIEW, imageSelectList, false), ImageSelectActivity.CODE_IMAGE_PREVIEW);
            } else {
                skipForImageSelect();
            }
        }

        @Override
        public void onDeleteImage(int index) {
            if (CommonUtils.isNullOrEmpty(imageSelectList) || index >= imageSelectList.size()) {
                return;
            }
            imageSelectList.remove(index);
            ImageInfo.selectImageItems.remove(index);
            reSetImage(imageSelectList);

            if (onCheckListener != null) {
                onCheckListener.onCheck();
            }
        }
    }

    /**
     * 返回重新赋值
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void doResult(int requestCode, int resultCode, Intent data) {
        if ((requestCode == REQUEST_CODE_SELECT_PHOTO || requestCode == ImageSelectActivity.CODE_IMAGE_PREVIEW) && resultCode == Activity.RESULT_OK) {
            imageSelectList.clear();
            imageSelectList.addAll(ImageInfo.selectImageItems);
            reSetImage(imageSelectList);
        }
    }

    /**
     * 重新设置
     */
    public void clearImageFile() {
        ImageInfo.reSetSelect();
        imageSelectList.clear();
        FileUtils.deleteDirInSelectImagesView();
    }

    public void clearImageNotDeleteFile() {
        ImageInfo.reSetSelect();
        imageSelectList.clear();
    }

    /**
     * 设置需要上传的图片
     *
     * @param uploadFiles
     */
    public void setUploadImage(Map<String, File> uploadFiles) {
        if (uploadFiles == null) {
            return;
        }
        uploadFiles.clear();
        // 添加身份认证上传图片
        if (!imageSelectList.isEmpty()) {
            for (ImageItem item : imageSelectList) {
                String path = item.imagePath;
                String name = path.substring(path.lastIndexOf("/") + 1, path.length());
                try {
                    File file = FileUtils.saveBitmap(SelectBitmapUtil.revitionImageSize(path), name);
                    uploadFiles.put(name, file);
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }

    /**
     * 获取选中的图片集合
     */
    public List<ImageItem> getSelectImages() {
        List<ImageItem> imageItemList = new ArrayList<ImageItem>();
        if (!imageSelectList.isEmpty()) {
            for (ImageItem item : imageSelectList) {
                imageItemList.add(item);
            }
        }
        return imageItemList;
    }


    /**
     * 检查是否添加图片
     *
     * @return
     */
    public boolean check() {
        if (CommonUtils.isNullOrEmpty(imageSelectList)) {
            return false;
        }

        if (divide_index > 0) {
            if (imageSelectList.size() < divide_index + 1) {
                return false;
            }
        }

        return true;
    }


    private int getResId(String resName) {
        return resources.getIdentifier(resName, "id", mContext.getPackageName());
    }

    public OnCheckListener onCheckListener;
    public onSelectImageListener onSelectImageListener;

    public void setOnSelectImageListener(ImagesSelectView.onSelectImageListener onSelectImageListener) {
        this.onSelectImageListener = onSelectImageListener;
    }

    public void setOnCheckListener(ImagesSelectView.OnCheckListener onCheckListener) {
        this.onCheckListener = onCheckListener;
    }

    /**
     * 选择和删除图片
     */
    public interface onSelectImageListener {
        /**
         * 选择图片
         *
         * @param index
         */
        public void onSelectImage(int index);

        /**
         * 删除图片
         *
         * @param index
         */
        public void onDeleteImage(int index);
    }

    /**
     * 检查是否选择接口
     */
    public interface OnCheckListener {
        /**
         * 检查是否选择了图片
         *
         * @return
         */
        public boolean onCheck();
    }

    /**
     * 设置 fragemnt
     *
     * @param mFragment
     */
    public void setFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }
}
