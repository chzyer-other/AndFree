package org.chenye.andfree.widget;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AFRlayout extends IWidget<AFRlayout, RelativeLayout> implements IWidgetLayout<AFRlayout>{

	public AFRlayout(RelativeLayout e){
		super(e);
	}

	public AFRlayout(int id){
		super(id, RelativeLayout.class);
	}
	
	public AFRlayout init(Object... objs) {
		super.init(objs);
		return this;
	}

	public AFRlayout addViewEqualDiv(View v) {
		LinearLayout.LayoutParams lp = getLinearParams(v);
		lp.weight = 1;
		v.setLayoutParams(lp);
		return addViewEqualDiv(v);
	}
	
	public AFRlayout addViewEqualDiv(IWidget<?, ?> v) {
		return addViewEqualDiv(v.view());
	}
	
	public AFRlayout addView(View v) {
		_e.addView(v);
		return this;
	}

	public AFRlayout addView(IWidget<?, ?> v) {
		addView(v.view());
		return this;
	}

	public AFRlayout removeAllViews() {
		_e.removeAllViews();
		return this;
	}

	public AFRlayout setMargins(int left, int top, int right, int bottom) {
		LayoutParams lp = (LayoutParams) _e.getLayoutParams();
		lp.setMargins(left, top, right, bottom);
		return this;
	}

	public AFRlayout removeLastest() {
		return removeViewAt(_e.getChildCount() - 1);
	}
	public AFRlayout removeViewAt(int index) {
		_e.removeViewAt(index);
		return this;
	}

	public AFRlayout copy() {
		return new AFRlayout(newChildInstance());
	}

	public AFRlayout setLayoutTo(Dialog d, int width, int height) {
		d.setContentView(obj(), new LayoutParams(px(width), px(height)));
		return this;
	}

	public int getChildCount() {
		return _e.getChildCount();
	}

	public AFRlayout addViewEqualDiv(View v, int width, int height){
		return addView(v, new LinearLayout.LayoutParams(width, height, 1));
	}
	
	public AFRlayout addView(View v, ViewGroup.LayoutParams lp){
		_e.addView(v, lp);
		return this;
	}
	
	public AFRlayout addView(View v, int width, int height){
		_e.addView(v, px(width), px(height));
		return this;
	}

}
