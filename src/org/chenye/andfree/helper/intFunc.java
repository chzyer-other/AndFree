package org.chenye.andfree.helper;

import android.content.Context;

public class intFunc {
	public static int getPix(Context m, int dp){
		final float scale = m.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
}
