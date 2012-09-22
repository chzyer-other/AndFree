package org.chenye.andfree.db;

import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Set;

import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.obj.Line;

import android.content.ContentValues;

public class dbParse extends BaseLog{
	public static final String FIELD_TEXT = "TEXT";
	public static final String FIELD_INT = "INTEGER";
	Field[] fields;
	Class<?> cs;
	String dbname;
	String[] columnsType;
	String[] columnsName;
	dbField[] columnsObj;
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
			Class<?> cs_package = Class.forName(AndfreeConf.PACKAGE_NAME + "._andfree.dbcore");
			return cs_package.getClasses();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
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
	
	public String[] getColumnsName(){
		if (columnsName != null) return columnsName;
		String[] cols = new String[fields.length];
		for (int i=0; i<cols.length; i++){
			cols[i] = fields[i].getName();
		}
		columnsName = cols;
		return cols;
	}
	
	public int findColumn(String key){
		getColumnsName();
		int i=0;
		for (String c:columnsName){
			if (c.equals(key)) return i;
			i++;
		}
		return -1;
	}
	
	public boolean containColumn(String key){
		return findColumn(key) < 0 ? false : true;
	}
	
	public dbField[] getColumnsObj(){
		if (columnsObj != null) return columnsObj;
		dbField[] objs = new dbField[fields.length];
		String[] names = getColumnsName();
		for(int i=0; i<objs.length; i++){
			dbField f;
			try {
				f = (dbField) fields[i].get(fields[i].getName());
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
		dbField[] objs = getColumnsObj();
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
		dbField[] objs = getColumnsObj();
		
		for(dbField obj:objs){
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
			d.put("`" + o.getKey() + "`", o.getValue() + "");
		}
		return d;
	}
	
	public void filter(ContentValues data){
		Set<Entry<String, Object>> iterator = data.valueSet();
		dbField[] f = getColumnsObj();
		for(Entry<String, Object> s:iterator){
			int pos = findColumn(s.getKey());
			if (pos < 0 || (f[pos].primary() && f[pos].isInt())){
				data.remove(s.getKey());
			}
		}
	}
	
}