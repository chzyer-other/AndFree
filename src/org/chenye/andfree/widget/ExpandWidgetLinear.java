package org.chenye.andfree.widget;

import java.lang.reflect.Field;

import org.chenye.andfree.obj.WidgetList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public abstract class ExpandWidgetLinear extends LinearLayout {

	public ExpandWidgetLinear(Context context) {
		super(context);
		init(context, null, -1);
	}

	public ExpandWidgetLinear(Context context, AttributeSet as){
		super(context, as);
		init(context, as, -1);
	}
	
	protected Context m;
	protected abstract void onInit(boolean inXML);
	private void init(Context context, AttributeSet attrs, int defStyle){
		m = context;
		onInit(isInEditMode());
	}

	/**
	 * width: dp
	 * height: dp
	 */
	@Override
	public void addView(View child, int width, int height) {
		super.addView(child, px(width), px(height));
	}
	
	public void addView(IWidget<?, ?> child, int width, int height) {
		addView(child.view(), width, height);
	}
	
	protected int px(int dp){
		return WidgetUtility.px(m, dp);
	}
	
	public void initAll(Context m, IWidget<?, ?> v, WidgetList w){
		v.inflateLayout(m);
		ViewGroup vg = v.viewgroup();
		Field[] fields = w.getClass().getFields();
		for (Field f:fields){
			try {
				IWidget<?, ?> obj = (IWidget<?, ?>) f.get(w);
				obj.inflate(vg);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
