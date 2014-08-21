package com.example.baidumap;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 * ToastUtils
 */
public class ToastUtils {

	private static Toast mToast;
	private static Handler mHandler = new Handler();

	private static Runnable r = new Runnable() {
		public void run() {
			mToast.cancel();
		}
	};

	public static void showToast(Context mContext, String text, int duration) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, duration);
		mHandler.postDelayed(r, duration);
		mToast.show();
	}

	public static void showToast(Context mContext, int resId, int duration) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(mContext.getResources().getText(resId));
		else
			mToast = Toast.makeText(mContext,
					mContext.getResources().getText(resId), duration);
		mHandler.postDelayed(r, duration);
		mToast.show();
	}

	public static void showToast(Context mContext, String text) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static void showToast(Context mContext, int resId) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(mContext.getResources().getText(resId));
		else
			mToast = Toast.makeText(mContext,
					mContext.getResources().getText(resId), Toast.LENGTH_SHORT);
		mToast.show();
	}

	public static void showLongToast(Context mContext, String text) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(text);
		else
			mToast = Toast.makeText(mContext, text, Toast.LENGTH_LONG);
		mToast.show();
	}

	public static void showLongToast(Context mContext, int resId) {
		mHandler.removeCallbacks(r);
		if (mToast != null)
			mToast.setText(mContext.getResources().getText(resId));
		else
			mToast = Toast.makeText(mContext,
					mContext.getResources().getText(resId), Toast.LENGTH_LONG);
		mToast.show();
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}
}
