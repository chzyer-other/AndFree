package org.chenye.andfree.widget;

import android.app.Dialog;
import android.view.View;
import android.view.ViewGroup;

public interface IWidgetLayout<T> {
	public T addView(View v);
	public T addView(IWidget<?, ?> v);
	public T addView(View v, ViewGroup.LayoutParams lp);
	public T addViewEqualDiv(View v, int width, int height);
	public T addViewEqualDiv(View v);
	public T addViewEqualDiv(IWidget<?, ?> v);
	public T addTopView(IWidget<?, ?> v);
	public T addTopView(View v);
	public T addBottomView(IWidget<?, ?> v);
	public T addBottomView(View v);
	
	/**
	 * remove all views
	 * @return
	 */
	public T removeAllViews();
	public T removeView(View v);
	public T removeView(IWidget<?, ?> v);
	public T setMargins(int left, int top, int right, int bottom);
	
	/**
	 * remove the last view
	 * @return
	 */
	public T removeLast();
	public T removeViewAt(int index);
	
	public T setLayoutTo(Dialog d, int width, int height);
	public int getChildCount();
}
