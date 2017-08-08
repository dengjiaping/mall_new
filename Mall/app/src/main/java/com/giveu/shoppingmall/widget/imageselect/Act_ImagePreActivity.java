package com.giveu.shoppingmall.widget.imageselect;

import android.content.Intent;

import com.giveu.shoppingmall.utils.CommonUtils;
import com.giveu.shoppingmall.utils.ToastUtils;

import java.util.List;

/**
 * 选择图片大图预览
 *
 * @author Administrator
 */
public class Act_ImagePreActivity extends AbsImgPreviewActivity {
    private boolean isFromSelectActivity;

    @Override
    public void afterInit() {
        SkipUtils.Params4<Integer, Integer, List<ImageItem>, Boolean> param4 = (SkipUtils.Params4<Integer, Integer, List<ImageItem>, Boolean>) SkipUtils.getParams(getIntent());
        currIndex = param4.param1;
        imageList = param4.param3;
        isFromSelectActivity = param4.param4;
        if (!CommonUtils.isNullOrEmpty(imageList)) {
            ImageItem removeItem = null;
            for (ImageItem item : imageList) {
                if (item.isTakeCamera) {
                    removeItem = item;
                    break;
                }
            }
            if (removeItem != null) {
                imageList.remove(removeItem);
                currIndex--;
            }
        }
    }

    @Override
    public void doPageSelected(int poistion) {
        if (!CommonUtils.isNullOrEmpty(imageList) && currIndex < imageList.size()) {
            if (ImageSelectViewUtil.isContainsImage(ImageInfo.selectImageItems, imageList.get(currIndex))) {
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
                if (!isFromSelectActivity) {
                    deleteImageInViewPager(viewPager, pagerAdapter, currIndex);
                }
            } else {
                if (ImageInfo.selectImageItems.size() >= ImageInfo.MAX_SELECT_SIZE) {
                    ToastUtils.showShortToast("您最多选择" + ImageInfo.MAX_SELECT_SIZE + "张图片");
                    return;
                }
                check.setChecked(true);
                ImageInfo.selectImageItems.add(imageItem);
            }
        }
    }

    private void deleteImageInViewPager(FixedViewPager viewPager, ImagePreViewAdapter pa, int currIndex) {
        if (pa.imageList.size() == 1) {
            back();
            return;
        }

        pa.imageList.remove(currIndex);
        pa.notifyDataSetChanged();
        viewPager.setCurrentItem(currIndex);
        onPageSelected(currIndex);
    }


}
