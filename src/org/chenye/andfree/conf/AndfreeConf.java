package org.chenye.andfree.conf;

import java.lang.reflect.Field;

import org.chenye.andfree.db.BaseDBcore;

import android.content.Context;

public class AndfreeConf {
	// debug
	public static class DEBUG {
		public static boolean ALL_DENIED = false; // denied all debug
		public static DebugField LOG = _f(true);
	}

	//auto
	public static String PACKAGE_NAME = "org.chenye.andfree";
	public static String DBCORE_PACKAGE_NAME = BaseDBcore.class.getCanonicalName();
	// version
	public static String APP_NAME = "AndFree";
	public static String LOG_TAG = "andfree";
	public static int VERTION = 1;
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

		getFieldFromPackage(packageName + "._andfree.Conf");
		DBCORE_PACKAGE_NAME = AndfreeConf.PACKAGE_NAME + "._andfree.dbcore";
		UPDATE_CONF = true;
	}
	
	private static void getFieldFromPackage(String packageName){
		Class<?> c;
		try {
			c = Class.forName(packageName);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			
			e1.printStackTrace();
			return;
		}
		Field[] fs = c.getFields();
		for (Field f: fs){
			try {
				Object obj = f.get(c);
				AndfreeConf.class.getField(f.getName()).set(AndfreeConf.class, obj);
			} catch (Exception e) {
			}
		}
	}
}