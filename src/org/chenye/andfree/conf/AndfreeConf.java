package org.chenye.andfree.conf;

import java.lang.reflect.Field;

import android.content.Context;

public class AndfreeConf {
	// debug
	public static class DEBUG {
		public static boolean ALL_DENIED = false; // denied all debug
		public static DebugField LOG = _f(true);
	}

	//auto
	public static String PACKAGE_NAME = "org.chenye.andfree";
	// version
	public static String APP_NAME = "AndFree";
	public static String LOG_TAG = "andfree";
	public static int VERTION = -1;
	public static String SD_DIR = "";
	public static String DB_FILENAME = LOG_TAG + ".db";
	public static String SECURITYFUNC_KEY = "";

	
	
	
	
	
	private static boolean UPDATE_CONF = false;

	protected static DebugField _f(boolean now, DebugField... depenys) {
		return new DebugField(now, depenys);
	}
	
	public static void update(Context obj){
		if (UPDATE_CONF) return;
		String packageName = obj.getPackageName();
		PACKAGE_NAME = packageName;
		try {
			Class<?> c = Class.forName(packageName + "._andfree.Conf");
			Field[] fs = c.getFields();
			for (Field f: fs){
				try {
					AndfreeConf.class.getField(f.getName()).set(AndfreeConf.class, f.get(c));
				} catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		UPDATE_CONF = true;
	}
}