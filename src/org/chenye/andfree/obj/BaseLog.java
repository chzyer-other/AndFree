package org.chenye.andfree.obj;

import org.chenye.andfree.func.log;

public class BaseLog {
	public void log(Object obj){
		log.i(this, obj);
	}
	
	public void error(Object obj){
		log.e(this, obj);
	}
	
	public static void error(Object obj, Object msg){
		log.e(obj, msg);
	}
	
}
