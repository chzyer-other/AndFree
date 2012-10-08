package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.text.InputType;
import android.view.ViewGroup;

public class ItemEdit extends BaseItem{
	
	public ItemEdit(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	String title;
	String hint;
	
	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
			@Override
			public void string(String obj) {
				// TODO Auto-generated method stub
				if (title != null) hint = obj;
				else title = obj;
			}
		};
	}
	
	public ItemEdit setText(String text){
		title = text;
		if (maked()){
			widget.edt.setText(title);
		}
		return this;
	}
	
	public ItemEdit setHint(String hints){
		hint = hints;
		if (maked()){
			widget.edt.edt().setHint(hints);
		}
		return this;
	}
	
	public ItemEdit setPaswType(){
		set("type", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		return this;
	}
	
	static class widget extends baseWidget {
		public static widgetHelper edt = txt(R.id.andfree_editText1);
	}
	
	@Override
	public ViewGroup make(){
		super.make();
		
		widget.edt.init(item, title);
		if (isset("type")) {
			widget.edt.edt().setInputType(integer("type"));
		}
		
		if (hint != null) widget.edt.edt().setHint(hint);
		return item;
	}
	
	@Override
	public String getContent() {
		// TODO Auto-generated method stub
		return widget.edt.select(item).getText();
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		widget.edt.select(item).setText(null);
	}
	
	public boolean isEmpty(){
		
		return widget.edt.isEmpty();
	}
	
	public boolean checkEmptyAndHint(String str){
		return widget.edt.checkEmptyAndHint(str);
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_edit;
	}

	
}
