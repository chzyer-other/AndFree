package org.chenye.andfree.obj;

import org.chenye.andfree.func.log;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public abstract class BaseControl extends RelativeLayout{
	protected Context _m;
	public BaseControl(Context context){
		super(context);
		pre_init(context);
	}
	
	public BaseControl(Context context, AttributeSet attrs) {
		super(context, attrs);
		pre_init(context);
	}
	
	public BaseControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		pre_init(context);
	}
	
	public int dp(int dp){
	    final float scale = getResources().getDisplayMetrics().density;
	    return (int) (dp * scale + 0.5f);
	}
	
	public RelativeLayout get_item(int layout){
		final LayoutInflater inflater = LayoutInflater.from(_m);
		try{
			RelativeLayout item = (RelativeLayout)inflater.inflate(layout, null);
			return item;
		}catch(Exception ex){
			e(ex);
			return null;
		}
	}
	
	public LinearLayout get_item_linear(int layout){
		final LayoutInflater inflater = LayoutInflater.from(_m);
		LinearLayout item = (LinearLayout)inflater.inflate(layout, null);
		return item;
	}
	
	void pre_init(Context context){
		_m = context;
	}
	
	protected void e(Exception ex){
		log.e(this, ex);
	}
	
	protected void i(Object obj){
		log.i(this, obj);
	}
	
	ViewGroup _layout;
	public ViewGroup init(int resid, int weight, int height){
		LayoutInflater inflater = LayoutInflater.from(_m);
		try{
		_layout = (ViewGroup) inflater.inflate(resid, null);
		}catch (Exception ex){
			log.e(this, ex);
		}
		addView(_layout, weight, height);
		return _layout;
	}
	
	public ViewGroup getLayout(){
		return _layout;
	}
}
