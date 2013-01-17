package org.chenye.andfree.widget;

import java.util.ArrayList;

import android.view.View;

public class WidgetAnimate {
	private static int FADEOUT = 0;
	private static int FADEIN = 1;
	
	ArrayList<Object[]> queue = new ArrayList<Object[]>();
	public WidgetAnimate fadeOut(int duration, IWidget<?, ?> v){
		addQueue(FADEOUT, duration, v);
		return this;
	}
	
	public WidgetAnimate fadeIn(int duration, IWidget<?, ?> v){
		addQueue(FADEIN, duration, v);
		return this;
	}
	
	private void addQueue(int type, int duration, IWidget<?, ?> v){
		queue.add(new Object[] {type, duration, v});
	}
	
	View.OnClickListener _click;
	public void start(View.OnClickListener click){
		_click = click;
		next();
	}
	
	public void next(){
		Object[] obj = queue.remove(0);
		int type = (Integer) obj[0];
		int duration = (Integer) obj[1];
		IWidget<?, ?> v = (IWidget<?, ?>) obj[2];
		View.OnClickListener next = new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if (queue.size() <= 0){
					if (_click == null) return;
					_click.onClick(v);
					return;
				}
				next();
			}
		};
		
		if (type == FADEOUT){
			v.fadeOut(duration, next);
		} else if (type == FADEIN){
			v.fadeIn(duration, next);
		}
	}
}
