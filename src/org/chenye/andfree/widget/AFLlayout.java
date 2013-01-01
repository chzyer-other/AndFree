package org.chenye.andfree.widget;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AFLlayout extends IWidget<AFLlayout, LinearLayout> implements IWidgetLayout<AFLlayout>{
	public AFLlayout(){
		super(LinearLayout.class);
	}
	public AFLlayout(LinearLayout l){
		super(l);
	}

	public AFLlayout(int id){
		super(id, LinearLayout.class);
	}
	
	public AFLlayout addView(View v) {
		_e.addView(v);
		return this;
	}
	
	public AFLlayout addViewEqualDiv(View v, int width, int height){
		return addView(v, new LinearLayout.LayoutParams(width, height, 1));
	}

	public AFLlayout addViewEqualDiv(View v) {
		LinearLayout.LayoutParams lp = getLinearParams(v);
		lp.weight = 1;
		v.setLayoutParams(lp);
		return addView(v);
	}
	public AFLlayout addViewEqualDiv(IWidget<?, ?> v) {
		return addViewEqualDiv(v.view());
	}
	
	public AFLlayout addView(View... vs){
		for (View v: vs){
			addView(v);
		}
		return this;
	}
	
	public AFLlayout addView(View v, ViewGroup.LayoutParams lp){
		_e.addView(v, lp);
		return this;
	}
	
	public AFLlayout addView(View child, int width, int height){
		_e.addView(child, px(width), px(height));
		return this;
	}
	
	public AFLlayout addView(IWidget<?, ?> v, int width, int height){
		return addView(v.view(), width, height);
	}

	public AFLlayout addView(IWidget<?, ?> v) {
		addView(v.view());
		return this;
	}

	public AFLlayout removeAllViews() {
		_e.removeAllViews();
		return this;
	}

	public AFLlayout setMargins(int all){
		return setMargins(all, all, all, all);
	}
	public AFLlayout setMargins(int left, int top, int right, int bottom) {
		ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) _e.getLayoutParams();
		if (lp == null){
			try{
				setLinearLayoutParams(-1, -1, 1);
			} catch (Exception ex){
				setRelativeLayoutParams(-1, -1);
			}
			return setMargins(left, top, right, bottom);
		} else if (lp instanceof LinearLayout.LayoutParams){
			((LinearLayout.LayoutParams)lp).setMargins(px(left), px(top), px(right), px(bottom));
		} else if (lp instanceof RelativeLayout.LayoutParams){
			((RelativeLayout.LayoutParams)lp).setMargins(px(left), px(top), px(right), px(bottom));
		}
		
		return this;
	}
	
	public AFLlayout setPadding(int left, int top, int right, int bottom){
		_e.setPadding(px(left), px(top), px(right), px(bottom));
		return this;
	}
	

	
	@Override
	protected void inflate(View v) {
		super.inflate(v);
	}

	public AFLlayout setVertical(){
		_e.setOrientation(LinearLayout.VERTICAL);
		return this;
	}
	
	public AFLlayout setHorizontal(){
		_e.setOrientation(LinearLayout.HORIZONTAL);
		return this;
	}
	public AFLlayout removeLastest() {
		return removeViewAt(_e.getChildCount() - 1);
	}
	public AFLlayout removeViewAt(int index) {
		_e.removeViewAt(index);
		return this;
	}
	public AFLlayout copy() {
		LinearLayout l = newChildInstance();
		return new AFLlayout(l);
	}

	public AFLlayout setWeight(int i){
		_e.setWeightSum(i);
		return this;
	}
	public AFLlayout setLayoutTo(Dialog d, int width, int height) {
		d.setContentView(obj(), new ViewGroup.LayoutParams(px(width), px(height)));
		return this;
	}
	public int getChildCount() {
		return _e.getChildCount();
	}
}
