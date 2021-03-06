package org.chenye.andfree.widget;

import org.chenye.andfree.func.FuncTime;
import org.chenye.andfree.obj.AFLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public abstract class ExpandWidgetScroll extends ScrollView {
	public static final int MATCH_PARENT = ExpandWidget.MATCH_PARENT;
	public static final int WRAP_CONTENT = ExpandWidget.WRAP_CONTENT;

	public ExpandWidgetScroll(Context context) {
		super(context);
		init(context, null, -1);
	}

	public ExpandWidgetScroll(Context context, AttributeSet as){
		super(context, as);
		init(context, as, -1);
	}
	
	public ExpandWidgetScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}
	
	protected Context m;
	protected abstract void onInit(boolean inXML);
	private void init(Context context, AttributeSet attrs, int defStyle){
		m = context;
		if (getChildCount() == 0){
			LinearLayout l = new LinearLayout(m);
			l.setOrientation(LinearLayout.VERTICAL);
			super.addView(l, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}
		onInit(isInEditMode());
	}

	public ViewGroup getViewGroup(){
		return (ViewGroup) getChildAt(0);
	}
	/**
	 * addView to LinearLayout(Vertical)
	 * width: dp
	 * height: dp
	 */
	@Override
	public void addView(View child, int width, int height) {
		getViewGroup().addView(child, px(width), px(height));
	}
	
	public void addView(IWidget<?, ?> child, int width, int height) {
		addView(child.view(), width, height);
	}
	
	public void addViewWithPix(View child, int width, int height){
		getViewGroup().addView(child, width, height);
	}
	
	public void addViewWithPix(IWidget<?, ?> child, int width, int height){
		addViewWithPix(child.view(), width, height);
	}
	
	public void addView(View child){
		getViewGroup().addView(child);
	}

	public void addView(View child, int index){
		try {
			int count = getViewGroup().getChildCount();
			getViewGroup().addView(child, index);
		} catch (Exception ex) {
			error(ex);
		}
	}

	public void error(Exception ex) {
		AFLog.e(this, ex);
	}

	public void addView(IWidget<?, ?> child){
		addView(child.view());
	}
	
	protected int px(int dp){
		return WidgetUtility.px(m, dp);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (_edt != null){
			if (h > oldh){
				_edt.onLostFocus();
			}
			if (h < oldh){
				_edt.onFocus();
			}
		}
		super.onSizeChanged(w, h, oldw, oldh);
		AFLog.i(this, "onSizeChanged", w, h, oldw, oldh);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
        int specSize = MeasureSpec.getSize(MeasureSpec.getMode(heightMeasureSpec));
//        AFLog.i(this, MeasureSpec.getSize(heightMeasureSpec));

		AFLog.i(this, "onMeasure", specMode, specSize, widthMeasureSpec, heightMeasureSpec);

	};

	protected int[] ddd = new int[2];
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (isIgnore) {
			isIgnore = false;
			return;
		}
		super.onLayout(changed, l, t, r, b);
		ddd[0] = r;
		ddd[1] = b;
		AFLog.i(this, "onLayout", changed, l, t, r, b);
	};
	
	AFEdit _edt;
	public void setAFEditFocusHandler(AFEdit et) {
		_edt = et;
	}

	public void log(Object obj) {
		AFLog.i(this, obj);
	}

	public void removeAllViews(){
		getViewGroup().removeAllViews();
	}

	public void runOnNewThread(Runnable run){
		new Thread(run).start();
	}

	boolean isIgnore = false;
	public void  ignoreMeasure() {
		isIgnore = true;
	}
}
