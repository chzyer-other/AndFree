package org.chenye.andfree.obj;


public class AFLogActivity {
	public void log(Object obj){
		AFLog.i(this, obj);
	}
	
	public void error(Object obj){
		AFLog.e(this, obj);
	}
	
	public static void error(Object obj, Object msg){
		AFLog.e(obj, msg);
	}
	
	public static void log(Object obj, Object msg){
		AFLog.i(obj, msg);
	}
}
