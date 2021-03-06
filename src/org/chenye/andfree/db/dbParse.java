package org.chenye.andfree.db;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Set;

import org.chenye.andfree.conf.AndfreeStaticConfigure;
import org.chenye.andfree.obj.AFLogObj;
import org.chenye.andfree.obj.Line;

import android.content.ContentValues;

public class dbParse extends AFLogObj{
	public static final String FIELD_TEXT = "TEXT";
	public static final String FIELD_INT = "INTEGER";
	Field[] fields;
	Class<?> cs;
	String dbname;
	String[] columnsType;
	String[] columnsName;
	DBField[] columnsObj;
	private String primaryKey = null;
	public dbParse(Class<?> cls){
		dbname = cls.getSimpleName();
		cs = cls;
		fields = cs.getFields();
	}
	
	public dbParse(String dbname){
		this.dbname = dbname;
		
		Class<?>[] all_cs = all();
		if (all_cs == null) {
			error("error package");
			return;
		}
		
		for(int i=0; i<all_cs.length; i++){
			if (dbname.equals(all_cs[i].getSimpleName())){
				cs = all_cs[i];
				break;
			}
		}
		if (cs == null) return;
		fields = cs.getFields();
	}
	
	public boolean invalid(){
		return cs == null;
	}
	
	public void setPrimaryId(String field){
		primaryKey = field;
	}
	
	public Class<?> getTableClass(){
		return cs;
	}
	
	public static Class<?>[] all(){
		try {
			Class<?> cs_package = Class.forName(AndfreeStaticConfigure.DBCORE_PACKAGE);
			return cs_package.getClasses();
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	public static void InitAll() {
		Class<?>[] clses = all();
		for (Class<?> cls: clses) {
			for (Field f: cls.getFields()){
				try {
					((DBField) f.get(null)).setDatabaseName(cls.getSimpleName());
				} catch (IllegalAccessException e) {

				}
			}
		}
	}
	
	public String getId(){
		return getPrimaryKey();
	}
	
	public String getType(String field){
		String[] type = getColumnsType();
		String[] name = getColumnsName();
		if ( ! containColumn(field)) return null;
		for (int i=0; i<name.length; i++){
			if (name[i].equals(field)){
				return type[i];
			}
		}
		return null;
	}
	
	/** get table name */
	public String getName(){
		return cs.getSimpleName();
	}
	
	public String[] getColumnsName() {
		if (columnsName != null) return columnsName;
		String[] cols = new String[fields.length];
		for (int i=0; i<cols.length; i++){
			cols[i] = fields[i].getName();
		}
		columnsName = cols;
		return cols;
	}
	
	public int findColumn(String key){
		getColumnsObj();
		int i=0;
		for (DBField c:columnsObj){
			if (c.getName().equals(key)) return i;
			i++;
		}
		return -1;
	}
	
	public boolean containColumn(String key){
		return findColumn(key) < 0 ? false : true;
	}
	
	public DBField[] getColumnsObj(){
		if (columnsObj != null) return columnsObj;
		DBField[] objs = new DBField[fields.length];
		String[] names = getColumnsName();
		for(int i=0; i<objs.length; i++){
			DBField f;
			try {
				f = (DBField) fields[i].get(fields[i].getName());
			} catch (IllegalArgumentException e) {
				continue;
			} catch (IllegalAccessException e) {
				continue;
			}
			f.setName(names[i]);
			objs[i] = f;
		}
		columnsObj = objs;
		return objs;
	}
	
	public String[] getColumnsType(){
		if (columnsType != null) return columnsType;
		DBField[] objs = getColumnsObj();
		String[] cols = new String[objs.length];
		for(int i=0; i<objs.length; i++){
			cols[i] = objs[i].type();
		}
		columnsType = cols;
		return cols;
	}
	
	public int getColumnsNum(){
		return getColumnsName().length;
	}
	
	public String getPrimaryKey(){
		if (primaryKey != null) return primaryKey;
		DBField[] objs = getColumnsObj();
		
		for(DBField obj:objs){
			if (obj.primary()){
				primaryKey = obj.getName();
				break;
			}
		}
		return primaryKey;
	}
	
	public ContentValues filter(Line data){
		ContentValues d = new ContentValues();
		for (Entry<Object, Object> o:data.valueSet()){
			Object obj = o.getValue();
			String key = "`" + o.getKey() + "`";
			if (obj instanceof Boolean){
				d.put(key, ((Boolean) obj) ? 1 : 0);
			} else if (obj instanceof Integer){
				d.put(key, (Integer) obj);
			} else {
				d.put(key, obj + "");
			}
		}
		return d;
	}
	
	public void filter(ContentValues data){
		Set<Entry<String, Object>> iterator = data.valueSet();
		DBField[] f = getColumnsObj();
		for(Entry<String, Object> s:iterator){
			int pos = findColumn(s.getKey());
			if (pos < 0 || (f[pos].primary() && f[pos].isInt())){
				data.remove(s.getKey());
			}
		}
	}

	public DBField getPrimaryKeyObj() {
		DBField[] objs = getColumnsObj();

		for(DBField obj:objs){
			if (obj.primary()){
				return obj;
			}
		}
		return null;
	}
}