package org.chenye.andfree.widget;

import java.util.Hashtable;

import org.chenye.andfree.db.baseConfig.configField;
import org.chenye.andfree.func.ClsFunc;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.func.log;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.layout.BaseMainItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public abstract class BaseItem {
	protected Context m;
	protected BaseItem baseitem = this;
	protected ViewGroup item;
	protected Hashtable<String, Object> data = new Hashtable<String, Object>();
	
	public BaseItem(Context mContext, Object... objs){
		m = mContext;
		init(objs);
		setLayout(getLayoutId());
	}
	
	public interface ItemClick {
		public void click(BaseItem item, View v);
	}
	
	protected abstract int getLayoutId();
	
	public BaseItem setContent(String field){
		return this;
	}
	
	protected GroupCategory category;
	public void setCategory(GroupCategory cate){
		category = cate;
	}
	
	protected TextView line;
	public void setLine(TextView txt){
		line = txt;
	}
	
	Object[] pass_objs;
	protected void init(Object... objs){
		pass_objs = objs;
		for (int i=0; i<objs.length; i++){
			ClsFunc.getClassChoise(objs[i], getFace());
		}
	}
	
	protected boolean inMenu = false;
	public BaseItem inMenu(){
		inMenu = true;
		return this;
	}
	
	boolean maked = false;
	public boolean maked(){
		return maked;
	}
	public ViewGroup make(){
		maked = true;
		return item = getViewGroup();
	}
	
	protected abstract clsFace getFace();
	
	public void update(Object... objs){
		
	}
	
	public abstract void reset();
	
	int base_layout;
	protected void setLayout(int layout){
		base_layout = layout;
	}
	
	protected ViewGroup getViewGroup(){
		final LayoutInflater inflater = LayoutInflater.from(m);
		return (ViewGroup) inflater.inflate(base_layout, null);
	}
	
	protected boolean isset(String field){
		return data.containsKey(field);
	}
	
	protected configField config(String field){
		return (configField) get(field);
	}
	
	protected void set(String field, Object obj){
		data.put(field, obj);
	}
	
	protected View.OnClickListener click(String field){
		return (View.OnClickListener) get(field);
	}
	
	protected Integer integer(String field){
		String ret = str(field);
		if (ret.equals("null")) return 0;
		if (ret.equals("false")) return 0;
		if (ret.equals("true")) return 1;
		return Integer.parseInt(ret);
	}
	
	protected Line line(String field){
		return (Line) get(field);
	}
	
	protected ItemClick itemclick(String field){
		return (ItemClick) get(field);
	}
	
	protected Object get(String field){
		return data.get(field);
	}
	
	protected String str(String field){
		return get(field) + "";
	}
	
	protected BaseItem del(String field){
		if (data.containsKey(field)) data.remove(field);
		return this;
	}
	
	protected boolean bool(String field){
		Object obj = get(field);
		if (obj == null) return false;
		return (Boolean) obj;
	}
	
	protected BaseMainItem mainitem(String field){
		return (BaseMainItem) get(field);
	}
	
	public BaseItem setId(Object obj){
		set("id", obj);
		return this;
	}
	
	public int getId(){
		return Integer.parseInt(str("id"));
	}
	
	public void remove(){
		category.removeLine(line);
		category.remove(this);
		item.removeAllViews();
		item.setVisibility(View.GONE);
	}
	
	protected void i(Object obj){
		log.i(this, obj);
	}
	
	protected void e(Exception ex){
		log.e(this, ex);
	}
	
	protected void setObj(Line line){
		setId(line);
		set("obj", line);
	}
	
	protected Line obj(){
		return line("obj");
	}
}

