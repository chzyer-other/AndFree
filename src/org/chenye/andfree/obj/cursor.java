package org.chenye.andfree.obj;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Map.Entry;

import org.chenye.andfree.db.dbParse;
import org.chenye.andfree.func.log;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;

public class cursor {
	public Cursor mc = null;
	public static final String CODING_ISO = "iso-8859-1";
	
	private int count = 0;
	
	String all_string = "";
	boolean failure = false;
	dbParse dbp;
	
	public cursor(){
		
	}
	
	public cursor(Cursor c){
		this.mc = c;
		count = length();
		if (c == null){
			e("get the null cursor ");
			failure = true;
			count = 0;
			return;
		}
		mc.moveToFirst();
		initKeyMapFromMc();
	}
	
	public boolean atEnd(){
		return mc.getPosition() >= count;
	}
	
	public void setDBParse(dbParse mDbp){
		dbp = mDbp;
	}
	
	public String _id(){
		return dbp.getPrimaryKey();
	}
	
	public int getId(){
		return getInt(_id());
	}
	
	boolean closed = false;
	public void close(){
		if (closed) return;
		closed = true;
		try{
			mc.close();
		} catch (Exception ex){
			
		}
	}
	
	public static boolean valid(cursor cur){
		return ! invalid(cur);
	}
	
	public static boolean invalid(cursor cur){
		if (cur == null) return true;
		boolean ret = cur.count <= 0;
		if (ret) cur.close();
		return ret;
	}
	
	public int length(){
		if (mc == null) return 0;
		return mc.getCount();
	}
	
	public cursor copyField(String oldfield, String newfield){
		put(newfield, getString(oldfield));
		return this;
	}
	
	public cursor moveField(String oldfield, String newfield){
		copyField(oldfield, newfield);
		remove(oldfield);
		return this;
	}
	
	public void moveAllField(String oldfield, String newfield){
		putAll(newfield, getString(oldfield));
		remove(oldfield);
	}
	
	ContentValues encoding = new ContentValues();
	public cursor encode(String field, String code){
		encoding.put(field, code);
		return this;
	}
	
	//-----------------------------------------------------------------------------------------------------------iterator
	int keyIndex = -1;
	ArrayList<String> keyMap = new ArrayList<String>();
	public boolean nextKey(){
		int max = keyMap.size();
		keyIndex++;
		if (keyIndex < max){
			return true;
		}
		return false;
	}
	
	public String getKey(){
		return keyMap.get(keyIndex);
	}
	
	void initKeyMap(){
		keyIndex = -1;
		for (Entry<String, Object> key:all_put.valueSet()){
			keyMap.add(key.getKey());
		}
	}
	
	void initKeyMapFromMc(){
		String[] keys = mc.getColumnNames();
		for (String key:keys){
			if (keyMap.contains(key)) continue;
			keyMap.add(key);
		}
	}
	//-----------------------------------------------------------------------------------------------------------remove
	
	ArrayList<String> tmp_del = new ArrayList<String>();
	public void remove(String key){
		if (tmp_del.contains(key)) return;
		beforeRemove(key);
		tmp_del.add(key);
	}
	
	void beforeRemove(String key){
		if (keyMap.contains(key)) keyMap.remove(key);
		if (tmp_put.containsKey(key)) tmp_put.remove(key);
	}
	
	//-----------------------------------------------------------------------------------------------------------put
	
	ContentValues tmp_put = new ContentValues();
	ContentValues all_put = new ContentValues();
	public void put(String field, int value){
		put(field, value + "");
	}
	
	public void put(String field, Long value){
		put(field, value + "");
	}
	public void put(String field, String value){
		beforePut(field);
		tmp_put.put(field, value);
	}
	public cursor putAll(String field, String value){
		beforePut(field);
		all_put.put(field, value);
		return this;
	}

	void beforePut(String field){
		if (tmp_del.contains(field)){
			tmp_del.remove(field);
		}
		if ( ! keyMap.contains(field)){
			keyMap.add(field);
		}
	}
	//-----------------------------------------------------------------------------------------------------------get
	
	public String getString(String field){
		if (invalid(this)) return null;
		if (firstNext) firstNext = false;
		if (tmp_put.containsKey(field)) return tmp_put.getAsString(field);
		if (all_put.containsKey(field)) return all_put.getAsString(field);
		if (tmp_del.contains(field)) return null;
		
		if (mc == null){
			e("got null mc");
			return null;
		}
		
		int FieldIndex = mc.getColumnIndex(field);
		if (FieldIndex >= 0){
			String ret = null;
			
			try{
				ret = mc.getString(FieldIndex);
			}catch(Exception ex){
				e("get data without use next? " + field);
			}
			return aftergetString(field, ret);
		}else{
			e(toString());
			e("can not find key: '" + field + "'");
			return null;
		}
		
	}
	
	public long getLong(String key) {
		String data = getString(key);
		if (data == null || data.length() <= 0) return 0;
		return Long.valueOf(data);
		
	}
	
	public int getInt(String key){
		String ret = getString(key);
		if (ret == null || ret.equals("")) return -1;
		try{
			return Integer.parseInt(ret);
		}catch(Exception ex){
			e(ex);
			return 0;
		}
	}
		
	void beforegetString(String field){
		
	}
	
	String aftergetString(String field, String data){
		//encode
		if (encoding.containsKey(field)){
			String code = encoding.getAsString(field);
			try {
				return new String(data.getBytes(code), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				return data;
			}
		}
		return data;
	}
	//-----------------------------------------------------------------------------------------------------------pos
	
	public void jump(int toIndex){
		if (index() < 0) return;
		first();
		while(index() != toIndex){
			next();
		}
	}
		
	public void first(){
		initKeyMap();
		firstNext = true;
		mc.moveToFirst();
	}
	
	public int index(){
		return mc.getPosition();
	}
	
	public boolean prev(){
		if (index() <= 0) return false;
		mc.moveToPrevious();
		return true;
	}
	
	boolean firstNext = true;
	public boolean next(){
		if (cursor.invalid(this)) return false;
		if (failure) return false;
		
		if (firstNext){
			firstNext = false;
			return true;
		}
		
		
		if (index() >= count - 1) return false;
		
		
		mc.moveToNext();
		tmp_put.clear();
		tmp_del.clear();
		initKeyMap();
		return true;
	}

	/**
	 * auto break when index = max
	 * @param max
	 * @return
	 */
	public boolean next(int max){
		if (index() >= max + prevIndex && max != -1) {
			setNextFlag();
			return false;
		}
		return next();
	}
	public void setNextFlag(){
		prevIndex = index();
	}
	int prevIndex = 0;
	
	//-----------------------------------------------------------------------------------------------------------helper
	
	boolean in_delete(String name){
		return tmp_del.contains(name);
	}
	
	
	Object tmp_firstNext;
	void beforeOutput(){
		tmp_firstNext = firstNext;
	}
	
	void afterOutput(){
		if (tmp_firstNext != null) {
			firstNext = (Boolean) tmp_firstNext;
			tmp_firstNext = null;
		}
		
	}
	
	
	int tmp_index = -1;
	void savePos(){
		tmp_index = index();
		first();
	}
	
	void loadPos(){
		if (tmp_index < 0) return;
		jump(tmp_index);
		tmp_index = -1;
	}
	
	//-----------------------------------------------------------------------------------------------------------pass data
	
	
	public void setContentvalues(ContentValues data, String[] fields){
		beforeOutput();
		for(String s:fields){
			data.put(s, getString(s));
		}
		afterOutput();
	}
	
	/**
	 * pass second cv to first cv with point fields
	 * @param data
	 * @param from
	 * @param fields
	 * @return
	 */
	public ContentValues setContentvalues(ContentValues data, ContentValues from, String[] fields){
		for(String s:fields){
			data.put(s, from.getAsString(s));
		}
		return data;
	}
	
	public void setIntent(Intent intent, String[] field){
		beforeOutput();
		for (int i=0; i<field.length; i++){
			intent.putExtra(field[i], getString(field[i]));
		}
		afterOutput();
	}
	
	
	//-----------------------------------------------------------------------------------------------------------search
	
	public boolean find(String key, String value){
		boolean ret = false;
		first();
		while(next()){
			if (getString(key).equals(value)){
				ret = true;
				break;
			}
		}
		return ret;
	}
	
	public boolean findNot(String key, String value){
		boolean ret = false;
		first();
		while(next()){
			if ( ! getString(key).equals(value)){
				loadPos();
				ret = true;
			}
		}
		return ret;
	}
	
	//-----------------------------------------------------------------------------------------------------------output
	/**
	 * get a ContentValues using a keyField and a dataField
	 * @param keyField
	 * @param dataField
	 * @return
	 */
	public ContentValues toContentValue(String keyField, String dataField){
		savePos();
		ContentValues data = new ContentValues();
		while(next()){
			data.put(getString(keyField), getString(dataField));
		}
		loadPos();
		return data;
	}

	/**
	 * convert all row to ContentValue
	 * @return
	 */
	public ContentValues[] toContentValues(){
		ContentValues[] result = new ContentValues[count];
		savePos();
		try{
			while (next()){
				result[index()] = toContentValue();
			}
		}catch(Exception e){
			e(e);
		}
		loadPos();
		return result;
	}
	
	public Line toLineRow(){
		Line row = new Line(dbp);
		for (int i = 0; i<mc.getColumnCount(); i++){
			if (in_delete(mc.getColumnName(i))) continue;
			row.put(mc.getColumnName(i), getString(mc.getColumnName(i)));
		}
		return row;
	}
	
	public Line toLine(){
		Line l = new Line(dbp);
		savePos();
		
		while (next()){
			l.put(toLineRow());
		}
		
		loadPos();
		return l;
	}
	
	/**
	 * get ContentValues in point field
	 * @param fields
	 * @return
	 */
	public ContentValues toContentValue(String[] fields){
		ContentValues result = new ContentValues();
		for (String s: fields){
			result.put(s, getString(s));
		}
		return result;
	}
	
	public ContentValues toContentValue(){
		ContentValues result = new ContentValues();
		for (int i =0; i<mc.getColumnCount(); i++){
			if (in_delete(mc.getColumnName(i))) continue;
			result.put(mc.getColumnName(i), getString(mc.getColumnName(i)));
		}
		return result;
	}
	
	/**
	 * print all row data to json
	 * @return
	 */
	public cursor print(){
		if (dbp != null && count <= 0) {
			e("tables " + dbp.getName() + " is empty");
			return this;
		}
		
		savePos();
		if (dbp != null) log.d(this, ">> start print table " + dbp.getName() + "(count:" + count + ")");
		while(next()){
			log.i(this);
		}
		loadPos();
		return this;
	}
	
	public String toString(){
		if (count <= 0) return "{null}";
		beforeOutput();
		String a = "{";
		
		ContentValues tmp = new ContentValues();
		tmp.putAll(tmp_put);
		tmp.putAll(all_put);
		String tmpString = tmp.toString(); 
		if (tmpString.length() > 0) a += tmpString.substring(1, tmpString.length() - 1) + ",";
		
		for (int i=0; i<mc.getColumnCount(); i++){
			String field = mc.getColumnName(i);
			if (in_delete(field)) continue;
			a += field + ":" + getString(field) + ",";
		}
		a = a.substring(0, a.length()-1) + "}";
		
		afterOutput();
		return a;
	}
	
	/**
	 * get implode string from all data's field. 
	 * eg. '11', '22'
	 * @param field
	 * @param format
	 * @return
	 */
	public String getFormatString(String field, String format){
		if (count <= 0) return "";
		String all_string = "";
		
		while(next()){
			String data = getString(field);
			all_string += "," + format.replace("%s", data);
		}
		if (all_string.length() > 0) all_string = all_string.substring(1);
		return all_string;
	}
	
	/**
	 * get a field data from all rows
	 * @param field
	 * @return String[]
	 */
	public String[] toStringArray(String field){
		first();
		String[] n = new String[count];
		while(next()){
			n[index() - 1] = getString(field);			
		}
		first();
		return n;
	}
	
	/**
	 * get data in a row by field
	 * @param field
	 * @return
	 */
	public String[] toStringArray(String[] field){
		String[] result = new String[field.length];
		for (int i=0; i<field.length; i++){
			result[i] = getString(field[i]);
		}
		return result;
	}
	
	//-----------------------------------------------------------------------------------------------------------db
	
	
	private void e(String str){
		log.e(this, str);
	}
	
	private void e(Exception ex){
		log.e(this, ex);
	}
}
