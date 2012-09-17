package org.chenye.andfree.func;

import org.chenye.andfree.R;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

public class UIFunc {
	public static void setHover(final View view){
		View.OnTouchListener listen_hover = new View.OnTouchListener() {

			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
					v.setBackgroundDrawable((Drawable)v.getTag(R.id.drawable));
				}else{
					String type = ClsFunc.getClassName(v.getTag(R.id.hover));
					if (type.equals("String")){
						v.setBackgroundColor(Color.parseColor(v.getTag(R.id.hover) + ""));
					} else {
						int hover_id = (Integer) v.getTag(R.id.hover);
						v.setBackgroundResource(hover_id);
					}
					
					
				}
				return false;
			}
		};
		
		String type = ClsFunc.getClassName(view.getTag(R.id.hover));
		view.setTag(R.id.drawable, type.equals("String") ? null : view.getBackground());
		view.setOnTouchListener(listen_hover);
		
		View.OnClickListener event = (View.OnClickListener) view.getTag(R.id.onclick);
		if (event != null){
			view.setOnClickListener(event);
		}
		
	}

}
