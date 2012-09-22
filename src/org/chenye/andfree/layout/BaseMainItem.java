package org.chenye.andfree.layout;

import java.util.ArrayList;
import java.util.Hashtable;

import org.chenye.andfree.db.DB;
import org.chenye.andfree.func.log;
import org.chenye.andfree.widget.BaseItem;
import org.chenye.andfree.widget.GroupCategory;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public abstract class BaseMainItem {
	protected BaseMainItem self = this;
	protected BaseMainActivity m;
	protected DB db;
	ViewGroup container;
	ScrollView scrollContainer;
	public BaseMainItem(BaseMainActivity mActicity){
		m = mActicity;
		db = DB.getInstance(m);
	}
	
	Hashtable<String, GroupCategory> group_tag = new Hashtable<String, GroupCategory>();
	public void addGroupCategoryTag(GroupCategory item, String tag){
		group_tag.put(tag, item);
	}
	
	public GroupCategory getGroupCategoryByTag(String tag){
		return group_tag.get(tag);
	}
	
	BaseItem subitem;
	public void setCurrentBaseItem(BaseItem item){
		subitem = item;
	}
	
	protected BaseItem getCurrentBaseItem(){
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
		
		//notify parent mainItem update it realtime
		
	}
	
	public final void resume(Object... objs){
		m.removeTitle();
		onResume(objs);
		if (getCurrentBaseItem() != null && ! resumeCheckDelete(objs)) {
			getCurrentBaseItem().updateLayout();
		}
		setCurrentBaseItem(null);
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
	
	protected boolean resumeCheckDelete(Object... objs){
		for (Object obj: objs){
			if (obj.toString().toLowerCase().equals("delete")) return true;
		}
		return false;
	}
	
	protected boolean resumeCheckUpdateLayout(Object... objs){
		for (Object obj: objs){
			if (obj.toString().toLowerCase().equals("updatelayout")) return true;
		}
		return false;
	}
	
	ArrayList<String> msg_when_back = new ArrayList<String>();
	public Object[] getMsgWhenBack(){
		Object[] s = new Object[msg_when_back.size()];
		for (int i=0; i<msg_when_back.size(); i++){
			s[i] = msg_when_back.get(i);
		}
		msg_when_back.clear();
		return s;
	}
	protected void addMsgWhenBack(String str){
		if (msg_when_back.contains(str)) return;
		msg_when_back.add(str);
	}
	
	protected void deleteListItemAndPagedown(){
		addMsgWhenBack("delete");
		m.pageDown(getMsgWhenBack());
	}
	
	public void removeView(){
		getContainer().removeAllViews();
	}
	
	protected void removeCurrentBaseItem(){
		getCurrentBaseItem().remove();
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
