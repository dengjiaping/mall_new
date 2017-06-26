package com.giveu.shoppingmall.widget.imageselect;

import android.content.Intent;

import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.ArrayList;

import static com.giveu.shoppingmall.widget.imageselect.ImageSelectViewUtil.isContainsImage;

/**
 * 选择图片大图预览
 *
 * @author Administrator
 */
public class Act_ImageSelectPreActivity extends AbsImgPreviewActivity {

    @Override
    public void afterInit() {
        if (null == imageList) {
            imageList = new ArrayList<ImageItem>();
        }
        for (ImageItem item : ImageInfo.selectImageItems) {
            imageList.add(item);
        }
    }

    @Override
    public void doPageSelected(int poistion) {
        if (!CommonUtils.isNullOrEmpty(imageList) && currIndex < imageList.size()) {
            if (isContainsImage(ImageInfo.selectImageItems, imageList.get(currIndex))) {
                check.setChecked(true);
            } else {
                check.setChecked(false);
            }
        }
    }

    @Override
    public void back() {
        setResult(RESULT_OK, new Intent());
        finish();
    }

    @Override
    public void onClickRight() {
        if (currIndex < imageList.size()) {
            ImageItem imageItem = imageList.get(currIndex);
            if (ImageSelectViewUtil.isContainsImage(ImageInfo.selectImageItems, imageItem)) {
                check.setChecked(false);
                ImageSelectViewUtil.removeImage(ImageInfo.selectImageItems, imageItem);
            } else {
                check.setChecked(true);
                ImageInfo.selectImageItems.add(imageItem);
            }
        }
    }
}
