package org.chenye.andfree.example;

import java.util.ArrayList;
import java.util.Hashtable;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.func.networkfunc.onCallback;
import org.chenye.andfree.layout.BaseBar;
import org.chenye.andfree.layout.BaseMainItem;
import org.chenye.andfree.obj.ActivityResult.onActivityResult;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.baseMainResult;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class TopBar extends BaseBar{
	
	public TopBar(Context context){
		super(context);
	}
	
	public TopBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public TopBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public static class widget extends baseWidget{
		public static widgetHelper big_title = txt(R.id.andfree_textView2);
		public static widgetHelper small_title = txt(R.id.andfree_textView1);
		
		public static widgetHelper tab1 = txt(R.id.andfree_textView3);
		public static widgetHelper tab1_bottom = txt(R.id.andfree_bottom2);
		
		public static widgetHelper tab2 = txt(R.id.andfree_textView4);
		public static widgetHelper tab2_bottom = txt(R.id.andfree_bottom3);
		
		public static widgetHelper btn1 = txt(R.id.andfree_textView5);
		public static widgetHelper btn2 = txt(R.id.andfree_textView6);
		
		public static widgetHelper more = txt(R.id.andfree_textView7);
		public static widgetHelper process = pgb(R.id.progressBar1);
	}
	

	
	@Override
	protected void init(){
		View.OnClickListener click_tab = new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (widget.equals(v, widget.tab1)){
					click(0);
				} else {
					click(1);
				}
			}
		};
		
		ViewGroup item = get_item(R.layout.andfree_layout_topbar);
		widget.small_title.init(item, "");
		widget.big_title.init(item, "title");
		widget.tab1.init(item, "tab1", click_tab);
		widget.tab1_bottom.init(item);
		widget.tab2.init(item, "tab2", click_tab);
		widget.tab2_bottom.init(item);
		
		widget.btn1.init(item);
		widget.btn2.init(item);
		
		widget.more.init(item, new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ( ! enableMenu) return;
				
				result.showMenu.SEND(m, new onActivityResult() {
					
					public void call(boolean result_ok, Intent data) {
						// TODO Auto-generated method stub
						if ( ! result_ok) return;
						Line d = new Line(data);
						if (menuCallback.containsKey(d.str("action"))){
							menuCallback.get(d.str("action")).call(d);
						}
					}
				}, new Intent(m, TopBarMenu.class).setData(Uri.parse(
						new Line()
						.put("y", getHeight())
						.put("x", "right")
						.put("press", pressCallback)
						.toString()
				)));
			}
		});
		
		addView(item, LayoutParams.FILL_PARENT, dp(44));
	}
	
	int pressCallback = -1;
	public void putPress(int count, onCallback call){
		pressCallback = count;
		menuCallback.put("press_" + count, call);
	}
	
	Hashtable<String, onCallback> menuCallback = new Hashtable<String, onCallback>();
	public void putMenuCallback(String field, onCallback call){
		menuCallback.put(field, call);
	}
	
	static class result extends baseMainResult{
		public result(String command) {
			super(command);
		}

		public static final baseMainResult showMenu = _f("topbar_show_result");
	}
	
	boolean enableMenu = true;
	public void disableMenu(){
		enableMenu = false;
	}
	
	public void enableMenu(){
		enableMenu = true;
	}
	
	public void showMenu(){
		widget.more.click();
	}
	
	
	int cur = -1;
	public void click(int index){
		if (cur == index) return;
		if (click_before_click != null) click_before_click.onClick(null);
		if (index == 0){
			widget.tab1_bottom.show();
			widget.tab2_bottom.hide(m);
		} else if (index == 1) {
			widget.tab1_bottom.hide(m);
			widget.tab2_bottom.show();
		} else {
			widget.tab1_bottom.hide(m);
			widget.tab2_bottom.hide(m);
		}
		
		click_after_click.onClick(null, index);
	}
	
	public void feight(BaseMainItem... names){
		if (names.length < 2) return;
		widget.tab1.setText(names[0].getLabel());
		widget.tab2.setText(names[1].getLabel());
	}
	
	ArrayList<String> titles = new ArrayList<String>();
	public void addTitle(String title){
		if (titles.size() == 0) titles.add(AndfreeConf.APP_NAME);
		titles.add(title);
		String prev = titles.size() >= 2 ? titles.get(titles.size() - 2) : "";
		setTitle(prev, title);
	}
	
	public void removeAllTitle(){
		String one = titles.get(0);
		titles.clear();
		titles.add(one);
		setTitle("", titles.get(0));
	}
	
	public String removeTitle(){
		titles.remove(titles.size() - 1);
		String prev = titles.size() >= 2 ? titles.get(titles.size() - 2) : "";
		String cur = titles.size() >= 1 ? titles.get(titles.size() - 1) : AndfreeConf.APP_NAME;
		setTitle(prev, cur);
		return cur;
	}
	
	public void setTitle(String prev, String cur){
		widget.big_title.setText(cur);
		widget.small_title.setText(prev);
	}
	
	public void disableEdit(){
		widget.btn2.hide(m);
		widget.btn1.hide(m);
	}
	
	public void enableEdit(String hint1, View.OnClickListener click1){
		widget.btn2.setText(hint1);
		widget.btn2.obj().setOnClickListener(click1);
		widget.btn2.show();
		widget.btn1.setText("取消");
		widget.btn1.obj().setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m.pageDown();
			}
		});
		widget.btn1.show();
	}
	
	public void enableEdit(String hint1, View.OnClickListener click1, String hint2, View.OnClickListener click2){
		widget.btn1.setText(hint1);
		widget.btn1.obj().setOnClickListener(click1);
		widget.btn1.show();
		
		widget.btn2.setText(hint2);
		widget.btn2.obj().setOnClickListener(click2);
		widget.btn2.show();
	}
	
	public void showLoading(){
		widget.process.init(m).show();
	}
	
	public void hideLoading(){
		widget.process.hide(m);
	}
}
