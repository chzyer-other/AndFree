package org.chenye.andfree.layout;

import org.chenye.andfree.db.DB;
import org.chenye.andfree.func.log;
import org.chenye.andfree.widget.BaseItem;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public abstract class BaseMainItem {
	protected BaseMainActivity m;
	protected DB db;
	ViewGroup container;
	ScrollView scrollContainer;
	public BaseMainItem(BaseMainActivity mActicity){
		m = mActicity;
		db = DB.getInstance(m);
	}
	
	BaseItem subitem;
	public void setCurrentItem(BaseItem item){
		subitem = item;
	}
	
	protected BaseItem getCurrentItem(){
		return subitem;
	}
	
	public abstract String getLabel();
	
	boolean started = false;
	public final void start(Object... obj){
		log.d(this, "start");
		
		started = true;
		finishing = false;
		removeView();
		getContainer();
		getScrollContainer();
		
		//if (getSubItem() != null) getSubItem().clear();
		onStart(obj);
		
		m.setTitle(getLabel());
	}
	
	public final void resume(Object... objs){
		m.removeTitle();
		onResume(objs);
		setCurrentItem(null);
	}
	
	public void startForPage(Object... objs){
		getContext().pageUp(this, objs);
	}
	
	public void onResume(Object... objs){
		
	}
	
	public final void refresh(Object... obj){
		log.d(this, "refresh");
		//if (getSubItem() != null) getSubItem().clear();
		onRefresh(obj);
	}
	
	boolean finishing = false;
	public final boolean isFinish(){
		return finishing;
	}
	public final void finish(){
		if ( ! started) return;
		log.d(this, "finish");
		finishing = true;
		onFinish();
	}
	
	protected abstract void onStart(Object... obj);
	public void onRefresh(Object... obj){
		getContainer().removeAllViews();
		onStart(obj);
	}
	protected void onFinish(){}
	
	public BaseMainActivity getContext(){
		return m;
	}
	
	public ViewGroup getContainer(){
		if (container != null) return container;
		container = m.getContainer();
		return container;
	}
	
	public ScrollView getScrollContainer(){
		if (scrollContainer != null) return scrollContainer;
		scrollContainer = m.getScrollContainer();
		return scrollContainer;
	}
	
	public void addView(View v, int... ints){
		if (v == null) return;
		if (isFinish()) return;
		if (ints.length == 2){
			getContainer().addView(v, ints[0], ints[1]);
		} else if (ints.length == 1){
			getContainer().addView(v, ints[0]);
		} else {
			getContainer().addView(v);
		}
	}
	
	protected boolean check_delete(Object... objs){
		if (objs.length > 0 && objs[0].equals("delete")){
			return true;
		}
		return false;
	}
	
	protected void deleteListItemAndPagedown(){
		m.pageDown("delete");
	}
	
	public void removeView(){
		getContainer().removeAllViews();
	}
	
	protected void e(String str){
		log.e(this, str);
	}
	
	protected void e(Exception ex){
		log.e(this, ex);
	}
	
	protected void i(Object obj){
		log.i(this, obj);
		
	}
}
