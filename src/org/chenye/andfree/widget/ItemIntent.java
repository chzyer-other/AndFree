package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.func.UIFunc;
import org.chenye.andfree.layout.BaseMainActivity;
import org.chenye.andfree.layout.BaseMainItem;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ItemIntent extends BaseItem{
	protected ItemIntent self = this;
	public ItemIntent(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	BaseMainItem mi;
	public ItemIntent setMainItem(BaseMainItem mMi){
		mi = mMi;
		return this;
	}
	
	@Override 
	protected clsFace getFace(){
		return new clsFace(){
			@Override
			public void string(String obj) {
				// TODO Auto-generated method stub
				set("title", obj);
			}
			
			public void click(View.OnClickListener obj) {
				set("click", obj);
			};
			
			public void mainItem(BaseMainItem obj) {
				set("mainitem", obj);
			};
		};
	}
	
	protected class widget extends baseWidget {
		public widgetHelper title = txt(R.id.andfree_textView1);
		public widgetHelper arrow = img(R.id.andfree_imageView1);
	}
	protected widget widget = new widget();

	public ItemIntent setTitle(String title){
		widget.title.select(item).setText(title);
		return this;
	}
	
	public ItemIntent getObj(){
		return this;
	}
	
	@Override
	public void update(Object... objs){
		if (objs.length <= 0) return;
		Line line = (Line) objs[0];
		if (line.invalid()){
			remove();
			return;
		}
		
		widget.title.select(item).setText(str("title"));
	}
	
	@Override
	public ViewGroup make(){
		super.make();
		widget.title.init(item, get("title"));
		
		
		if (inMenu) {
			widget.title.txt().setTextSize(16);
			widget.arrow.hide(item);
		}
		
		item.setOnClickListener(click_intent);
		
		item.setTag(R.id.hover, ThemeColor.item_hover);
		UIFunc.setHover(item);
		return item;
	}
	
	public interface intentClick{
		public void click(ItemIntent item, View v);
	}
	
	public ItemIntent setClick(intentClick click){
		set("intentclick", click);
		return this;
	}
	
	protected View.OnClickListener click_intent = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (get("mainitem") != null){
				flagSelfToCurrentBaseItem((BaseMainActivity) m);
				if (get("id") != null){
					mainitem("mainitem").startForPage(get("id"));
				} else {
					mainitem("mainitem").startForPage();
				}
			} else if (isset("intentclick")) {
				((intentClick) get("intentclick")).click(ItemIntent.this, item);
			} else if (isset("click")){
				click("click").onClick(item);
			}
		}
	};
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_intent;
	}
}
