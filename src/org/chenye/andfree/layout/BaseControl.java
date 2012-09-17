package org.chenye.andfree.layout;

import org.chenye.andfree.func.log;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class BaseControl extends RelativeLayout{
	Context context;
	public BaseControl(Context context){
		super(context);
		pre_init(context);
		init();
	}
	
	public BaseControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		pre_init(context);
		init();
	}
	
	public BaseControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		pre_init(context);
		init();
	}
	
	public int dp(int dp){
	    final float scale = getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
	
	public RelativeLayout get_item(int layout){
		final LayoutInflater inflater = LayoutInflater.from(context);
		try{
			RelativeLayout item = (RelativeLayout)inflater.inflate(layout, null);
			return item;
		}catch(Exception ex){
			e(ex);
			return null;
		}
	}
	
	public LinearLayout get_item_linear(int layout){
		final LayoutInflater inflater = LayoutInflater.from(context);
		LinearLayout item = (LinearLayout)inflater.inflate(layout, null);
		return item;
	}
	
	void pre_init(Context context){
		this.context = context;
	}
	
	protected void e(Exception ex){
		log.e(this, ex);
	}
	
	protected void i(Object obj){
		log.i(this, obj);
	}
	
	protected abstract void init();
}
