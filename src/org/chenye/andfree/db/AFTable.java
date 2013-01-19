package org.chenye.andfree.db;

public class AFTable extends Tables{
	public AFTable() {
		Class<?> cls = getClass();
		setDBParse(cls);
	}
	
	public void init(Object obj) {
		Class<?> cls = obj.getClass();
		setDBParse(cls);
	}
	
}
