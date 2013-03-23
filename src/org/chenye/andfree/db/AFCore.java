package org.chenye.andfree.db;

public class AFCore extends baseCore{
	protected AFCore(){};
	public static class config extends AFTable{
		public static final DBField key = DBField.primaryText("key");
		public static final DBField value = DBField.text("value");
	}
	
	public static class ImageCache extends AFTable {
		public static final DBField _id = DBField.primaryText("_id");
		public static final DBField path = DBField.text("path");
		public static final DBField file = DBField.text("file");
		public static final DBField createTime = DBField.text("create_time");
	}
}
