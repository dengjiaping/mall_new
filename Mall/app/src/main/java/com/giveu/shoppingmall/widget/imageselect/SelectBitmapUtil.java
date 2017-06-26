package com.giveu.shoppingmall.widget.imageselect;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SelectBitmapUtil {

	public static int max = 0;
	public static List<Bitmap> bmp = new ArrayList<Bitmap>();
	public static List<String> drr = new ArrayList<String>();
	public static ArrayList<ImageItem> selected = new ArrayList<ImageItem>();
	
	public static Bitmap revitionImageSize(String path) throws IOException {
		Bitmap bitmap = null;
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(in, null, options);
		in.close();
		int i = 0;

		while (true) {
			if ((options.outWidth >> i <= 1000)
					&& (options.outHeight >> i <= 1000)) {
				in = new BufferedInputStream(new FileInputStream(new File(path)));
				options.inSampleSize = (int) Math.pow(1.9D, i);
				options.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeStream(in, null, options);
				break;
			}
			i += 1;
		}
		return bitmap;
	}
	///////////////////////
	public static void compressBmpToFile(String path){
		BufferedInputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(new File(path)));
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = false;
			Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int option = 80;
			bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
			while (baos.toByteArray().length / 1024 > 250) { 
				baos.reset();
				option -= 10;
				bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
			}
			
			//FileUtils.saveBitmap(bitmap, "2.jpg");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}
	
}
