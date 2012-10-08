package org.chenye.andfree.conf;

import java.lang.reflect.Field;

import org.chenye.andfree.db.AndfreeDBcore;

import android.content.Context;

public class AndfreeConf {
	// debug
	public static class DEBUG {
		public static boolean ALL_DENIED = false; // denied all debug
		public static DebugField LOG = _f(true);
	}

	//auto
	public static String PACKAGE_NAME = "org.chenye.andfree";
	public static String DBCORE_PACKAGE_NAME = AndfreeDBcore.class.getCanonicalName();
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
			getFieldFromPackage(packageName + "._andfree.Conf");
			DBCORE_PACKAGE_NAME = AndfreeConf.PACKAGE_NAME + "._andfree.dbcore";
		} catch (Exception e) {
		}
		UPDATE_CONF = true;
	}
	
	private static void getFieldFromPackage(String packageName) throws Exception {
		Class<?> c = Class.forName(packageName);
		Field[] fs = c.getFields();
		for (Field f: fs){
			try {
				AndfreeConf.class.getField(f.getName()).set(AndfreeConf.class, f.get(c));
			} catch (NoSuchFieldException e) {
			}
		}
	}
}