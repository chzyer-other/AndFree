package org.chenye.andfree.db;

import org.chenye.andfree.obj.Line;

public class dbField{
	private int t = 0; // 0:null, 1: text, 2:integer
	private String type;
	private int len;
	private boolean primary;
	private String def;
	private String name;
	dbField(String name, String type, int len, boolean primary, String def){
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
	dbField(String name, String type, int len, boolean primary){
		this(name, type, len, primary, null);
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String v(int i){
		return "`" + name + "` = " + i;
	}
	
	public String v(String i){
		if (i.startsWith(">") || i.startsWith("<")){
			return "`" + name + "` " + i;
		}
		return "`" + name + "` = '" + i + "'";
	}
	
	public String in(Line data){
		return "`" + name + "` in (" + data.toSQL() + ")";
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
	
	
	public static dbField primaryText(String name){
		return primaryText(name, -1);
	}
	
	public static dbField primaryText(String name, int len){
		return text(name, len, true);
	}
	
	public static dbField text(String name){
		return text(name, -1);
	}
	
	public static dbField text(String name, int len, boolean primary){
		return new dbField(name, "TEXT", len, primary);
	}
	public static dbField dateline(String name){
		return text(name, 13);
	}
	
	public static dbField text(String name, int len){
		return text(name, len, false);
	}
	
	public static dbField integer(String name, boolean primary){
		return integer(name, -1, null, primary);
	}
	
	public static dbField integer(String name, int defaults){
		return integer(name, -1, "" + defaults, false);
	}
	public static dbField integer(String name, int len, String defaults){
		return integer(name, len, defaults, false);
	}
	
	public static dbField integer(String name, String defaults){
		return integer(name, -1, defaults, false);
	}
	
	public static dbField integer(String name, int len, boolean primary){
		return integer(name, len, null, primary);
	}
	
	public static dbField integer(String name, int len, String def, boolean primary){
		return new dbField(name, "INTEGER", len, primary, def);
	}
	
	public static dbField integer(String name){
		return integer(name, -1);
	}
	
	public static dbField obj(String name){
		return integer(name, 8);
	}
	
	public static dbField obj(String name, dbField field){
		return integer(name, 8);
	}
	
	public static dbField boolInt(String name){
		return boolInt(name, 0);
	}
	
	public static dbField boolInt(String name, int defaults){
		return integer(name, 1, defaults + "");
	}
	
	public static dbField primaryInt(String name){
		return primaryInt(name, 8);
	}
	
	public static dbField primaryInt(String name, int len){
		return integer(name, len, true);
	}
	
	public boolean primary(){
		return primary;
	}
}