package org.chenye.andfree.func;

import org.chenye.andfree.conf.AndfreeStaticConfigure;

public class FuncClass {
	public static Class<?> GetApplicationClass(String pack){
		String objName = String.format("%s.%s", AndfreeStaticConfigure.APPLICATION_PACKAGE, pack);
		try {
			return Class.forName(objName);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static Class<?> FindClass(Class<?> r, String string) {
		Class<?> layout = null;
		for (Class<?> cls: r.getClasses()){
			if (cls.getSimpleName().equals("layout")){
				layout = cls;
			}
		}
		if (layout == null) return null;
		return layout;
	}
}
