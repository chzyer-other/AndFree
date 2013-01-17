package org.chenye.andfree.obj;

import java.lang.reflect.Field;

import org.chenye.andfree.func.log;
import org.chenye.andfree.widget.AFButton;
import org.chenye.andfree.widget.AFEdit;
import org.chenye.andfree.widget.AFImage;
import org.chenye.andfree.widget.AFLlayout;
import org.chenye.andfree.widget.AFRadio;
import org.chenye.andfree.widget.AFRadioList;
import org.chenye.andfree.widget.AFRlayout;
import org.chenye.andfree.widget.AFText;
import org.chenye.andfree.widget.ICopy;
import org.chenye.andfree.widget.IWidget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup;

public class WidgetList {
	public void initAll(ViewGroup vg, IWidget<?, ?> v, int width, int height){
		initAll(vg.getContext(), v);
		v.addTo(vg, width, height);
	}
	
	public void initAll(ViewGroup vg, IWidget<?, ?> v){
		initAll(vg.getContext(), v);
		v.addTo(vg);
	}
	
	public void initAll(Context m, IWidget<?, ?> v){
		v.inflateLayout(m);
		initAll(v.viewgroup());
	}
	
	public void initAll(Context m){
		Field[] fields = getClass().getFields();
		for (Field f:fields){
			IWidget<?, ?> widget = getWidget(f);
			if (widget == null){
				continue;
			}
			widget.inflate(m);
		}
	}
	
	public void initAll(ViewGroup vg){
		Field[] fields = getClass().getFields();
		for (Field f:fields){
			IWidget<?, ?> widget = getWidget(f);
			if (widget == null){
				continue;
			}
			widget.inflate(vg);
		}
	}
	
	public void initAll(Activity a){
		Field[] fields = getClass().getFields();
		for (Field f:fields){
			IWidget<?, ?> widget = getWidget(f);
			if (widget == null){
				continue;
			}
			widget.inflate(a);
		}
	}
	
	public void initAll(Dialog a){
		Field[] fields = getClass().getFields();
		for (Field f:fields){
			IWidget<?, ?> widget = getWidget(f);
			if (widget == null){
				continue;
			}
			widget.inflate(a);
		}
	}
	
	/**
	 * PLEASE IMPLEMENT ICopy FOR THE OBJECT
	 * @param field
	 */
	public void newInstance(String field){
		try {
			Field f = getClass().getField(field);
			ICopy<?> obj = (ICopy<?>) f.get(this);
			f.set(this, obj.copy());
		} catch (Exception e) {
			log.i(this, "implements " + field + " with ICopy");
		}
		
	}
	
	private IWidget<?, ?> getWidget(Field f){
		try {
			return (IWidget<?, ?>) f.get(this);
		} catch (Exception ex){
			
		}
		return null;
	}
	
	protected AFText txt(int id){
		return new AFText(id);
	}
	protected AFText txt(){
		return new AFText();
	}
	
	protected AFImage img(int id){
		return new AFImage(id);
	}
	
	protected AFRlayout rlayout(int id){
		return new AFRlayout(id);
	}
	
	protected AFLlayout llayout(){
		return new AFLlayout();
	}
	protected AFLlayout llayout(int id){
		return new AFLlayout(id);
	}
	
	protected AFEdit edt(int id){
		return new AFEdit(id);
	}
	
	protected AFButton btn(){
		return new AFButton();
	}
	protected AFButton btn(int id){
		return new AFButton(id);
	}
	
	protected AFRadio radio(int id){
		return new AFRadio(id);
	}
	
	protected AFRadioList radiolist(AFRadio... objs){
		return new AFRadioList(objs);
	}
}
