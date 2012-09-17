package org.chenye.andfree.layout;

import org.chenye.andfree.obj.dbActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

public abstract class BaseMainActivity extends dbActivity{
	protected BaseMainActivity m = this;
	protected BaseBar bar;
	private BaseMainPage mainpage;
	
	public BaseBar getBar(){
		if (bar == null) bar = new BaseBar(m);
		return bar;
	}
	
	public BaseMainPage getMainPage(){
		if (mainpage == null) {
			mainpage = new BaseMainPage(m, getBaseContainerId(), getScrollChildId());
		}
		return mainpage;
	}
	
	@Override
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
	}

	protected abstract int getBaseContainerId();
	protected abstract int getScrollChildId();
	
	public ViewGroup getContainer(){
		return getMainPage().getContainer();
	}
	
	public ScrollView getScrollContainer(){
		return getMainPage().getScrollContainer();
	}
	
	public void feight(BaseMainItem... items){
		getMainPage().feight(items);
		getMainPage().setBeforeClick(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getMainPage().first();
			}
		});
		getBar().feight(items);
		getBar().setAfterClick(new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				getMainPage().click(which);
			}
		});
	}
	
	public void pageUp(BaseMainItem item, Object... objs){
		getMainPage().next();
		getMainPage().addCache(item);
		item.start(objs);
	}
	
	public void pageDown(Object... objs){
		getMainPage().removeCacheMainItem(getMainPage().getLevel()).finish();
		getMainPage().prev();
		getMainPage().getCacheMainItem(getMainPage().getLevel()).resume(objs); 
	}
	
	public void enableMenu(){}
	public void disableMenu(){}
	public void showMenu(){}
	public void removeTitle(){}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (getMainPage().getLevel() == 0) {
			finish();
			return;
		}
		pageDown();
	}
	
	public BaseMainItem getCurrentItem(){
		return getMainPage().getCurrentItem();
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if (keyCode == 82){
    		showMenu();
    		return true;
    	}
    	return super.onKeyDown(keyCode, event);
    }
	
	public void showLoading(){}
	public void hideLoading(){}
}

