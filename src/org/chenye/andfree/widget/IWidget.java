package org.chenye.andfree.widget;

import in.hitme.android._animate.Fade;
import in.hitme.android._animate.Shake;

import java.lang.reflect.Constructor;
import java.util.Hashtable;

import org.chenye.andfree.conf.S;
import org.chenye.andfree.obj.BaseLog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


public abstract class IWidget<T, E> extends BaseLog implements ICopy<T> {
	public static long[] layoutRange = new long[2];
	public static boolean inLayoutRandge(int id){
		return id >= layoutRange[0] && id <= layoutRange[1];
	}
	
	public IWidget(Class<E> cls){
		onInit(cls);
	}
	
	public IWidget(E e) {
		onInit(e.getClass());
		_m = ((View) e).getContext();
		_e = e;
		v = (View) e;
	}
	
	public IWidget(int id, Class<E> cls){
		onInit(cls);
		_id = id;
	}
	
	Hashtable<String, Object> data = new Hashtable<String, Object>();
	E _e;
	Class<E> _cls;
	Context _m;
	int _id = 0;
	View v;
	T self;
	
	public void inflate(ViewGroup vg){
		if (isInflated()) return;
		_m = vg.getContext();
		if (isNoId()){
			return;
		}
		inflate(vg.findViewById(_id));
	}
	
	/**
	 * may get Parent(ViewGroup) from this
	 * <br> use setTags(S.parent)
	 * @param vg
	 * @return
	 */
	public T setParent(ViewGroup vg){
		setTags(S.parent, vg);
		return self;
	}
	
	/**
	 * get the parent from getTags(S.parent)
	 * @return
	 */
	public ViewGroup getParent(){
		return (ViewGroup) getTag(S.parent);
	}
	
	/**
	 * getTag and parse to Integer
	 * @param id
	 * @return
	 */
	public int getIntTag(int id){
		return (Integer) v.getTag(id);
	}
	
	/**
	 * getTag and parse to Bool
	 * @param id
	 * @return if tag not instance of Boolean, it will return False when "" or 0; 
	 */
	public boolean getBoolTag(int id){
		Object obj = v.getTag(id);
		if (obj == null){
			setTags(id, false);
			return false;
		}
		if (obj instanceof Boolean){
			return (Boolean) obj;
		}
		String str = obj.toString();
		return str.equals("0") || str.length() == 0;
	}
	
	/**
	 * getTag
	 * @param id
	 * @return
	 */
	public Object getTag(int id){
		return v.getTag(id);
	}
	
	/**
	 * get LayoutParams and parse to LinearLayout.LayoutParams
	 * <br> if got null, will return <b>a new LinearLayout.LayoutParams</b>
	 * <br> if the LayoutParams got not equals LinearLayout.LayoutParams, will return <b>null</b>
	 * @param v
	 * @return LinearLayout.LayoutParams
	 */
	protected LinearLayout.LayoutParams getLinearParams(View v){
		try{
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) v.getLayoutParams();
		if (lp == null){
			lp = new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT);			
		}
		return lp;
		} catch (Exception ex){
			error(ex);
			return null;
		}
	}
	
	public void inflate(Activity a){
		if (isInflated()) return;
		_m = a.getApplicationContext();
		if (isNoId()){
			return;
		}
		
		if (inLayoutRandge(_id)){
			LayoutInflater li = LayoutInflater.from(_m);
			inflate(li.inflate(_id, null));
			return;
		}
		
		inflate(a.findViewById(_id));
	}
	
	public void inflate(Dialog d){
		if (isInflated()) return;
		_m = d.getContext();
		if (isNoId()){
			return;
		}
		
		if (inLayoutRandge(_id)){
			LayoutInflater li = LayoutInflater.from(_m);
			inflate(li.inflate(_id, null));
			return;
		}
		
		inflate(d.findViewById(_id));
	}
	
	public void inflateLayout(Context m){
		if (isInflated()) return;
		_m = m;
		
		if (isNoId()){
			return;
		}
		
		LayoutInflater li = LayoutInflater.from(m);
		inflate(li.inflate(_id, null));
	}
	
	public void inflate(Context m){
		if (isInflated()) return;
		_m = m;
		
		if (isNoId()){
			return;
		}
		
		if (inLayoutRandge(_id)){
			LayoutInflater li = LayoutInflater.from(m);
			inflate(li.inflate(_id, null));
			return;
		}
		
		inflate(v);
	}
	
	public boolean equalView(View v){
		return this.v.equals(v);
	}
	
	public boolean isNoId(){
		if (_id != 0) return false;
		inflate((View) newChildInstance());
		return true;
	}
	
	protected void inflate(View view){
		_e = v2e(view);
		v = view;
	}
	
	/**
	 * set the Width
	 * @param width
	 * @return
	 */
	public T setWidth(int width){
		return setSize(width, -5);
	}
	
	/**
	 * set Height
	 * @param height
	 * @return
	 */
	public T setHeight(int height){
		return setSize(-5, height);
	}
	
	/**
	 * set the width and height for the widget
	 * @param width
	 * @param height
	 * @return
	 */
	public T setSize(int width, int height){
		LayoutParams lp = (LayoutParams) getLayoutParams();
		if (width != -5){
			lp.width = px(width);
		}
		if (height != -5){
			lp.height = px(height);
		}
		setLayoutParams(lp);
		return self;
	}
	
	/**
	 * set padding for all(dip)
	 * @param all
	 * @return
	 */
	public T setPadding(int all){
		return setPadding(all, all, all, all);
	}
	
	/**
	 * set padding(dip)
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	public T setPadding(int left, int top, int right, int bottom){
		v.setPadding(px(left), px(top), px(right), px(bottom));
		return self;
	}
	
	/**
	 * set left and right padding(dip)
	 * @param left
	 * @param right
	 * @return
	 */
	public T setPaddingHorizontal(int left, int right){
		return setPadding(left, dp(v.getPaddingTop()), right, dp(v.getPaddingBottom()));
	}

	public T init(Object... objs){
		for (Object obj: objs){
			if (obj instanceof View.OnClickListener){
				setClick((OnClickListener) obj);
			}
		}
		return self;
	}
	
	/**
	 * get the height of view
	 * @return
	 */
	public int getHeight(){
		return v.getHeight();
	}
	
	/**
	 * set Tags(id, object, id, object, ...)
	 * @param objs
	 * @return
	 */
	public T setTags(Object... objs){
		if (v == null) return self;
		for (int i=0; i<objs.length; i+=2){
			int key = Integer.valueOf(objs[i].toString());
			Object value = objs[i+1];
			v.setTag(key, value);
		}
		return self;
	}
	
	/**
	 * get ViewGroup.LayoutParams
	 * @return
	 */
	public ViewGroup.LayoutParams getLayoutParams(){
		return v.getLayoutParams();
	}
	
	/**
	 * set backgroup with resource id
	 * @param res
	 * @return
	 */
	public T setBackgroundResource(int res){
		v.setBackgroundResource(res);
		return self;
	}
	
	/**
	 * set LayoutParams(width, height)
	 * @param width
	 * @param height
	 * @return
	 */
	public T setLayoutParams(int width, int height){
		return setLayoutParams(new ViewGroup.LayoutParams(px(width), px(height)));
	}
	
	/**
	 * set ViewGroup.LayoutParams
	 * @param params
	 * @return
	 */
	public T setLayoutParams(ViewGroup.LayoutParams params){
		v.setLayoutParams(params);
		return self;
	}
	
	/**
	 * set background transparent color
	 * @return
	 */
	public T setBackgroundTransparent(){
		return setBackgroundColor("");
	}
	
	/**
	 * set background with color (#xxxxxx)
	 * @param color
	 * @return
	 */
	public T setBackgroundColor(String color){
		int c;
		if (color.length() != 0){
			c = Color.parseColor(color);
		} else {
			c = Color.TRANSPARENT;
		}
		return setBackgroundColor(c);
	}
	
	/**
	 * set background color (Color int)
	 * @param color
	 * @return
	 */
	public T setBackgroundColor(int color){
		v.setBackgroundColor(color);
		return self;
	}
	
	public T startAnimation(Animation animation){
		v.startAnimation(animation);
		return self;
	}
	
	/**
	 * Gets the background drawable
	 * @return
	 */
	public Drawable getBackground(){
		return v.getBackground();
	}
	
	/**
	 * helper for setVisibility(View.Gone)
	 * @return this;
	 */
	public T hide(){
		v.setVisibility(View.GONE);
		return self;
	}
	
	/**
	 * helper for setVisibility(View.VISIBLE)
	 * @return this;
	 */
	public T show(){
		v.setVisibility(View.VISIBLE);
		return self;
	}
	
	/**
	 * Initialize new child instance.
	 * @return 
	 * E; viewfsadfasdf
	 * null if failure;
	 *
	 */
	protected E newChildInstance(){
		try {
			Constructor<E> i = _cls.getConstructor(Context.class);
			return i.newInstance(_m);
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * Whether IWidget inflated
	 * @return boolean
	 */
	public boolean isInflated(){
		
		return _e != null && v != null;
	}
	
	/**
	 * Helper for shark animation.
	 * @return this
	 */
	public T shake(){
		startAnimation(Shake.shake(_m));
		return self;
	}
	
	/**
	 * Helper for FadeIn Animation.
	 * Auto show object after the animation finished
	 * @param duration the time plan to run
	 * @param next [View.OnClickListener] callback function
	 * @return this
	 */
	public T fadeIn(int duration, final View.OnClickListener next){
		show();
		startAnimation(Fade.fadeIn(duration, next));
		return self;
	}
	
	/**
	 * Helper for FadeOut Animation.
	 * Auto hide object after the animation finished
	 * @param duration the time plan to run
	 * @param next [View.OnClickListener] callback function
	 * @return this
	 */
	public T fadeOut(int duration, final View.OnClickListener next){
		startAnimation(Fade.fadeOut(duration, new View.OnClickListener() {
			
			public void onClick(View v) {
				hide();
				next.onClick(null);
			}
		}));
		return self;
	}
	
	/**
	 * force convert obj to viewGroup
	 * @return ViewGroup
	 */
	public ViewGroup viewgroup(){
		return (ViewGroup) v;
	}
	
	/**
	 * force convert obj to view
	 * @return View
	 */
	public View view(){
		return (View) v;
	}
	
	/**
	 * [protected] convert pix to dip
	 * @param dip
	 * @return pix
	 */
	protected int px(int dp){
		return WidgetUtility.px(_m, dp);
	}
	
	protected int dp(int px){
		return WidgetUtility.dip(_m, px);
	}
	
	/**
	 * got the original view
	 * @return the original view
	 */
	public E obj(){
		return (E) _e;
	}
	
	/**
	 * Equals to ViewGroup.addView(IWidget)
	 * @param vg
	 * @return
	 */
	public T addTo(IWidgetLayout<?> vg){
		vg.addView(this);
		return self;
	}
	
	/**
	 * Equals to ViewGroup.addView(View)
	 * @param vg
	 * @return
	 */
	public T addTo(ViewGroup vg){
		vg.addView(v);
		return self;
	}
	
	/**
	 * Equals to ViewGroup.addView(View, px(width), px(height))
	 * @param vg
	 * @param width
	 * @param height
	 * @return
	 */
	public T addTo(ViewGroup vg, int width, int height){
		if ( ! (vg instanceof ExpandWidget) && ! (vg instanceof ExpandWidgetScroll)){
			width = px(width);
			height = px(height);
		}
		vg.addView(v, width, height);
		return self;
	}
	
	/**
	 * set on click listener
	 * @param click
	 * @return this
	 */
	public T setClick(View.OnClickListener click){
		if (click == null) return self;
		data.put("click", click);
		v.setOnClickListener(click);
		return self;
	}
	
	/**
	 * simulate click event
	 * @return
	 */
	public T click(){
		if (data.containsKey("click")){
			((View.OnClickListener) data.get("click")).onClick(v);
		}
		return self;
	}
	
	/**
	 * View.findViewById
	 * @param resid
	 * @return View
	 */
	public View findView(int resid){
		return v.findViewById(resid);
	}
	
	@SuppressWarnings("unchecked")
	protected E v2e(View v){return (E) v;}
	@SuppressWarnings("unchecked")
	private void onInit(Class<?> cls){
		self = (T) this;
		_cls = (Class<E>) cls;
	}
	
	public T setLinearLayoutParams(int width, int height, int weight){
		v.setLayoutParams(new LinearLayout.LayoutParams(px(width), px(height), weight));
		return self;
	}
	
	public T setRelativeLayoutParams(int width, int height){
		v.setLayoutParams(new RelativeLayout.LayoutParams(px(width), px(height)));
		return self;
	}
}
