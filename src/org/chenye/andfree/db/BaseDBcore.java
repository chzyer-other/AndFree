package org.chenye.andfree.db;

public class BaseDBcore extends baseCore{
	public static class config extends BaseTable{
		public static final dbField key = dbField.primaryText("key");
		public static final dbField value = dbField.text("value");
	}
}
