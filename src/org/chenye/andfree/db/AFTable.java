package org.chenye.andfree.db;

public class AFTable extends Tables{
	public AFTable() {
		Class<?> cls = getClass();
		if ( ! cls.getSimpleName().equals("")) {
			setDBParse(cls);
			return;
		}

		String clsName = cls.getFields()[0].toGenericString();
		clsName = clsName.substring(clsName.indexOf('$') + 1);
		clsName = clsName.substring(0, clsName.indexOf('.'));

		setDBParse(clsName);
	}
	
	public void init(Object obj) {
		Class<?> cls = obj.getClass();
		setDBParse(cls);
	}
	
}
