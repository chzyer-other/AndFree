package org.chenye.andfree.obj;


import org.chenye.andfree.func.FuncTime;

public class AFLogObj {
	public void log(Object obj){
		AFLog.i(this, obj);
	}
	
	public void error(Object obj){
		AFLog.e(this, obj);
	}
	
	public void d(String obj) {
		AFLog.d(this, obj);
	}
	
	public static void error(Object obj, Object msg){
		AFLog.e(obj, msg);
	}
	
	public static void log(Object obj, Object msg){
		AFLog.i(obj, msg);
	}

	long _time;
	public void setTimeFlag(){
		_time = FuncTime.time();
	}
	public long getTimeFlag(){
		return FuncTime.time() - _time;
	}
	public long getTimeFlagAndClear(){
		long t = _time;
		_time = FuncTime.time();
		return _time - t;
	}
	public void logTimeFlag(){
		log("spend time: " + getTimeFlag());
	}
	public void logTimeFlagAndClear(){
		log("spend time: " + getTimeFlagAndClear());
	}
}
