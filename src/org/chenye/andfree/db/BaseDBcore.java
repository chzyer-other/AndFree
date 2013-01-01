package org.chenye.andfree.db;

public class BaseDBcore extends baseCore{
	public static class config extends BaseTable{
		public static final dbField key = dbField.primaryText("key");
		public static final dbField value = dbField.text("value");
	}
	
	public static class ImageCache extends BaseTable {
		public static final dbField _id = dbField.primaryText("_id");
		public static final dbField path = dbField.text("path");
		public static final dbField file = dbField.text("file");
		public static final dbField createTime = dbField.text("create_time");
	}
}
