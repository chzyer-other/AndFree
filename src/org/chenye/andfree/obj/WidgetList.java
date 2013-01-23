package org.chenye.andfree.obj;

import java.lang.reflect.Field;

import org.chenye.andfree.widget.*;
import org.chenye.andfree.widget.AFLLayout;

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
	
	private IWidget<?, ?> getWidget(Field f){
		try {
			return (IWidget<?, ?>) f.get(this);
		} catch (Exception ex){
			
		}
		return null;
	}
	
	protected AFCheck check(int id){
		return new AFCheck(id);
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
	
	protected AFRLayout rlayout(int id){
		return new AFRLayout(id);
	}
	
	protected AFLLayout llayout(){
		return new AFLLayout();
	}
	protected AFLLayout llayout(int id){
		return new AFLLayout(id);
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
