package org.chenye.andfree.db;

public class BaseTable extends Tables{
	public BaseTable() {
		Class<?> cls = getClass();
		setDBParse(cls);
	}
	
	public void init(Object obj) {
		Class<?> cls = obj.getClass();
		setDBParse(cls);
	}
}
