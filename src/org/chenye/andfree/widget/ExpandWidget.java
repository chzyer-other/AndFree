package org.chenye.andfree.widget;

import java.lang.reflect.Field;

import org.chenye.andfree.obj.AFLog;
import org.chenye.andfree.obj.WidgetList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public abstract class ExpandWidget extends RelativeLayout {
	public final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
	public final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
	
	public ExpandWidget(Context context) {
		super(context);
		init(context, null, -1);
	}

	public ExpandWidget(Context context, AttributeSet as){
		super(context, as);
		init(context, as, -1);
	}
	
	public ExpandWidget(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	protected Context m;
	protected abstract void onInit(boolean inXML);
	private void init(Context context, AttributeSet attrs, int defStyle){
		try {
			m = context;
			onInit(isInEditMode());
		} catch (Exception ex){
			error(ex);
		}
	}

	/**
	 * PLEASE USE dip INSTEAD OF pix
	 * <p>set the size of this widget</p>
	 * @param width 
	 * @param height
	 */
	public void setSize(int width, int height){
		ViewGroup.LayoutParams rp = (ViewGroup.LayoutParams) getLayoutParams();
		width = px(width);
		height = px(height);
		if (rp == null){
			rp = new ViewGroup.LayoutParams(width, height);
		}
		setLayoutParams(rp);
	}
	
	/**
	 * Equals to addView(child, px(width), px(height))
	 * width: dp
	 * height: dp
	 */
	@Override
	public void addView(View child, int width, int height) {
		super.addView(child, px(width), px(height));
	}
	
	/**
	 * Equals to (View, px(width), px(height))
	 * @param child
	 * @param width
	 * @param height
	 */
	public void addView(IWidget<?, ?> child, int width, int height) {
		addView(child.view(), width, height);
	}
	
	protected int px(int dp){
		return WidgetUtility.px(m, dp);
	}
	
	public void initAll(IWidget<?, ?> v, WidgetList w){
		Context m = getContext();
		v.inflateLayout(m);
		ViewGroup vg = v.viewgroup();
		Field[] fields = w.getClass().getFields();
		for (Field f:fields){
			try {
				IWidget<?, ?> obj = (IWidget<?, ?>) f.get(w);
				obj.inflate(vg);
			} catch (IllegalArgumentException e) {
				error(e);
			} catch (IllegalAccessException e) {
				error(e);
			}
		}
	}
	
	public void removeSelfFromParent(){
		ViewGroup vg = null;
		try{
			vg = (ViewGroup) getParent();
		} catch (Exception ex) {
			return;
		}
		if (vg == null) return;
		vg.removeView(this);
	}
	
	public void log(Object... obj){
		AFLog.i(this, obj);
	}
	
	public void error(Object obj) {
		AFLog.e(this, obj);
	}
	
	public void toast(String str) {
		AFLog.toast(m, str);
	}
}
