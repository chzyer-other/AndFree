package org.chenye.andfree.widget;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AFRLayout extends IWidget<AFRLayout, RelativeLayout> implements IWidgetLayout<AFRLayout>{

	public AFRLayout(RelativeLayout e){
		super(e);
	}

	public AFRLayout(int id){
		super(id, RelativeLayout.class);
	}
	
	public AFRLayout init(Object... objs) {
		super.init(objs);
		return this;
	}

	public AFRLayout addViewEqualDiv(View v) {
		LinearLayout.LayoutParams lp = getLinearParams(v);
		lp.weight = 1;
		v.setLayoutParams(lp);
		return addViewEqualDiv(v);
	}
	
	public AFRLayout addViewEqualDiv(IWidget<?, ?> v) {
		return addViewEqualDiv(v.view());
	}
	
	public AFRLayout addView(View v) {
		_e.addView(v);
		return this;
	}

	public AFRLayout addView(IWidget<?, ?> v) {
		addView(v.view());
		return this;
	}

	public AFRLayout removeAllViews() {
		_e.removeAllViews();
		return this;
	}

	public AFRLayout setMargins(int left, int top, int right, int bottom) {
		LayoutParams lp = (LayoutParams) _e.getLayoutParams();
		lp.setMargins(left, top, right, bottom);
		return this;
	}

	public AFRLayout removeLast() {
		return removeViewAt(_e.getChildCount() - 1);
	}
	
	public AFRLayout removeViewAt(int index) {
		_e.removeViewAt(index);
		return this;
	}

	public AFRLayout setLayoutTo(Dialog d, int width, int height) {
		d.setContentView(obj(), new LayoutParams(px(width), px(height)));
		return this;
	}

	public int getChildCount() {
		return _e.getChildCount();
	}

	public AFRLayout addViewEqualDiv(View v, int width, int height){
		return addView(v, new LinearLayout.LayoutParams(width, height, 1));
	}
	
	public AFRLayout addView(View v, ViewGroup.LayoutParams lp){
		_e.addView(v, lp);
		return this;
	}
	
	public AFRLayout addView(View v, int width, int height){
		_e.addView(v, px(width), px(height));
		return this;
	}

	public AFRLayout addTopView(IWidget<?, ?> v) {
		return addTopView(v.view());
	}
	
	public AFRLayout addTopView(View v) {
		_e.addView(v, 0);
		return this;
	}

	public AFRLayout addBottomView(IWidget<?, ?> v) {
		return addBottomView(v.view());
	}

	public AFRLayout addBottomView(View v) {
		_e.addView(v, getChildCount());
		return this;
	}

	public AFRLayout removeView(View v) {
		_e.removeView(v);
		return this;
	}

	public AFRLayout removeView(IWidget<?, ?> v) {
		return removeView(v.view());
	}
}
