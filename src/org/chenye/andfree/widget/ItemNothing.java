package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.view.ViewGroup;

public class ItemNothing extends BaseItem{

	public ItemNothing(Context mContext, Object... objs) {
		super(mContext, objs);
	}

	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
			
			@Override
			public void string(String obj) {
				set("title", obj);
			}
		};
	}

	class widget extends baseWidget{
		public widgetHelper title = txt(R.id.andfree_textView1);
	}
	widget widget = new widget();
	
	@Override
	public ViewGroup make() {
		// TODO Auto-generated method stub
		super.make();
		widget.title.init(m, item, str("title"));
		return item;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_nothing;
	}

}
