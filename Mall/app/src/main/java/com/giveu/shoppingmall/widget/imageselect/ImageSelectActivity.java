package com.giveu.shoppingmall.widget.imageselect;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.giveu.shoppingmall.R;
import com.giveu.shoppingmall.base.BaseActivity;
import com.giveu.shoppingmall.utils.FileUtils;
import com.giveu.shoppingmall.utils.ImageUtils;
import com.giveu.shoppingmall.utils.StringUtils;
import com.giveu.shoppingmall.widget.emptyview.CommonLoadingView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageSelectActivity extends BaseActivity implements OnClickListener, SelectImageAdapter.OnClickSelectImageListener, FloderView.OnClickFloderListener {
    /**
     * 图片名字
     */
    public String identity_pic = "identity_pic";

    /**
     * 相机拍照
     */
    public static int CODE_CAMERA = 234;

    /**
     * 大图预览
     */
    public static final int CODE_IMAGE_PREVIEW = 235;

    /**
     * 选中图片预览
     */
    public static int CODE_IMAGE_SELECT_PREVIEW = 236;

    @ViewInject(R.id.gridview)
    private GridView gridview;
    @ViewInject(R.id.ll_top)
    private LinearLayout ll_top;
    @ViewInject(R.id.txt_title)
    private TextView txt_title;
    @ViewInject(R.id.txt_ok)
    private TextView txt_ok;
    @ViewInject(R.id.txt_cancle)
    private TextView txt_cancle;
    @ViewInject(R.id.txt_preview)
    private TextView txt_preview;
    @ViewInject(R.id.ll_bottom)
    private LinearLayout ll_bottom;
    @ViewInject(R.id.top_bar)
    private RelativeLayout top_bar;
    @ViewInject(R.id.floderView)
    private FloderView floderView;
    @ViewInject(R.id.loading)
    private CommonLoadingView common_loading;


    private ImageInfo imageInfo;
    private ExecutorService buildImageService;
    private SelectImageAdapter mAdapter;
    private FloderRunable mFloderRunable;

    private boolean isHasAddCamera;

    /**
     * 请求码
     */
    private int form;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            common_loading.disLoading();
            if (msg.what == 0) {
                if (!isHasAddCamera) {
                    imageInfo.allImageItem.add(0, new ImageItem(true));
                    isHasAddCamera = true;
                }
                txt_title.setText("所有图片");
                mAdapter.setItemList(imageInfo.allImageItem);
                floderView.setData(imageInfo);
            } else {
                ImageFloder imageFloder = (ImageFloder) msg.obj;
                if (imageFloder.imageItmeList == null) {
                    imageFloder.imageItmeList = new ArrayList<ImageItem>();
                }
                boolean isHasCamera = false;
                for (ImageItem item : imageFloder.imageItmeList) {
                    if (item.isTakeCamera) {
                        isHasCamera = true;
                        break;
                    }
                }
                if (!isHasCamera) {
                    imageFloder.imageItmeList.add(0, new ImageItem(true));
                }
                txt_title.setText(imageFloder.getName());
                mAdapter.setItemList(imageFloder.imageItmeList);
                gridview.smoothScrollToPosition(0);
            }
        }
    };

    @Override
    public void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_image_select);
        baseLayout.setTitleBarAndStatusBar(false, true);

        ViewUtils.inject(this);
        init();
    }

    @Override
    public void setListener() {

    }

    @Override
    public void setData() {

    }

    private void init() {
        txt_title.setText("所有图片");
        txt_cancle.setOnClickListener(this);
        ll_top.setOnClickListener(this);
        txt_preview.setOnClickListener(this);
        txt_ok.setOnClickListener(this);
        ll_bottom.setOnClickListener(this);
        floderView.setOnClickFloderListener(this);
        setSelectText();
        mAdapter = new SelectImageAdapter(this);
        mAdapter.setOnClickSelectImageListener(this);
        gridview.setAdapter(mAdapter);
        gridview.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), true, true));
        loadData();

        SkipUtils.Params1<Integer> params1 = (SkipUtils.Params1<Integer>) SkipUtils.getParams(getIntent());
        if (params1 != null) {
            form = params1.param1;
        }
    }

    private void loadData() {
        if (buildImageService == null) {
            buildImageService = Executors.newSingleThreadExecutor();
        }
        common_loading.showLoading();
        buildImageService.execute(buildAllRunable);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.ll_top:
                floderView.setVisibility(View.VISIBLE);
                break;
            case R.id.txt_cancle:
                back(false);
                break;
            case R.id.txt_ok:
                back(true);
                break;
            case R.id.txt_preview:
                SkipUtils.skipActivityForResult(this, Act_ImageSelectPreActivity.class, null, CODE_IMAGE_SELECT_PREVIEW);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void back(boolean isSetRes) {
        if (isSetRes) {
            setResult(RESULT_OK, new Intent());
        }
        finish();
    }

    @Override
    public void onClickFloder(ImageFloder imageFloder) {
        if (imageFloder.isAllPic) {
            mHandler.sendEmptyMessage(0);
        } else {
            if (imageFloder.isBuildSubImage) {
                Message msg = Message.obtain();
                msg.what = 1;
                msg.obj = imageFloder;
                mHandler.sendMessage(msg);
            } else {
                mFloderRunable = new FloderRunable(imageFloder);
                if (buildImageService == null) {
                    buildImageService = Executors.newSingleThreadExecutor();
                }
                buildImageService.execute(mFloderRunable);
            }
        }
    }

    /**
     * 获取各个文件夹图片
     */
    private class FloderRunable implements Runnable {
        private ImageFloder imageFloder;

        public FloderRunable(ImageFloder imageFloder) {
            super();
            this.imageFloder = imageFloder;
        }

        @Override
        public void run() {
            imageFloder.getAllImageItem();
            Message msg = Message.obtain();
            msg.what = 1;
            msg.obj = imageFloder;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 获取所有图片
     */
    private Runnable buildAllRunable = new Runnable() {

        @Override
        public void run() {
            //Utility.notifySystemGallary(ImageSelectActivity.this);
            if (imageInfo == null) {
                imageInfo = new ImageInfo();
            }
            imageInfo.getImage(ImageSelectActivity.this);
            mHandler.sendEmptyMessage(0);
        }
    };

    /**
     * 设置选中文本
     */
    public void setSelectText() {
        String leftString = ImageInfo.getLeftAllSize();
        if (StringUtils.isNull(leftString)) {
            txt_ok.setTextColor(getResources().getColor(R.color.color_999999));
            txt_ok.setBackgroundResource(R.drawable.bg_select_image_unselect);
            txt_ok.setText("完成");
            txt_ok.setEnabled(false);
        } else {
            txt_ok.setTextColor(getResources().getColor(R.color.white));
            txt_ok.setBackgroundResource(R.drawable.bg_select_image_select);
            txt_ok.setText(leftString);
            txt_ok.setEnabled(true);
        }
        if (ImageInfo.selectImageItems.isEmpty()) {
            txt_preview.setEnabled(false);
            txt_preview.setTextColor(getResources().getColor(R.color.color_999999));
        } else {
            txt_preview.setEnabled(true);
            txt_preview.setTextColor(getResources().getColor(R.color.color_00adb2));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * 选择图片
     */
    @Override
    public void onSelectImage() {
        setSelectText();
    }

    /**
     * 点击拍照
     */
    @Override
    public void onClickCamera() {
        skipToCamera();
    }

    /**
     * 点击预览大图
     */
    @Override
    public void onClickShowBigImage(int poistion) {
        SkipUtils.skipActivityForResult(this, Act_ImagePreActivity.class, SkipUtils.Params3.getInstance(poistion, CODE_IMAGE_PREVIEW, mAdapter.itemList), CODE_IMAGE_PREVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 拍照返回
         */
        if (requestCode == CODE_CAMERA) {
            if (camerafile == null || camerafile.length() == 0 || camearItem == null) {
                return;
            }
            FileUtils.rotateCameraFile(camerafile, this);
            if (form == ImageSelectViewUtil.REQUEST_CODE_SELECT_IMAGE) {

            } else {
                String urlString = ImageUtils.notifysaveImage(this, camerafile, true, false);
                String newPath = getImagePathByUri(this, urlString);
                if (StringUtils.isNotNull(newPath)) {
                    camearItem.imagePath = newPath;
                }
            }
            ImageInfo.selectImageItems.add(camearItem);
            back(true);
        }

        /**
         * 预览大图返回
         */
        if (requestCode == CODE_IMAGE_PREVIEW) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
                setSelectText();
            }
        }

        /**
         * 选择大图返回
         */
        if (requestCode == CODE_IMAGE_SELECT_PREVIEW) {
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
                setSelectText();
            }
        }
    }

    public static String getImagePathByUri(Activity activity, String uriString) {
        if (activity == null) {
            return "";
        }
        if (StringUtils.isNull(uriString)) {
            return "";
        }
        String img_path = "";
        try {
            Uri uri = Uri.parse(uriString);
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = activity.managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            img_path = actualimagecursor.getString(actual_image_column_index);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img_path;
    }

    private File camerafile;
    private ImageItem camearItem;

    /**
     * 跳转到拍照界面
     */
    private void skipToCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerafile = new File(FileUtils.getCacheCanDeleteImageDir(), getFileName(identity_pic, true, false));
        String camearPath = camerafile.getPath();
        Uri imageUri = Uri.fromFile(camerafile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        camearItem = new ImageItem();
        camearItem.imageName = camerafile.getName();
        camearItem.imagePath = camearPath;
        camearItem.isCamera = true;
        startActivityForResult(intent, CODE_CAMERA);
    }

    public static String getFileName(String extraStr, boolean isNeedTime, boolean isShow) {
        return getFileName(extraStr, isNeedTime, isShow, ".jpg");
    }

    /**
     * @param extraStr   文件名
     * @param isNeedTime 是否需要时间
     * @param isShow     是否需要后缀名
     * @param type       后缀名
     * @return
     */
    public static String getFileName(String extraStr, boolean isNeedTime, boolean isShow, String type) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        if (isNeedTime) {
            if (isShow) {
                return format.format(date) + "_" + extraStr + type;
            } else {
                return format.format(date) + "_" + extraStr;
            }
        } else {
            if (isShow) {
                return extraStr + type;
            } else {
                return extraStr;
            }
        }
    }




}
