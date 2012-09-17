package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.db.baseConfig.configField;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.func.UIFunc;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class ItemBool extends BaseItem{
	public ItemBool(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_bool;
	}
	
	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
			@Override
			public void config(configField obj) {
				// TODO Auto-generated method stub
				set("config", obj);
				set("check", obj.bool());
			}
						
			@Override
			public void string(String obj) {
				// TODO Auto-generated method stub
				if (isset("title")){
					set("hint", obj);
				} else {
					set("title", obj);
				}
			}
			
			@Override
			public void click(OnClickListener obj) {
				// TODO Auto-generated method stub
				set("click", obj);
			}
			
		};
	}
	
	static class widget extends baseWidget {
		public static widgetHelper title = txt(R.id.andfree_textView1);
		public static widgetHelper hint = txt(R.id.andfree_textView2);
		public static widgetHelper pic = img(R.id.andfree_imageView1);
	}
	
	@Override
	public ViewGroup make(){
		super.make();
		widget.title.init(m, item, get("title"));
		widget.hint.init(m, item, get("hint"));
		widget.pic.init(m, item, click_item, img_switch[integer("check")]);
		item.setTag(R.id.hover, ThemeColor.item_hover);
		item.setOnClickListener(click_item);
		UIFunc.setHover(item);
		return item;
	}
	
	public ItemBool setSummary(String str){
		widget.hint.select(item).setText(str);
		return this;
	}
	
	boolean ignoreBoolClick = false;
	public void clickThrough(){
		ignoreBoolClick = true;
		click();
	}
	
	public void click(){
		click_item.onClick(item);
	}
	
	public ItemBool setBoolClick(ItemBoolClick click){
		set("boolClick", click);
		return this;
	}
	
	public interface ItemBoolClick{
		public boolean click(boolean ret, ItemBool w, View v);
	}
	
	public static int[] img_switch = new int[] {R.drawable.andfree_view_bool_switcher, R.drawable.andfree_view_bool_switcher_hover};
	
	View.OnClickListener click_item = new View.OnClickListener() {
		
		public void onClick(View v) {
			if (isset("boolClick") && ! ignoreBoolClick){
				boolean ret = ((ItemBoolClick) get("boolClick")).click( ! bool("check"), ItemBool.this, item);
				if ( ! ret) return;
			}
			ignoreBoolClick = false;
			
			set("check", bool("check") ? false : true);
			if (get("config") != null){
				config("config").set(bool("check"));
			}
			widget.pic.select(item).setImageResource(img_switch[integer("check")]);
			if (isset("click")){
				if (bool("check")){
					click("click").onClick(item);
				} else {
					click("click").onClick(null);
				}
			}
		}
	};
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}



}
