package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.func.StrFunc;
import org.chenye.andfree.layout.BaseMainItem;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;
import org.chenye.andfree.widget.ItemIntent.intentClick;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ItemText extends BaseItem{
	public ItemText(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
			@Override
			public void itemClick(ItemClick obj) {
				// TODO Auto-generated method stub
				set("click", obj);
			}
			
			@Override
			public void string(String obj) {
				// TODO Auto-generated method stub
				if (isset("content")){
					set("btn", obj);
				} else if (isset("title")){
					set("content", obj);
				} else {
					set("title", obj);
				}
			}
		};
	}
	
	public ItemText setClick(intentClick click){
		set("intentclick", click);
		return this;
	}
	
	@Override
	public ItemText setContent(String content){
		set("content", content);
		if (maked()){
			widget.content.select(item).setText(content);
		}
		return this;
	}
	
	BaseMainItem mi;
	public ItemText setMainItem(BaseMainItem mMi){
		mi = mMi;
		return this;
	}
	
	static class widget extends baseWidget {
		public static widgetHelper title = txt(R.id.andfree_textView1);
		public static widgetHelper content = txt(R.id.andfree_textView2);
		public static widgetHelper btn = txt(R.id.andfree_textView3);
	}
	
	@Override
	public void update(Object... objs){
		if (objs.length <= 0) return;
	}
	
	@Override
	public ViewGroup make(){
		if ( ! isset("content") && isset("title")){
			setContent(str("title"));
			del("title");
		}
		
		super.make();
		widget.content.init(m, item, get("content"));
		
		if ( ! isset("title")){
			widget.title.hide(m, item);
			widget.content.txt().setTextSize(StrFunc.dp2pix(m, 8));
		} else {
			widget.title.init(m, item, get("title"));
		}
		
		if (isset("btn")){
			widget.btn.init(m, item, get("btn"), click_btn).setHover(ThemeColor.item_hover).show();
		}
		return item;
	}
	
	public widgetHelper getContent(){
		return widget.title.select(item);
	}
	
	View.OnClickListener click_btn = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isset("click")) itemclick("click").click(baseitem, v);
			
		}
	};

	@Override
	public void reset() {
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_text;
	}
}
