package org.chenye.andfree.hook;

import java.lang.reflect.Field;

import org.chenye.andfree.widget.IWidget;

public class OnStart {
	
	public static void UpdateLayoutIdRange(Class<?> r){
		Class<?> layout = null;
		for (Class<?> cls: r.getClasses()){
			if (cls.getSimpleName().equals("layout")){
				layout = cls;
			}
		}
		if (layout == null) return;
		long min = 0;
		long max = 0;
		for (Field f: layout.getFields()){
			try {
				long i = f.getLong(layout);
				if (i < min || min == 0){
					min = i;
				}
				if (i > max || max == 0){
					max = i;
				}
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		IWidget.layoutRange[0] = min;
		IWidget.layoutRange[1] = max;
	}
}
