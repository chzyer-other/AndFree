package org.chenye.andfree.widget;

import android.content.Context;
import android.view.View;

public class WidgetUtility {
	public static void SetTags(View v, Object... objs){
		
	}
	
	public static View SetBackgroundColor(View v, String color){
		return v;
	}
	
	public static void Show(View v){
		
	}
	
	public static View SetClick(View v, View.OnClickListener click){
		v.setOnClickListener(click);
		return v;
	}
	
	/**
	 * 将dp转成px
	 * @param m Context
	 * @param dp 如果是负的则返回原值
	 * @return
	 */
	public static int px(Context m, int dp){
		if (dp <= 0){
			return dp;
		}
	    final float scale = m.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}

	public static int dip(Context m, int dipValue) {
		float scale = m.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}
}
