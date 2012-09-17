package org.chenye.andfree.db;

import org.chenye.andfree.obj.Line;

public class baseCore {
	public static Line line;

	public static void use(Line mline){
		line = mline;
	}
	
	public static void remove(){
		line = null;
	}
}
