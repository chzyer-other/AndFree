package org.chenye.andfree.db;

import org.chenye.andfree.obj.Line;

public class DBField{
	private int t = 0; // 0:null, 1: text, 2:integer
	private String type;
	private int len;
	private boolean primary;
	private String def;
	private String name;
	DBField(String name, String type, int len, boolean primary, String def){
		if (type.equals("TEXT")){
			t = 1;
		}else if (type.equals("INTEGER")){
			t = 2;
		}
		this.type = type;
		this.len = len;
		this.primary = primary;
		this.def = def;
		this.name = name;
	}
	DBField(String name, String type, int len, boolean primary){
		this(name, type, len, primary, null);
	}
	
	public String ASC(){
		return String.format("`%s` ASC", name);
	}
	
	public String DESC(){
		return String.format("`%s` DESC", name);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String like(String field){
		return " AND `" + name + "` LIKE '" + field + "'";
	}
	
	public String orLike(String field){
		return " OR " + like(field).substring(5);
	}
	
	public String equal(boolean i){
		return equal(i ? 1 : 0);
	}
	
	public String equal(int i){
		return " AND `" + name + "` = " + i;
	}
	
	public String notEqual(int i){
		return " AND `" + name + "` != " + i;
	}

	public String equal(String i){
		return " AND `" + name + "` = '" + i + "'";
	}
	
	public String bigger(int i){
		return " AND `" + name + "` > " + i + "";
	}
	
	public String smaller(int i){
		return " AND `" + name + "` < " + i + "";
	}
	
	public String in(Line data){
		return " AND `" + name + "` in (" + data.toSQL() + ")";
	}
	
	public String orEquals(int i){
		return " OR " + equal(i).substring(5);
	}
	
	public String orEquals(boolean i){
		return orEquals(i ? 1 : 0);
	}
	
	public String orEquals(String i){
		return " OR " + equal(i).substring(5);
	}
	
	public String orIn(Line data){
		return " OR " + in(data).substring(5);
	}
	
	public String orBigger(int i){
		return " OR " + bigger(i).substring(5);
	}
	
	public String orSmaller(int i){
		return " OR " + smaller(i).substring(5);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public String getName(){
		return name;
	}
	
	public boolean isInt(){
		return t == 2;
	}
	
	public String type(){
		String t = type;
		if (len > 0 && ! primary){
			t += " (" + len + ")";
		}
		if (primary){
			t += " PRIMARY KEY";
		}else if (def != null && def.length() > 0){
			String[] format = {"%s", "'%s'"};
			t += " DEFAULT " + String.format(format[t.startsWith("INTEGER") ? 0 : 1], def);
			
		}
		return t;
	}
	
	
	public static DBField primaryText(String name){
		return primaryText(name, -1);
	}
	
	public static DBField primaryText(String name, int len){
		return text(name, len, true);
	}
	
	public static DBField text(String name){
		return text(name, -1);
	}
	
	public static DBField text(String name, int len, boolean primary){
		return new DBField(name, "TEXT", len, primary);
	}
	public static DBField dateline(String name){
		return text(name, 13);
	}
	
	public static DBField text(String name, int len){
		return text(name, len, false);
	}
	
	public static DBField integer(String name, boolean primary){
		return integer(name, -1, null, primary);
	}
	
	public static DBField integer(String name, int defaults){
		return integer(name, -1, "" + defaults, false);
	}
	public static DBField integer(String name, int len, String defaults){
		return integer(name, len, defaults, false);
	}
	
	public static DBField integer(String name, String defaults){
		return integer(name, -1, defaults, false);
	}
	
	public static DBField integer(String name, int len, boolean primary){
		return integer(name, len, null, primary);
	}
	
	public static DBField integer(String name, int len, String def, boolean primary){
		return new DBField(name, "INTEGER", len, primary, def);
	}
	
	public static DBField integer(String name){
		return integer(name, -1);
	}
	
	public static DBField obj(String name){
		return integer(name, 8);
	}
	
	public static DBField obj(String name, DBField field){
		return integer(name, 8);
	}
	
	public static DBField boolInt(String name){
		return boolInt(name, 0);
	}
	
	public static DBField boolInt(String name, int defaults){
		return integer(name, 1, defaults + "");
	}
	
	public static DBField primaryInt(String name){
		return primaryInt(name, 8);
	}
	
	public static DBField primaryInt(String name, int len){
		return integer(name, len, true);
	}
	
	public boolean primary(){
		return primary;
	}
}