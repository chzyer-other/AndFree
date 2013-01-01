package org.chenye.andfree.func;

import org.chenye.andfree.conf.S;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class FuncUI {
	public static void setHover(final View view){
		View.OnTouchListener listen_hover = new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
					v.setBackgroundDrawable((Drawable)v.getTag(S.drawable));
				}else{
					if (v.getTag(S.hover) instanceof String){
						v.setBackgroundColor(Color.parseColor(v.getTag(S.hover) + ""));
					} else {
						int hover_id = (Integer) v.getTag(S.hover);
						v.setBackgroundResource(hover_id);
					}
					
					
				}
				return false;
			}
		};
		
		Object obj = null;
		if ( ! (view.getTag(S.hover) instanceof String)){
			obj = view.getBackground();
		}
		view.setTag(S.drawable, obj);
		view.setOnTouchListener(listen_hover);
		
		View.OnClickListener event = (View.OnClickListener) view.getTag(S.onclick);
		if (event != null){
			view.setOnClickListener(event);
		}
		
	}

	public static void hideKeybroad(Activity m){
		((InputMethodManager) m.getSystemService(Context.INPUT_METHOD_SERVICE))
		.hideSoftInputFromWindow(m.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   
	}
}
