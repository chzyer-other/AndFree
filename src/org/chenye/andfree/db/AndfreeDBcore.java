package org.chenye.andfree.db;

public class AndfreeDBcore extends baseCore{
	public final static class config extends BaseDBcore{
		public final static dbField key = dbField.primaryText("key");
		public final static dbField value = dbField.text("value");
	}
}
