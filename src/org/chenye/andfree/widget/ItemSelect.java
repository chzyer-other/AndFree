package org.chenye.andfree.widget;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.ThemeColor;
import org.chenye.andfree.db.dbField;
import org.chenye.andfree.db.baseConfig.configField;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.func.UIFunc;
import org.chenye.andfree.helper.dialogHelper;
import org.chenye.andfree.helper.dialogHelper.dataClick;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.baseWidget;
import org.chenye.andfree.obj.widgetHelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class ItemSelect extends BaseItem{
	public ItemSelect(Context mContext, Object... objs){
		super(mContext, objs);
	}
	
	@Override
	protected clsFace getFace() {
		// TODO Auto-generated method stub
		return new clsFace(){
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
			public void config(configField obj) {
				// TODO Auto-generated method stub
				set("config", obj);
			}
		};
	}
	
	class widget extends baseWidget {
		public widgetHelper title = txt(R.id.andfree_textView1);
		public widgetHelper hint = txt(R.id.andfree_textView2);
		public widgetHelper pic = img(R.id.andfree_imageView1);
	}
	widget widget = new widget();
	
	@Override
	public ViewGroup make(){
		super.make();
		widget.title.init(item, get("title"));
		widget.hint.init(item, get("hint"));
		item.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				int[] x = new int[10];
				item.getLocationOnScreen(x);
				Line l = new Line();
				l.put("x", "right").put("y", x[1]).put("h", item.getHeight())
				.put("data", select).put("title", str("title"));
				
				Conf.mainResult.itemselect.SEND((dbActivity) m, new onActivityResult() {
					
					@Override
					public void call(boolean result_ok, Intent data) {
						// TODO Auto-generated method stub
						if ( ! result_ok) return;
						Line d = new Line(data);
						set("select_data", d.str(1));
						if (isset("config")) config("config").set(d.str(1));
						widget.hint.select(item).setText(d.str(0));
					}
				}, l.toIntent(m, SelectMenu.class));
				
				/**/
				dialogHelper dialog = new dialogHelper(m);
				
				for (Line item:select){
					dialog.addItem(item.str(0), item);
				}
				
				dialog.setDialogClick(new dataClick() {
					
					public void onClick(Line data) {
						if (isset("click")){
							boolean ret = ((SelectClick) get("click")).click(ItemSelect.this, data.str(1), data.str(0));
							if ( ! ret) return;
						}
						setSelect(data);
						
					}
				}).setTitle(str("title")).show();
				
			}
		});
		
		item.setTag(R.id.hover, ThemeColor.item_hover);
		UIFunc.setHover(item);
		return item;
	}
	
	@Override
	public String getContent(){
		return str("hint");
	}
	
	public String getData(){
		return str("select_data");
	}
	
	Line select = Line.def();
	public void add(String field, String values){
		if ( ! isset("hint") && ! isset("config")){
			set("hint", values);
		} else if ( ! isset("hint") && isset("config")){
			set("hint", config("config").str());
		}
		
		if (get("hint").equals(values)) {
			set("select_data", values);
			set("hint", field);
		}
		select.put(new Line().put(field).put(values));
		
		if (isset("bind_line")){
			Line data = line("bind_line");
			dbField f = (dbField) get("bind_field");
			if (values.equals(data.str(f))){
				set("hint", field);
				set("select_data", field);
			}
		}
	}
	
	public ItemSelect setDefault(String value){
		set("select_data", value);
		return this;
	}
	
	public ItemSelect setItems(String... data) {
		for (int i=0; i<data.length; i+= 2){
			add(data[i], data[i + 1]);
		}
		return this;
	}
	
	public ItemSelect setSelect(Line data){
		set("select_data", data.str(1));
		if (isset("config")) config("config").set(data.str(1));
		if (isset("bind_line")) {
			Line line = line("bind_line");
			dbField field = (dbField) get("bind_field");
			new Line(line.getTableClass())
				.put(field, data.str(1))
				.put(line._field(), line.id())
				.save();
		}
		widget.hint.select(item).setText(data.str(0));
		return this;
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public interface SelectClick{
		/** return false if not allow */
		public boolean click(ItemSelect item, String value, String lable);
	}
	
	public ItemSelect setConfig(configField conf){
		set("config", conf);
		return this;
	}
	
	public ItemSelect setClick(SelectClick click){
		set("click", click);
		return this;
	}

	@Override
	protected int getLayoutId() {
		// TODO Auto-generated method stub
		return R.layout.andfree_view_select;
	}
	
	public ItemSelect bindDatabase(Line data, dbField field){
		set("bind_line", data);
		set("bind_field", field);
		if (select.length() > 0){
			for (Line i: select){
				if (i.str(1).equals(data.str(field))){
					set("hint", i.str(0));
					set("select_data", i.str(1));
					return this;
				}
			}
			if (data.str(field) != null && data.str(field).length() > 0){
				set("hint", data.str(field));
				set("select_data", data.str(field));
			}
		}
		return this;
	}
}
