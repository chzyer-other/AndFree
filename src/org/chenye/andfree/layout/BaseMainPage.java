package org.chenye.andfree.layout;

import java.util.ArrayList;

import org.chenye.andfree.func.log;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.RelativeLayout.LayoutParams;

public class BaseMainPage {
	public BaseMainPage(BaseMainActivity context, int container_id, int scroll_child_id){
		init_system(context);
		CONTAINER_ID = container_id;
		SCROLL_CHILD_ID = scroll_child_id;
	}
	
	protected ArrayList<ScrollView> containers = new ArrayList<ScrollView>();
	protected int CONTAINER_ID;
	protected int SCROLL_CHILD_ID;
	protected BaseMainActivity m;
	protected LinearLayout container;
	protected ScrollView scrollcontainer;
	
	int width;
	//-----------------------------------------------------------------��ݿ����
	
	int inLevel = 0;
	public int getLevel(){
		return inLevel;
	}
	
	private void init_system(BaseMainActivity context){
		m = context;
		width = context.getWindowManager().getDefaultDisplay().getWidth();
		
	}
	
	View.OnClickListener click_before_click = null;
	public void setBeforeClick(View.OnClickListener before_click){
		click_before_click = before_click;
	}
	
	View.OnClickListener pageUp;
	public void first(){
		if (inLevel == 0) return;
		ScrollView orgScroll = getScrollContainer(inLevel);
		inLevel = 0;
    	ScrollView newScroll = getScrollContainer(inLevel);
		
    	int time = 250;
    	newScroll.setVisibility(View.VISIBLE);
		
		TranslateAnimation ta = new TranslateAnimation(-width, 0, 0, 0);
		ta.setDuration(time);
		TranslateAnimation ta_child = new TranslateAnimation(0, width, 0, 0);
		ta_child.setDuration(time);
		
		newScroll.startAnimation(ta);
		orgScroll.startAnimation(ta_child);
		
		orgScroll.setVisibility(View.GONE);
		
		handle.sendEmptyMessageDelayed(0, time);
		changeContainer();
	}
	
	private ScrollView getScrollContainer(int index){
		if (inLevel >= containers.size()){
			ScrollView s = initScrollView();
			
			if (index != 0) s.setVisibility(View.GONE);
			LinearLayout lly = new LinearLayout(m);
			lly.setOrientation(LinearLayout.VERTICAL);
			lly.setId(SCROLL_CHILD_ID);
			lly.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			s.addView(lly);
			try{
				ViewGroup vg = (ViewGroup) m.findViewById(CONTAINER_ID);
				vg.addView(s, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			}catch(Exception ex){
				e(ex);
			}
			containers.add(s);
		}
		
		return containers.get(index);
	}
	
	public ScrollView initScrollView(){
		return new ScrollView(m);
	}
	
    public void prev(){
    	int time = 300;
    	
    	ScrollView orgScroll = getScrollContainer(inLevel);
    	inLevel--;
    	ScrollView newScroll = getScrollContainer(inLevel);
    	
    	newScroll.setVisibility(View.VISIBLE);
		
		TranslateAnimation ta = new TranslateAnimation(-width, 0, 0, 0);
		ta.setDuration(time);
		TranslateAnimation ta_child = new TranslateAnimation(0, width, 0, 0);
		ta_child.setDuration(time);
		
		newScroll.startAnimation(ta);
		orgScroll.startAnimation(ta_child);
		
		orgScroll.setVisibility(View.GONE);
		
		handle.sendEmptyMessageDelayed(0, time);
		changeContainer();
    }
    
    Handler handle = new Handler(){
    	@Override
    	public void handleMessage(Message msg){
    		if (event_prev == null) return;
    		event_prev.onClick(null);
    		event_prev = null;
    	}
    };
    
    public void next(){
    	int time = 400;
    	ScrollView orgScroll = getScrollContainer(inLevel);
    	inLevel ++;
    	ScrollView newScroll = getScrollContainer(inLevel);
    	
    	newScroll.setVisibility(View.VISIBLE);
    	

		
		TranslateAnimation ta = new TranslateAnimation(0, -width, 0, 0);
		ta.setDuration(time);
		
		TranslateAnimation ta_child = new TranslateAnimation(width, 0, 0, 0);
		ta_child.setDuration(time);
		
		orgScroll.startAnimation(ta);
		newScroll.startAnimation(ta_child);
		
		orgScroll.setVisibility(View.GONE);
		
		changeContainer();
		
    }
    
	public void setPrev(View.OnClickListener event){
		event_prev = event;
	}
	
	private View.OnClickListener event_prev = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	public ScrollView getScrollContainer(){
		if (scrollcontainer != null) return scrollcontainer;
		
		
		
		if (inLevel < containers.size()){
			scrollcontainer = getScrollContainer(inLevel);
		}
		
		return scrollcontainer;
	}
	
	public LinearLayout getContainer(){
		if (container != null) return container;
		container = (LinearLayout) getScrollContainer(inLevel).findViewById(SCROLL_CHILD_ID);
		return container;
	}
	
	public void changeContainer(){
		container = null;
		scrollcontainer = null;
	}
	
	public void e(Exception ex){
		log.e(this, ex);
	}
	
	ArrayList<BaseMainItem> levelItem = new ArrayList<BaseMainItem>();
	public void addCache(BaseMainItem item){
		levelItem.add(item);
	}
	
	public BaseMainItem removeCacheMainItem(int level){
		while (levelItem.size() - 1 > level){
			levelItem.remove(level + 1);
		}
		return levelItem.remove(level);
	}
	
	public BaseMainItem getCacheMainItem(int level){
		while (levelItem.size() - 1 > level){
			levelItem.remove(level + 1);
		}
		return levelItem.get(level);
	}
	
	public BaseMainItem getFeightMainItem(int index){
		if (index >= items.size()) return null;
		return items.get(index);
	}
	
	ArrayList<BaseMainItem> items = new ArrayList<BaseMainItem>();
	public void feight(BaseMainItem... mainitems){
		for (int i=0; i<mainitems.length; i++){
			items.add(mainitems[i]);			
		}
	}
	
	int cur = -1;
	public void click(int index){
		if (m == null) m = items.get(0).getContext();
		if (cur == index) return;
		
		if (click_before_click != null) click_before_click.onClick(null);
		
		levelItem.clear();
		levelItem.add(items.get(index));
		items.get(index).start();
	}
	
	public BaseMainItem getCurrentItem(){
		return getCacheMainItem(getLevel());
	}
	
}
