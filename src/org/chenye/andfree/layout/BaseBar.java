package org.chenye.andfree.layout;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;

public class BaseBar extends BaseControl{
	protected BaseMainActivity m;
	public BaseBar(Context context) {
		super(context);
		m = (BaseMainActivity) context;
		// TODO Auto-generated constructor stub
	}
	
	public BaseBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		m = (BaseMainActivity) context;
	}
	
	public BaseBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m = (BaseMainActivity) context;
	}
	
	@Override
	protected void init() {
		// TODO Auto-generated method stub
		m = (BaseMainActivity) context;
	}

	public void feight(BaseMainItem... items){}
	
	protected View.OnClickListener click_before_click = null;
	public void setBeforeClick(View.OnClickListener before_click){
		click_before_click = before_click;
	}
	
	protected DialogInterface.OnClickListener click_after_click = null;
	public void setAfterClick(DialogInterface.OnClickListener click){
		click_after_click = click;
	}
}
