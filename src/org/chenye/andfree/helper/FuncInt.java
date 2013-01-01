package org.chenye.andfree.helper;

import java.util.Random;

import android.content.Context;

public class FuncInt {
	public static int getPix(Context m, int dp){
		final float scale = m.getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}

	public static int getRandom(int length) {
		Random random = new Random();  
		return random.nextInt(length);
	}
}
