package org.chenye.andfree.obj;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.chenye.andfree.db.Tables;
import org.chenye.andfree.db.DBField;
import org.chenye.andfree.db.dbParse;
import org.chenye.andfree.func.FuncTime;
import org.chenye.andfree.msgpack.IDictPicker;
import org.chenye.andfree.msgpack.IListPicker;
import org.chenye.andfree.msgpack.MsgPackByte;
import org.chenye.andfree.msgpack.MsgPackUnpack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Line implements Iterable<Line>, IDictPicker, IListPicker{
	public static final int OBJ = 0;
	public static final int ARRAY = 1;
	
	public static final int OBJ_LEFT = 123;
	public static final int ARRAY_LEFT = 91;
	public static final int ARRAY_RIGHT = 93;
	public static final int OBJ_RIGHT = 125;
	public static final int COMMA = 44;
	public static final int QUOT = 34;
	public static final int SQUOT = 39;
	public static final int BACKSLASH = 92;
	public static final int COLON = 58;
	
	private boolean showString = false;
	private boolean debug_string = true;
	public String _str;

	public boolean isValid(String field) {
		if ( ! contains(field)) return false;
		Object obj = get(field);
		if (obj == null) return false;
		String str = obj.toString().trim();
		if (str.length() == 0) return false;
		if (str.equals("null")) return false;
		return true;
	}

	public Line split(int max) {
		Line l = new Line();
		for (int i = 0; i < max; i++) {
			l.put(get(i));
		}
		return l;
	}

	public boolean isValid(DBField thumbnail) {
		return isValid(thumbnail.getName());
	}

	private class nullClass{
		@Override
		public String toString() {
			return "null";
		}
	}
	String dataString;
	int type = -1;
	Map<Object, Object> data = new HashMap<Object, Object>();
	ArrayList<Object> array = new ArrayList<Object>();
	dbParse dbp;
	public Line(){
		type = -1;
	}

    public Line(byte[] bb) {
        this(new String(bb));
    }
	
	public static Line fromMsgPack(byte[] b){
		Object obj = MsgPackUnpack.parse(b);
		if (obj == null){
			return null;
		}
		return (Line) obj;
	}
	
	public byte[] toMsgPack(){
		MsgPackByte b = new MsgPackByte();
		if (isArray()){
			b.putList(this);
		} else {
			b.putDict(this);
		}
		return b.buffer().array();
	}
	
	public Line reverse(){
		Collections.reverse(array);
		return this;
	}
	
	public ArrayList<String> getSortKeys(){
		ArrayList<String> keys= new ArrayList<String>();
		for (Object o: data.keySet()){
			keys.add(o + "");
		}
		Collections.sort(keys);
		return keys;
	}
	
	public Class<?> getTableClass(){
		return dbp.getTableClass();
	}
	
	public static Line def(){
		return new Line();
	}

    public static Line PutArray(Object ...objs) {
        Line line = new Line();
        for (Object obj: objs) {
            line.put(obj);
        }
        return line;
    }

    public static Line Put(Object obj) {
        return def().put(obj);
    }

	public static Line Put(Object key, String ret){
		return def().put(key, ret);
	}
	
	public static Line Put(Object key, int ret){
		return def().put(key, ret);
	}
	
	public static Line Put(Object key, boolean ret){
		return def().put(key, ret);
	}
	
	public static Line PutId(int _id) {
		return def().put("_id", _id);
	}
	
	public int id(){
		return integer(_field());
	}
	
	public Line(Intent intent){
		this(intent.getDataString());
	}
	
	public Line(Class<?> cls){
		this();
		setTable(cls);
		if (showString) toString();
	}
	
	public Line(dbParse mdbp){
		this();
		dbp = mdbp;
	}
	
	public Line(Map<Object, Object> map){
		this();
		data = map;
		type = OBJ;
		if (showString) toString();
	}
	
	public Line(ArrayList<Object> a){
		this();
		array = a;
		type = ARRAY;
		if (showString) toString();
	}
	
	public Line(String str){
		str = str.trim();
		if ( ! isLine(str)) return;
		type = str.startsWith("{") ? OBJ : ARRAY;
		//implementObj(str);
		try {
			if (type == OBJ){
				formatJSONObject(new JSONObject(str), false);
			} else if (type == ARRAY) {
				formatJSONArray(new JSONArray(str), false);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (showString) toString();
		//i("]".charAt(0) + 1 - 1);
	}
	
	Line formatJSONObject(JSONObject obj, boolean buildnew) throws JSONException{
		Line l = null;
		if (buildnew) {
			l = new Line();
		}
		for (Iterator<?> iter = obj.keys(); iter.hasNext();) { //先遍历整个 people 对象
			String key = (String) iter.next();
			Object o = obj.get(key);
			if (o == null){
				o = new nullClass();
			} else if (o instanceof JSONArray){ 
				o = formatJSONArray((JSONArray) o, true);
			} else if (o instanceof JSONObject){
				o = formatJSONObject((JSONObject) o, true);
			}
			
			if (buildnew){
				l.put(key, o);
			} else {
				put(key, o);
			}
		}
		
		if (buildnew) {
			return l;
		}
		
		return this;
	}
		
	Line formatJSONArray(JSONArray obj, boolean buildnew) throws JSONException{
		Line l = null;
		if (buildnew) {
			l = new Line();
		}
		
		for (int i=0; i<obj.length(); i++){
			Object o = obj.get(i);
			if (o == null){
				o = new nullClass();
			} else if (o instanceof JSONArray){
				o = formatJSONArray((JSONArray) o, true);
			} else if (o instanceof JSONObject){
				o = formatJSONObject((JSONObject) o, true);
			}
			
			if (buildnew){
				l.put(o);
			} else {
				put(o);
			}
		}
		
		if (buildnew) {
			return l;
		}
		
		return this;
	}
	
	
	public boolean valid(){
		return ! invalid();
	}
	
	public boolean invalid(){
		return getType() == -1;
	}
	
	public Line setTable(Class<?> cls){
		return setTable(new dbParse(cls));
	}
	
	public Line setTable(dbParse mdbp){
		dbp = mdbp;
		return this;
	}
	
	public static boolean isLine(String str){
		str = str.trim();
		if (str.startsWith("{") && str.endsWith("}")) {
			return true;
		}
		if (str.startsWith("[") && str.endsWith("]")) {
			return true;
		}
		return false;
	}
	
	
	protected void implementObj(String str){
		// FIXME NOT USE
		String lines = str;
		int obj_start = 0;
		boolean startCollect = false;
		StringBuilder field = new StringBuilder();
		char lastchar = 0;
		boolean inQuot = false;
		boolean readedField = false;
		StringBuffer value = new StringBuffer();
		for (int i=0; i<lines.length(); i++){
			char c = lines.charAt(i);
			if (c == QUOT && lastchar != BACKSLASH) {
				inQuot = ! inQuot;
			}
			
			if (startCollect && inQuot){
				if ( ! readedField && type == OBJ){
					field.append(c);
				} else {
					value.append(c);
				}
			} else if (c == OBJ_LEFT || c == ARRAY_LEFT){
				obj_start++;
				if (obj_start == 1) startCollect = true;
				else value.append(c);
			} else if (c == OBJ_RIGHT || c == ARRAY_RIGHT) {
				obj_start--;
				if (obj_start == 0) {
					startCollect = false;
					addData(field, value);
				} else {
					value.append(c);
				}
			} else if (c == COLON && ! inQuot) {
				if (obj_start > 1) {
					value.append(c);
				} else {
					readedField = true;	
				}
			} else if (c == COMMA && ! inQuot) {
				if (obj_start > 1) {
					value.append(c);
				} else {
					readedField = false;
					addData(field, value);
				}
			} else if (startCollect){
				if ( ! readedField && type == OBJ){
					field.append(c);
				} else {
					value.append(c);
				}
			}
			lastchar = c;
		}
	}
	
	
	public Line include(Line l){
		if (l.invalid()) return this;
		if (l.getType() == OBJ){
			for (Entry<Object, Object> v:l.valueSet()){
				put(v.getKey(), v.getValue());
			}
		} else if (l.getType() == ARRAY){
			for (Line ll:l){
				put(ll);
			}
		}
		return this;
	}
	
	public Line putIfNotExist(Object keyObj, String value){
		if (contains(keyObj)) return this;
		return put(keyObj, value);
	}
	
	public Line putIfNotExist(Object keyObj, Line value){
		if (contains(keyObj)) return this;
		return put(keyObj, value);
	}
	
	public Line put(Object keyObj, String value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(String value){
		array.add(value);
		return this;
	}
	
	public Line put(long value){
		array.add(value);
		return this;
	}
	
	public Line putUni(Object obj){
		if (contains(obj)) return this;
		put(obj);
		return this;
	}

	public Line putNull(Object keyObj){
		String key = keyObj + "";
		if (key.length() == 0) return putNull();
		data.put(key, new nullClass());
		return this;
	}
	
	public Line putNull(){
		array.add(new nullClass());
		return this;
	}
	
	public Line put(Object keyObj, long value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Object keyObj, Integer value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Integer value){
		array.add(value);
		return this;
	}
	
	public Line put(Object keyObj, Boolean value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Boolean value){
		array.add(value);
		return this;
	}
	
	public Line put(Object keyObj, Double value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Double value){
		array.add(value);
		return this;
	}
	
	public Line put(Object value){
		array.add(value);
		return this;
	}
	
	public Line put(Object keyObj, Object value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Object keyObj, Line value){
		String key = keyObj + "";
		if (key.length() == 0) return put(value);
		data.put(key, value);
		return this;
	}
	
	public Line put(Line value){
		array.add(value);
		return this;
	}
	
	public Line remove(String key){
		data.remove(key);
		return this;
	}
	
	public Line remove(int index){
		array.remove(index);
		return this;
	}
	
	@SuppressWarnings("unchecked")
	public Line clone(){
		Line l = null;
		if (getType() == OBJ) {
			l = new Line((Map<Object, Object>) ((HashMap<Object, Object>) data).clone());
		} else if (getType() == ARRAY) {
			l = new Line(((ArrayList<Object>) array.clone()));
		}
		if (l == null) return null;
		l.setTable(dbp);
		
		return l;
	}
	
	private void addData(StringBuilder field, StringBuffer value){
		String v = value.toString().trim();
		String k = field.toString().trim();
		if (v.length() == 0 && k.length() == 0) return;
		if (startWithString(k)){
			k = k.substring(1, k.length() - 1);
		}
		
		if (v.length() == 0) {
			v = k;
			k = "";
		}
		
		if (startWithString(v)){
			v = v.substring(1, v.length() - 1).replace("\\\"", "\"");
			v = decodeUnicode(v);
			put(k, v);
		} else if (Line.isLine(v)) {
			put(k, new Line(v).setTable(dbp));
		} else if (v.contains(".")){
			put(k, Double.parseDouble(v));
		} else if (v.toLowerCase().equals("false") || v.toLowerCase().equals("true")){
			put(k, Boolean.parseBoolean(v));
		} else if (v.equals("null")){
			putNull(k);
		} else {
			long vl = Long.parseLong(v);
			if (vl < Integer.MAX_VALUE && vl > Integer.MIN_VALUE){
				put(k, Integer.parseInt(v));
			} else {
				put(k, vl);
			}
		}
		
		field.delete(0, field.length());
		value.delete(0, value.length());
	}
	
	private boolean startWithString(String v){
		if (v.length() <= 0) return false;
		if (v.charAt(0) == QUOT && v.charAt(v.length() - 1) == QUOT) return true;
		if (v.charAt(0) == SQUOT && v.charAt(v.length() - 1) == SQUOT) return true;
		return false;
	}
	
	public String strDecode(Object key){
		String str = str(key);
		try {
			return new String(str.getBytes("iso-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return str;
		}
	}
	
	public String time(Object key){
		return FuncTime.formatTime(Long.parseLong(str(key) + ""));
	}
	
	public int _id(){
		return integer("_id");
	}
	
	public Object get(Object key){
		if (key != null && key instanceof DBField){
			key = ((DBField) key).getName();
		}
		
		if (key instanceof String){
			return data.get(key);
		} else {
			return array.get((Integer) key);
		}
	}
	
	
	public String str(Object key){
 		if (key != null && key instanceof DBField){
			key = ((DBField) key).getName();
		}
		
		String ret;
		if (key instanceof String){
			ret = data.get(key) + "";
		} else {
			ret = array.get((Integer) key) + "";
		}
		return ret.trim().equals("null") ? null : ret;
	}
	
	public Line line(Object key){
		Line ret;
		Object tmp;
		if (key instanceof String || key instanceof DBField){
			tmp = data.get(key + "");
		} else {
			int index = (Integer) key;
			if (array.size() > index){
				tmp = array.get(index);
			} else {
				tmp = null;
			}
		}
		
		if (tmp == null) return Line.def();
		if (tmp instanceof String) ret = new Line(tmp.toString());
		else {
			try {
				ret = (Line) tmp;
			} catch (Exception ex) {
				ret = Line.def();
			}
		}
		
		ret.setTable(dbp);
		return ret;
	}
	
	public int integer(Object key){
		String value = str(key);
		if (value == null || value.length() <= 0) return -1;
		return Integer.parseInt(value);
	}
	
	public boolean bool(Object key){
		String value = str(key);
		if (value == null) return false;
        if (value.equals("false")) return false;
        if (value.equals("0")) return false;
		return true;
	}
	
	public double doub(Object key){
		String value = str(key);
		if (value == null || value.length() <= 0) return -1;
		return Double.parseDouble(value);
	}
	
	public long longint(Object key){
		String value = str(key);
		if (value == null || value.length() <= 0) return -1;
		return Long.parseLong(value);
	}
	
	@Override
	public String toString(){
		//String str = "";
		StringBuilder str = new StringBuilder();
		if (getType() == OBJ){
			str.append("{");
			Set<Entry<Object, Object>> objs = (Set<Entry<Object, Object>>) data.entrySet();
			int i = 0;
			for (Entry<Object, Object> o:objs){
				str.append("\"".concat(toStr(o.getKey())).concat("\":"));
				if (o.getValue() == null) {
					str.append("\"\"");
				} else if (o.getValue().getClass().getSimpleName().equals("String")){
					str.append("\"".concat(toStr(o.getValue())).concat("\""));
				} else {
					str.append(toStr(o.getValue()));
				}
				if (i++ < objs.size() - 1) str.append(",");
			}
			str.append("}");
		} else {
			str.append("[");
			int i = 0;
			for (Object o:array){
				if (o.getClass().getSimpleName().equals("String")){
					str.append("\"".concat(toStr(o)).concat("\""));
				} else {
					str.append(toStr(o));
				}
				if (i++ < array.size() - 1) str.append(",");
			}
			str.append("]");
		}
		
		if (debug_string) {
			_str = str.toString();
			return _str;
		}
		//dataString = str;
		return str.toString();
	}
	
	public ContentValues toContentValue(){
		if (getType() != OBJ) {
			e("toContentValue must in obj");
			return null;
		}
		
		ContentValues d = new ContentValues();
		for (Entry<Object, Object> o:data.entrySet()){
			d.put(o.getKey() + "", o.getValue() + "");
		}
		
		return d;
	}
	
	private String toStr(Object obj){
		if (obj.getClass().getSimpleName().equals("String")) return obj.toString().replace("\\\"", "\"").replace("\"", "\\\"");
		return obj.toString();
	}
	
	public void i(Object obj){
		AFLog.i(this, obj);
	}
	
	public void e(Exception ex){
		AFLog.e(this, ex);
	}
	
	public void e(String o){
		AFLog.e(this, o);
	}
	
	public Line find(int id){
		return new Tables(dbp.getTableClass()).getByPrimaryKey(id);
	}
		
	public boolean eof(){
		if (tmp_iter == null) return true;
		return ! tmp_iter.hasNext();
	}
	
	
	iter tmp_iter;
	
	public int length(){
		return getType() == OBJ ? data.size() : array.size();
	}
	
	public int getType(){
		if (type == -1){
			if (data.size() > 0) type = OBJ;
			if (array.size() > 0) type = ARRAY;
		}
		return type;
	}
	
	public boolean isArray(){
		return array.size() > 0;
	}
	
	public boolean contains(Object kv){
		if (invalid()) return false;
		if (kv.getClass().getSimpleName().equals("dbField")){
			kv = kv.toString();
		}
		if (getType() == OBJ){
			return data.containsKey(kv);
		}
		return array.contains(kv);
	}
	
	public Iterator<Line> iterator() {
		// TODO Auto-generated method stub
		return new iter();
	}
	
	class iter implements Iterator<Line>{
		int index = -1;
		int count;
		public iter(){
			count = getType() == OBJ ? data.size() : array.size();
		}

		public boolean hasNext() {
			// TODO Auto-generated method stub
			return index < count - 1;
		}

		public Line next() {
			// TODO Auto-generated method stub
			return line(++index);
		}

		public void remove() {
			Line.this.remove(index);
			count --;
		}
		
		public int getIndex(){
			return index;
		}
		
	}
	
	public String[] toStringArray(){
		try{
			return array.toArray(new String[array.size()]);
		}catch (Exception ex){
			e(ex);
			return null;
		}
	}
	
	
	
	public Set<Entry<Object, Object>> valueSet(){
		return data.entrySet();
	}
	
	//-------------------------------------------dbcore
	
	public String _field(){
		if (dbp == null) return "";
		return dbp.getPrimaryKey();
	}
	
	public long save(){
		return save(_field());
	}
	
	public long save(DBField... field){
		String[] fields = new String[field.length];
		int i = 0;
		for (DBField f:field){
			fields[i] = f.toString();
			i++;
		}
		return save(fields);
	}

	public boolean isExist(DBField... fields) {
		Tables obj;
		try {
			 obj = (Tables) dbp.getTableClass().newInstance();
		} catch (Exception e) {
			return false;
		}
		for (DBField f: fields) {
			obj.whereAndPrevAll(f.equal(get(f)));
		}
		return obj.count() > 0;
	}
	
	public long save(String... uniqueFields){
		if (getType() != OBJ) {
			e("save only support obj");
			return -1;
		}
		
		if (dbp == null) {
			e("please setTable " + toString());
			return -1;
		}
		
		Tables t = new Tables(dbp.getTableClass());
		boolean isNew = false;
		for (String f:uniqueFields){
			if ( ! contains(f)){
				isNew = true;
				break;
			}
		}
		
		Set<Entry<Object, Object>> kv = data.entrySet();
		ArrayList<String> removeKey = new ArrayList<String>();
		for(Entry<Object, Object> s:kv){
			String key = s.getKey() + "";
			if ( ! dbp.containColumn(key)){
				removeKey.add(key);
			}
		}
		
		for(String key:removeKey){
			data.remove(key);
		}
		
		String[] wheres = new String[uniqueFields.length];
		int i=0;
		for (String w: uniqueFields){
			wheres[i] = "`" + w + "` = '" + str(w) + "'";
			i++;
		}
		
		if ( ! isNew && t.where(wheres).count() <= 0){
			isNew = true;
		}
		
		if (isNew){
			long id = t.insert(this);
			if (id < 0) {
				e("[insert " + dbp.getName() + " failure]" + this);
				return id;
			}
			i("[insert " + dbp.getName() + "]" + this);
			put(_field(), id);
			return id;
		}else{
			
			i("[update " + dbp.getName() + "]" + this);

			t.update(this, wheres);
			Line data = t.where(wheres).get();
			for (Entry<Object, Object> d: data.valueSet()){
				put(d.getKey(), d.getValue());
			}
			return _id();
			
		}
	}
	
	public void delete(){
		if (getType() != OBJ) {
			e("save only support obj");
			return;
		}
		
		if (dbp == null) {
			e("please setTable " + toString());
			return;
		}
		
		new Tables(dbp.getTableClass()).delete(id());
		
	}
	
	// position
	public Line find(String field, String value){
		for (Line o:this){
			if (o.str(field).equals(value)) {
				return o;
			}
		}
		return null;
	}
	
	public String type(String key){
		if ( ! contains(key)) return "null";
		return data.get(key).getClass().getSimpleName().toLowerCase();
	}
	
	public String encodeUnicode(String str){
        char[] chars = new char[str.length()];
        str.getChars(0, str.length(), chars, 0);
        StringBuffer sb = new StringBuffer();
        for (char c:chars){
        	if (c == 10) {
        		sb.append("\\r");
        	} else if (c == 13) {
        		sb.append("\\n");
        	} else if (c <= 256){
        		sb.append(c);
        	}else{
        		sb.append("\\u" + Integer.toHexString(c));
        	}
        }
        return sb.toString();
	}
	
	public String decodeUnicode(String str){
		StringBuffer sb = new StringBuffer();
		for (int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			if (c == BACKSLASH && str.charAt(i+1) == 117){
				String code = str.substring(i + 2, i + 6);
				sb.append((char) Integer.parseInt(code, 16));
				i += 5;
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

    public String toPostData(){
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<Object, Object> kv: valueSet()) {
            String value;
            try{
                value = URLEncoder.encode(kv.getValue().toString(), "utf-8");
            } catch (Exception ex) {
                continue;
            }

            sb.append("&");
            sb.append(kv.getKey().toString());
            sb.append("=");
            sb.append(value);
        }
        String str = sb.toString();
        if (str.length() > 0) str = str.substring(1);
        return str;
    }

	public Intent toIntent(Context m, Class<?> cls){
		return toIntent().setClass(m, cls);
	}
	
	public Intent toIntent(){
		return new Intent().setData(Uri.parse(toString()));
	}
	
	public String toUTF8(){
		return encodeUnicode(toString());
	}
	
	public String toSQL(){
		String str = toString();
		return str.substring(1, str.length() -1);
	}
	
	public String toSQL(String field){
		return field + " in (" + toSQL() + ")";
	}

	public Line toFieldLine(String field){
		Line l = new Line();
		for (Line ll: this){
			String value = ll.str(field);
			if (value == null) continue;
			l.put(value);
		}
		return l;
	}
	
	public Line print(){
		i(toString());
		return this;
	}
	
	public void clear(){
		data.clear();
		array.clear();
		dataString = "";
	}
	
	public boolean checkTablesExist(DBField... field){
		String[] wheres = new String[field.length];
		int i=0;
		for (DBField f: field){
			wheres[i] = f.equal(str(f));
			i++;
		}
		return new Tables(dbp.getTableClass()).where(wheres).count() > 0;
	}
	
	public Line refreshFromDatabase(){
		Line l = new Tables(dbp.getTableClass()).getByPrimaryKey(id());
		for (Entry<Object, Object> i: l.valueSet()){
			put(i.getKey(), i.getValue());
		}
		return this;
	}

	public Iterator<Object> values() {
		return array.iterator();
	}

	public boolean isListValid() {
		return getType() == ARRAY;
	}

	public boolean isDictValid() {
		return getType() == OBJ;
	}

	public boolean isset(DBField mark) {
		if ( ! contains(mark)){
			return false;
		}
		
		String str = str(mark);
		return str != null && str.length() > 0;
	}

	public String join(String string) {
		if (isArray()){
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<length(); i++){
				if (i > 0){
					sb.append(string);
				}
				sb.append(str(i));
			}
			return sb.toString();
		}
		return null;
	}
}
