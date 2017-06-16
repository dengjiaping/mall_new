package com.giveu.shoppingmall.view.imageselect;

import com.giveu.shoppingmall.utils.CommonUtils;

import java.util.List;

/**
 * Created by 508632 on 2016/12/24.
 */

public class ImageSelectViewUtil {
	public static final int REQUEST_CODE_SELECT_IMAGE = 1;// 相册(自定义)

	/**
	 * 判断集合是否包含图片
	 *
	 * @param list
	 * @param item
	 * @return
	 */
	public static boolean isContainsImage(List<ImageItem> list, ImageItem item) {
		if (CommonUtils.isNullOrEmpty(list) || item == null) {
			return false;
		}
		for (ImageItem imageItem : list) {
			if (imageItem.imagePath.equals(item.imagePath)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 从集合移除图片
	 *
	 * @param list
	 * @param item
	 */
	public static void removeImage(List<ImageItem> list, ImageItem item) {
		if (CommonUtils.isNullOrEmpty(list) || item == null) {
			return;
		}
		ImageItem removeItem = null;
		for (ImageItem imageItem : list) {
			if (imageItem.imagePath.equals(item.imagePath)) {
				removeItem = imageItem;
			}
		}
		if (removeItem != null) {
			list.remove(removeItem);
		}
	}

}
