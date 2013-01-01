package org.chenye.andfree.conf;

import java.lang.reflect.Field;

import org.chenye.andfree.db.BaseDBcore;
import org.chenye.andfree.hook.OnStart;
import org.chenye.andfree.msgpack.IDictPicker;
import org.chenye.andfree.msgpack.IListPicker;
import org.chenye.andfree.msgpack.MsgPackUnpack;
import org.chenye.andfree.msgpack.MsgPackUnpack.IGetDictPicker;
import org.chenye.andfree.msgpack.MsgPackUnpack.IGetListPicker;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.os.Environment;

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
	public static String SD_DIR = Environment.getExternalStorageDirectory().getPath() + "/" + LOG_TAG + "/";
	public static String DB_FILENAME = LOG_TAG + ".db";
	public static String SECURITYFUNC_KEY = "";

	public static int SCREEN_WIDTH = 0;
	
	private static boolean UPDATE_CONF = false;

	protected static DebugField _f(boolean now, DebugField... depenys) {
		return new DebugField(now, depenys);
	}
	
	public static void update(Context obj){
		if (UPDATE_CONF) return;
		
		String packageName = obj.getPackageName();
		PACKAGE_NAME = packageName;

		getFieldFromPackage(packageName + "._andfree.Conf");
		try {
			OnStart.UpdateLayoutIdRange(Class.forName(packageName + ".R"));
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBCORE_PACKAGE_NAME = AndfreeConf.PACKAGE_NAME + "._andfree.dbcore";
		UPDATE_CONF = true;
		
		MsgPackUnpack.setGetDict(new IGetDictPicker() {
			
			public IDictPicker ret() {
				// TODO Auto-generated method stub
				return new Line();
			}
		});
		MsgPackUnpack.setGetList(new IGetListPicker() {
			
			public IListPicker ret() {
				// TODO Auto-generated method stub
				return new Line();
			}
		});
		
	}
	
	private static void getFieldFromPackage(String packageName){
		Class<?> c;
		try {
			c = Class.forName(packageName);
		} catch (ClassNotFoundException e1) {
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