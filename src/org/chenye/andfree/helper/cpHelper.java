package org.chenye.andfree.helper;

import java.util.Hashtable;

import org.chenye.andfree.db.DB;
import org.chenye.andfree.func.log;
import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.cursor;

import android.content.ContentResolver;
import android.net.Uri;

public class cpHelper extends BaseLog{
	private static final String TYPE_SMS = "content://sms";
	private static final String TYPE_THREAD = "content://thread"; // canonical_addresses
	private static final String TYPE_MMS = "content://mms";
	private static final String TYPE_MMS_PART = "content://mms/part";
	private static final String TYPE_CONTACT_PHONE = "content://contacts/phones";
	private static final String TYPE_CONTACT = "content://com.android.contacts/contacts";
	
	private static Hashtable<String, cpHelper> instance = new Hashtable<String, cpHelper>();
	private Line _queryData = Line.def();
	public static cpHelper SMS(){
		return getInstance(TYPE_SMS, "sms");
	}
	
	public static cpHelper MMS(){
		return getInstance(TYPE_MMS, "pdu");
	}
	
	public static cpHelper MMS_PART(){
		return getInstance(TYPE_MMS_PART, "parts");
	}
	
	public static cpHelper THREAD(){
		return getInstance(TYPE_THREAD, "threads");
	}
	
	public static cpHelper CONTACT_PHONE(){
		return getInstance(TYPE_CONTACT_PHONE, "phones");
	}
	
	public static cpHelper CONTACT(){
		return getInstance(TYPE_CONTACT, "contact");
	}
	
	private static cpHelper getInstance(String type, final String table_name){
		if (instance.containsKey(type)) return instance.get(type);
		instance.put(type, new cpHelper(type){{
			currentTable = table_name;
		}});
		return getInstance(type, table_name);
	}
	

	private ContentResolver c;
	String uri_string;
	Uri type_uri;
	String virtualTable;
	String currentTable = "";
	public cpHelper(String mType){
		mType = formatUri(mType);
		uri_string = mType;
		type_uri = Uri.parse(uri_string);
		c = DB.getInstance().getContext().getContentResolver();
	}
	
	private String formatUri(String type){
		if (type.equals(TYPE_THREAD)){
			virtualTable = "threads";
			currentTable = "threads";
			return TYPE_SMS;
		}
		return type;
	}
	
	public cpHelper distinct(String... fields){
		fields[0] = "DISTINCT " + fields[0];
		return select(fields);
	}
	
	public cpHelper select(String... fields){
		String str = "";
		for (int i=0; i<fields.length; i++){
			str += "," + fields[i];
		}
		if (str.length() > 0) str = str.substring(1);
		_queryData.put("select", str);
		return this;
	}

	public cpHelper where(String where, Object... args){
		_queryData.put("where", String.format(where, args));
		return this;
	}

	public cpHelper order(String field){
		return order(field, "DESC");
	}
	
	public cpHelper order(String field, String sort){
		_queryData.put("order", field + " " + sort);
		return this;
	}
	
	public cpHelper limit(int count){
		return limit("0, " + count);
	}
	
	public cpHelper limit(String limit_string){
		_queryData.put("limit", limit_string);
		return this;
	}
	
	public cpHelper join(String table, String condition){
		_queryData.put("join", String.format(" %s %s ON %s", 
				table, table.substring(0, 1), condition
		));
		return this;
	}

	public Line get(){
		limit(1);
		return result().line(0);
	}
	
	public String getWithField(){
		return get().str(_queryData.str("select"));
	}
	
	public Line getId(long id){
		where("_id = %s", id);
		return get();
	}
	
	public Line result(){
		Line l = Line.def();
		if (virtualTable != null || _queryData.contains("join")){
			l = result_sql();
		} else {
			l = result_normal();
		}
		_queryData.clear();
		return l;
	}
	
	public int count(){
		_queryData.put("select", "COUNT(*) as count");
		return get().integer("count");
	}
	
	private Line result_sql(){
		_queryData.putIfNotExist("select", "*");
		String tables = virtualTable == null ? currentTable : virtualTable;
		String sql = String.format("%s FROM %s %s", 
				_queryData.str("select"), tables, tables.substring(0, 1));
		if (_queryData.contains("join")){
			sql += " LEFT JOIN " + _queryData.str("join");
		}
		_queryData.putIfNotExist("where", "1=1");
		_queryData.putIfNotExist("order", tables.substring(0, 1) + "._id DESC");
		sql += String.format(" WHERE %s ORDER BY %s", _queryData.str("where"), _queryData.str("order"));
		if (_queryData.contains("limit")){
			sql += " LIMIT " + _queryData.str("limit");
		}
		
		cursor cur = null;
		try{
			cur = new cursor(
				c.query(type_uri, new String[] {sql + " --"}, null, null, null)
			);
		} catch(Exception ex){
			error(ex);
		}
		if (cursor.invalid(cur)) return Line.def();
		Line l = cur.toLine();
		cur.close();
		return l;
	}
	
	private Line result_normal(){
		String[] projection = null;
		if (_queryData.contains("select")){
			projection = _queryData.str("select").split(",");
		}
		
		String selection = null;
		if (_queryData.contains("where")){
			selection = _queryData.str("where");
		}
		
		String sortOrder = "_id DESC";
		if (_queryData.contains("order")){
			sortOrder = _queryData.str("order");
		}
		
		if (_queryData.contains("limit")){
			sortOrder += " LIMIT " + _queryData.str("limit");
		}
		
		cursor cur = null;
		try{
		cur = new cursor(
			c.query(type_uri, projection, selection, null, sortOrder)
		);
		} catch (Exception ex) {
			log.e(this, ex);
		}
		if (cursor.invalid(cur)) return Line.def();
		Line l = cur.toLine();
		cur.close();
		return l;
	}
	
	public long update(String field, String value, String selection){
		return update(new Line().put(field, value), selection);
	}
	
	public long update(Line line){
		if (line.invalid()) return -1;
		return updateId(line, line.integer("_id"));
	}
	
	public long updateId(Line line, int id){
		return update(line, "_id = " + id);
	}
	
	public long update(Line Line, String where){
		return c.update(type_uri, Line.toContentValue(), where, null);
	}
	
	public void delete(Line obj){
		if (obj.invalid()) return;
		if (uri_string.equals(TYPE_SMS)){
			
			String where = "";
			if (obj.contains("_id")) where = "_id = " + obj.integer("_id");
			if (obj.contains("where")){
				if (obj.str("where").equals("all")){
					where = "_id > 0";
				} else {
					where = obj.str("where");
				}
			}
			c.delete(Uri.parse("content://sms/conversations/" + obj.str("thread_id")), where, null);
		}
		
	}
	
	public long insert(Line obj){
		String u = c.insert(type_uri, obj.toContentValue()).toString();
		return Long.valueOf(u.substring(u.lastIndexOf("/") + 1));
	}
	
	public void e(Exception str){
		log.e(this, str);
	}
	
	public void e(String str){
		log.e(this, str);
	}
}
