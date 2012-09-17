package org.chenye.andfree.widget;

import java.util.ArrayList;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.db.Tables;
import org.chenye.andfree.func.StrFunc;
import org.chenye.andfree.func.ThreadFunc;
import org.chenye.andfree.func.networkfunc.onCallbackRet;
import org.chenye.andfree.layout.BaseMainItem;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.widget.ItemIntent.intentClick;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class GroupCategory{
	ViewGroup container;
	String db_title;
	View.OnClickListener click_config;
	String nothing_hint = "暂时找不到相关信息";
	Context m;
	public GroupCategory(Context mContext, ViewGroup mContainer){
		container = mContainer;
		m = mContext;
	}
	
	ArrayList<BaseItem> items = new ArrayList<BaseItem>();
	ArrayList<TextView> lines = new ArrayList<TextView>();
	public GroupCategory addItem(BaseItem... item){
		for (BaseItem i:item){
			items.add(i);
		}
		return this;
	}
	
	boolean inMenu = false;
	public GroupCategory inMenu(){
		inMenu = true;
		return this;
	}
	
	public GroupCategory setConfig(final BaseMainItem item){
		click_config = new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				item.startForPage();
			}
		};
		return this;
	}
	
	public interface readTableSource{
		public BaseItem foreach(Line line);
		public Tables factoryTables(Tables t);
	}
	
	Class<?> _t;
	readTableSource _rts;
	int _readmore_count;
	int _readmore_position;
	public GroupCategory setTableSource(Class<?> table, readTableSource rts){
		_rts = rts;
		_t = table;
		Tables t = rts.factoryTables(new Tables(table));
		
		_readmore_position = 0;
		t.limit(_readmore_position + ", " + t.getLimit());
		_readmore_count = t.count();
		Line ret = t.result();
		for (Line i: ret){
			addItem(rts.foreach(i));
		}
		setContinue(1, ret);
		return this; 
	}
	
	public interface readSource{
		public BaseItem read(Line line);
	}
	
	Line _tmp_readmore_line;
	public void setContinue(int count, Line line){
		_readmore_position += line.length();
		if (_readmore_position >= _readmore_count) {
			_tmp_readmore_line = null;
			return;
		}
		_tmp_readmore_line = line;
	}
	
	public GroupCategory setTitle(String title){
		db_title = title;
		return this;
	}
	
	public GroupCategory setNothingHint(String hint){
		nothing_hint = hint;
		return this;
	}
	
	int last_make_index = 0;
	int start_index = 0;
	public void make(){
		if (last_make_index == 0){
			final LayoutInflater inflater = LayoutInflater.from(m);
			ViewGroup item = (ViewGroup) inflater.inflate(getLayoutId(), null);
			TextView t = (TextView) item.findViewById(R.id.andfree_textView1);
			if (db_title != null) t.setText(db_title);
			else {
				t.setVisibility(View.GONE);
				item.findViewById(R.id.andfree_bottom1).setVisibility(View.GONE);
			}
			
			if (inMenu) {
				t.setTextColor(Color.parseColor(ThemeColor.bold_text));
				t.setTextSize(StrFunc.dp2pix(m, 5) < 10 ? 10 : StrFunc.dp2pix(m, 5));
			}
			if (click_config == null) item.findViewById(R.id.andfree_imageView1).setVisibility(View.GONE);
			else item.findViewById(R.id.andfree_imageView1).setOnClickListener(click_config);
			addView(item);
			start_index = container.getChildCount();
		}
		
		
		for (int i=last_make_index; i<items.size(); i++){
			BaseItem baseitem = items.get(i);
			baseitem.setCategory(this);
			TextView tv = newLine(i);
			lines.add(tv);
			baseitem.setLine(tv);
			if (inMenu) baseitem.inMenu();
			addView(baseitem.make());
		}
		
		last_make_index = items.size();
		
		checkNothing();
		
		if (_tmp_readmore_line != null){
			addView(new ItemMore(m, "更多").setClick(new intentClick() {
				
				public void click(ItemIntent item, final View v) {
					// TODO Auto-generated method stub
					item.setTitle("加载中");
					ThreadFunc.async(1, new onCallbackRet(){
						@Override
						public Line call(Line ret) {
							// TODO Auto-generated method stub
							Tables t = _rts.factoryTables(new Tables(_t));
							t.limit(_readmore_position + ", " + t.getLimit());
							ret = t.result();
							for (Line i: ret){
								addItem(_rts.foreach(i));
							}
							return ret;
						}
						
						public Line ret(Line ret) {
							setContinue(1, ret);
							make();
							container.removeView(v);
							return null;
						};
					});
				}
			}).make());
		}
		
	}
	
	public void checkNothing(){
		if (items.size() > 0) return;
		
		addView(new ItemNothing(m, nothing_hint).make());
	}
	
	
	public void remove(BaseItem item){
		items.remove(item);
		checkNothing();
	}
	
	public void removeLine(TextView line){
		line.setVisibility(View.GONE);
		lines.remove(line);
		if (lines.size() > 0) {
			lines.get(0).setBackgroundColor(Color.parseColor("#fafafa"));
		}
	}
	
	public TextView newLine(int index){
		TextView item = newLineObj(index);
		container.addView(item, LayoutParams.MATCH_PARENT, 1);
		return item;
	}
	
	public TextView newLineObj(int index){
		final LayoutInflater inflater = LayoutInflater.from(m);
		TextView item = (TextView) inflater.inflate(R.layout.andfree_group_category_line, null);
		item.setBackgroundColor(Color.parseColor(index == 0 ? "#fafafa" : "#e6e6e6"));
		return item;
	}
	
	public void removeLastView(){
		int count = container.getChildCount();
		if (count <= 0) return;
		container.removeViewAt(count - 1);
	}
	
	public void addView(View v){
		container.addView(v);
	}
	
	public void addTopItem(BaseItem item){
		if (items.size() > 0){
			TextView txt = (TextView) container.getChildAt(start_index);
			txt.setBackgroundColor(Color.parseColor("#e6e6e6"));
		}
		
		items.add(item);
		container.addView(item.make(), start_index);
		TextView tv = newLineObj(0);
		item.setLine(tv);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		container.addView(tv, start_index, lp);
		
	}

	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_group_category;
	}

}
