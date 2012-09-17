package org.chenye.andfree.widget;

import org.chenye.andfree.R;

import android.content.Context;

public class ItemMore extends ItemIntent{
	public ItemMore(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_more;
	}
}
