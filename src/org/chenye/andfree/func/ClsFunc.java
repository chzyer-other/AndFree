package org.chenye.andfree.func;

import org.chenye.andfree.db.baseConfig.configField;
import org.chenye.andfree.layout.BaseMainItem;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.cursor;
import org.chenye.andfree.obj.widgetHelper;
import org.chenye.andfree.widget.BaseItem.ItemClick;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;

public class ClsFunc {
	public static String getClassName(Object obj) throws ClassCastException{
		if (obj == null) return null;
		String t = obj.getClass().getSimpleName();
		
		if ( ! t.equals("")) {
			String t2 = obj.getClass().getSuperclass().getSimpleName();
			if ( ! t2.equals("Object")){
				return t2;
			}
			return t;
		}
		String type = null;
		
		type = try_type_TextWatcher(obj);
		if (type != null) return type;
		
		type = try_type_click(obj);
		if (type != null) return type;
		
		type = try_type_key(obj);
		if (type != null) return type;
		
		type = try_type_seek(obj);
		if (type != null) return type;
		
		type = try_type_focus(obj);
		if (type != null) return type;
		
		type = try_type_dialog_click(obj);
		if (type != null) return type;
		
		type = try_type_item_click(obj);
		if (type != null) return type;
		
		return null;
	}
	
	
	public static void getClassChoise(Object obj, clsFace cls){
		if (obj == null) return;
		if (cls == null) return;
		String name = getClassName(obj);
		if (name.equals("View.OnClickListener")){
			cls.click((View.OnClickListener) obj);
		} else if (name.equals("String")) {
			cls.string((String) obj);
		} else if (name.equals("Number")) {
			if (obj.getClass().getSimpleName().equals("Integer")) {
				cls.integer((Integer) obj);
			} else {
				cls.longint((Long) obj);
			}
		} else if (name.equals("Integer")) {
			cls.integer((Integer) obj);
		} else if (name.equals("seekbar.change")){
			cls.seek((SeekBar.OnSeekBarChangeListener) obj);
		} else if (name.equals("DialogInterface.OnClickListener")){
			cls.dialogClick((DialogInterface.OnClickListener) obj);
		} else if (name.equals("TextWatcher")){
			cls.TextWatcher((TextWatcher) obj);
		} else if (name.equals("View.OnKeyListener")){
			cls.onKey((View.OnKeyListener) obj);
		} else if (name.equals("Boolean")){
			cls.bool((Boolean) obj);
		} else if (name.equals("View.OnFocusChangeListener")){
			cls.onFocusChange((View.OnFocusChangeListener) obj);
		} else if (name.equals("widgetHelper")){
			cls.widget((widgetHelper) obj);
		} else if (name.equals("Uri")){
			cls.uri((Uri) obj);
		} else if (name.equals("configField")){
			cls.config((configField) obj);
		} else if (name.equals("cursor")){
			cls.cursor((cursor) obj);
		} else if (name.equals("Line")){
			cls.line((Line) obj);
		} else if (name.equals("Bitmap")){
			cls.bitmap((Bitmap) obj);
		} else if (name.equals("MainItem")) {
			cls.mainItem((BaseMainItem) obj);
		} else if (name.equals("ItemClick")){
			cls.itemClick((ItemClick) obj);
		} else {
			cls.other(obj);
		}
	}
	
	
	public static class clsFace {
		public void click(View.OnClickListener obj){}
		public void string(String obj){}
		public void integer(int obj){}
		public void longint(long obj){}		
		public void seek(SeekBar.OnSeekBarChangeListener obj){}
		public void dialogClick(DialogInterface.OnClickListener obj){}
		public void TextWatcher(TextWatcher obj){}
		public void onKey(View.OnKeyListener obj){}
		public void bool(boolean obj){}
		public void onFocusChange(View.OnFocusChangeListener obj){}
		public void widget(widgetHelper obj){}
		public void uri(Uri obj){}
		public void other(Object obj){}
		public void config(configField obj){}
		public void cursor(cursor obj){}
		public void line(Line obj){}
		public void bitmap(Bitmap obj){}
		public void mainItem(BaseMainItem obj){}
		public void itemClick(ItemClick obj){}
	}
	
	private static String check(Object obj, String name){
		return name;
	}
	
	private static String try_type_item_click(Object obj){
		try{
			return check((ItemClick) obj, "ItemClick");
		}catch (ClassCastException ex){
			
		}
		return null;
		
	}
	
	private static String try_type_dialog_click(Object obj){
		try{
			return check((DialogInterface.OnClickListener) obj, "DialogInterface.OnClickListener");
		}catch (ClassCastException ex){
			
		}
		return null;
		
	}

	private static String try_type_focus(Object obj){
		try{
			return check((View.OnFocusChangeListener) obj, "View.OnFocusChangeListener");
		}catch (ClassCastException ex){
			
		}
		return null;
		
	}
	
	private static String try_type_TextWatcher(Object obj){
		try{
			return check((TextWatcher) obj, "TextWatcher");
		}catch (ClassCastException ex){
			
		}
		return null;
	}
	
	private static String try_type_seek(Object obj){
		try{
			return check((SeekBar.OnSeekBarChangeListener) obj, "seekbar.change");
		} catch (ClassCastException ex){
			
		}
		return null;
	}
	
	private static String try_type_click(Object obj){
		try{
			return check((View.OnClickListener) obj, "View.OnClickListener");
		}catch (ClassCastException ex){
			
		}
		return null;
	}
	
	private static String try_type_key(Object obj){
		try{
			return check((View.OnKeyListener) obj, "View.OnKeyListener");
		}catch (ClassCastException ex){
			
		}
		return null;
	}
}
