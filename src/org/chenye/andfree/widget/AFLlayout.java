package org.chenye.andfree.widget;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class AFLLayout extends IWidget<AFLLayout, LinearLayout> implements IWidgetLayout<AFLLayout>{
	public AFLLayout(){
		super(LinearLayout.class);
	}
	public AFLLayout(LinearLayout l){
		super(l);
	}

	public AFLLayout(int id){
		super(id, LinearLayout.class);
	}
	
	public AFLLayout addView(View v) {
		_e.addView(v);
		return this;
	}
	
	public AFLLayout addViewEqualDiv(View v, int width, int height){
		return addView(v, new LinearLayout.LayoutParams(width, height, 1));
	}

	public AFLLayout addViewEqualDiv(View v) {
		LinearLayout.LayoutParams lp = getLinearParams(v);
		lp.weight = 1;
		v.setLayoutParams(lp);
		return addView(v);
	}
	public AFLLayout addViewEqualDiv(IWidget<?, ?> v) {
		return addViewEqualDiv(v.view());
	}
	
	public AFLLayout addView(View... vs){
		for (View v: vs){
			addView(v);
		}
		return this;
	}
	
	public AFLLayout addView(View v, ViewGroup.LayoutParams lp){
		_e.addView(v, lp);
		return this;
	}
	
	public AFLLayout addView(View child, int width, int height){
		_e.addView(child, px(width), px(height));
		return this;
	}
	
	public AFLLayout addView(IWidget<?, ?> v, int width, int height){
		return addView(v.view(), width, height);
	}

	public AFLLayout addView(IWidget<?, ?> v) {
		addView(v.view());
		return this;
	}

	public AFLLayout removeAllViews() {
		_e.removeAllViews();
		return this;
	}

	public AFLLayout setMargins(int all){
		return setMargins(all, all, all, all);
	}
	
	public AFLLayout setMargins(int left, int top, int right, int bottom) {
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
	
	public AFLLayout setPadding(int left, int top, int right, int bottom){
		_e.setPadding(px(left), px(top), px(right), px(bottom));
		return this;
	}
	
	@Override
	protected void inflate(View v) {
		super.inflate(v);
	}

	public AFLLayout setVertical(){
		_e.setOrientation(LinearLayout.VERTICAL);
		return this;
	}
	
	public AFLLayout setHorizontal(){
		_e.setOrientation(LinearLayout.HORIZONTAL);
		return this;
	}
	
	public AFLLayout removeLast() {
		return removeViewAt(_e.getChildCount() - 1);
	}
	
	public AFLLayout removeViewAt(int index) {
		_e.removeViewAt(index);
		return this;
	}

	public AFLLayout setWeight(int i){
		_e.setWeightSum(i);
		return this;
	}
	
	public AFLLayout setLayoutTo(Dialog d, int width, int height) {
		d.setContentView(obj(), new ViewGroup.LayoutParams(px(width), px(height)));
		return this;
	}
	
	public int getChildCount() {
		return _e.getChildCount();
	}
	
	public AFLLayout addTopView(IWidget<?, ?> v) {
		return addTopView(v.view());
	}
	
	public AFLLayout addTopView(View v) {
		_e.addView(v, 0);
		return this;
	}
	
	public AFLLayout addBottomView(IWidget<?, ?> v) {
		return addBottomView(v.view());
	}
	public AFLLayout addBottomView(View v) {
		_e.addView(v, getChildCount());
		return this;
	}
	
	public AFLLayout removeView(IWidget<?, ?> v) {
		return removeView(v.view());
	}
	
	public AFLLayout removeView(View v) {
		_e.removeView(v);
		return this;
	}
}
