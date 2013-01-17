package org.chenye.andfree.animate;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

public class Shake {
	public static void shake(final View v, final int time){
		int duration = 50;
		int length = 10;
		final TranslateAnimation toRight = new TranslateAnimation(0, length, 0, 0);
		final TranslateAnimation toLeft = new TranslateAnimation(length, -length, 0, 0);
		final TranslateAnimation toCenter = new TranslateAnimation(-length, 0, 0, 0);
		toRight.setDuration(duration);
		toLeft.setDuration(duration * 2);
		toCenter.setDuration(duration);
		toRight.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			public void onAnimationEnd(Animation animation) {
				v.startAnimation(toLeft);
			}
		});
		
		toLeft.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				v.startAnimation(toCenter);
			}
		});
		
		toCenter.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation animation) {
				
			}
			
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				if (time == 0) return;
				shake(v, time - 1);
			}
		});
		v.startAnimation(toRight);
	}
}
