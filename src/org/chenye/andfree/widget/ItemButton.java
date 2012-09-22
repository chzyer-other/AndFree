package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ItemButton extends ItemIntent{

	public ItemButton(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		int paramsLength = pass_objs.length;
		int layout = R.layout.andfree_view_button;
		if (paramsLength == 4) {
			layout = R.layout.andfree_view_button_2;
		} else if (paramsLength == 6){
			layout = R.layout.andfree_view_button_3;
		}
		return layout;
	}
	
	protected class widget extends baseWidget{
		public widgetHelper left = txt(R.id.andfree_textView2);
		public widgetHelper right = txt(R.id.andfree_textView1);
		public widgetHelper mid = txt(R.id.andfree_textView3);
	}
	protected widget widget = new widget();
	
	@Override
	public ItemButton setContent(String str){
		set("title", str);
		widget.right.init(item, str);
		return this;
	}
	
	public String getContent(){
		return str("title");
	}
	
	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
			@Override
			public void click(View.OnClickListener obj) {
				// TODO Auto-generated method stub
				if (isset("click_2")){
					set("click_3", obj);
				} else if (isset("click")) {
					set("click_2", obj);
				} else {
					set("click", obj);
				}
			}
			
			@Override
			public void string(String obj) {
				// TODO Auto-generated method stub
				if (isset("title_2")){
					set("title_3", obj);
				} else if (isset("title")) {
					set("title_2", obj);
				} else {
					set("title", obj);
				}
			}
			
		};
	}
	
	@Override
	public ViewGroup make() {
		// TODO Auto-generated method stub
		int paramsLength = pass_objs.length;
		if (paramsLength == 2){
			return super.make();
		}		
		item = getViewGroup();
		widget.left.init(item, get("title"), get("click")).setHover(ThemeColor.item_hover);
		
		if (paramsLength == 4){
			widget.right.init(item, get("title_2"), get("click_2")).setHover(ThemeColor.item_hover);
		} else if (paramsLength == 6){
			widget.mid.init(item, get("title_2"), get("click_2")).setHover(ThemeColor.item_hover);
			widget.right.init(item, get("title_3"), get("click_3")).setHover(ThemeColor.item_hover);
		}
		
		return item;
	}
}
