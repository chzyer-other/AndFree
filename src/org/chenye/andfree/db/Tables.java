package org.chenye.andfree.db;

import java.util.Map.Entry;

import org.chenye.andfree.func.FuncStr;
import org.chenye.andfree.obj.AFLogObj;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.AFLog;

import android.content.ContentValues;

public class Tables extends AFLogObj{
	protected DB db;
	protected dbParse dbp;
	
	private Line _queryData = Line.def();
	public Tables(){
		db = DB.getInstance();
	}
	
	public Tables(Class<?> name){
		this();
		setDBParse(name);
	}
	
	public void setDBParse(Class<?> name){
		setDBParse(new dbParse(name));
	}

	public void setDBParse(String name) {
		setDBParse(new dbParse(name));
	}

	public void setDBParse(dbParse dp) {
		dbp = dp;
	}
	
	@Override
	public String toString(){
		return "[Table]" + dbp.getName();
	}

	public String toSqlAS(String aliasName) {
		return toSqlAS(null, aliasName);
	}

	public String toSqlAS(String field, String aliasName) {
		if (field != null) {
			select(field);
		}
		return String.format("(%s) as %s", toSQL(), aliasName);
	}

	public String toSQL(){
		String sql = "";
		_queryData.putIfNotExist("select", "*");
		if (dbp.getName().length() == 0){
			error("please set dbParse before use {{}} ");
			return null;
		}
		sql += String.format("SELECT %s FROM %s", _queryData.str("select"), dbp.getName());

		if (_queryData.contains("join")){
			sql += " LEFT JOIN " + _queryData.str("join");
		}

		_queryData.putIfNotExist("where", "1=1");
		_queryData.putIfNotExist("order", String.format("%s DESC", dbp.getPrimaryKeyObj().getColumnNameSQL()));
		sql += String.format(" WHERE %s ORDER BY %s", _queryData.str("where"), _queryData.str("order"));

		if (_queryData.contains("limit")) {
			sql += " LIMIT " + _queryData.str("limit");
		}
		return sql;
	}
	
	public Tables select(DBField... fields){
		String sql = "";
		for (DBField field: fields){
			sql += ", " + field.toString();
		}
		if (sql.startsWith(", ")) sql = sql.substring(2);
		_queryData.put("select", sql);
		return this;
	}
	
	public Tables select(String fields){
		_queryData.put("select", fields);
		return this;
	}

	public String makeWhere(String... wheres) {
		String where = "";
		for (String w: wheres){
			if (w.length() == 0) continue;
			if ( ! w.startsWith(" AND ") && ! w.startsWith(" OR ")) {
				w = " AND " + w;
			}
			where += w;
		}
		if (where.startsWith(" AND ")){
			where = where.substring(5);
		} else if (where.startsWith(" OR ")){
			where = where.substring(4);
		}
		return String.format("(%s)", where);
	}
	
	public Tables where(String... wheres){
		String where = makeWhere(wheres);
		_queryData.put("where", where);
		return this;
	}
	
	public Tables whereContinue(String where){
		if ( ! _queryData.contains("where") ||
				_queryData.str("where").length() <= 0){
			where(where);
			return this;
		}
		
		where = _queryData.str("where") + where;
		_queryData.put("where", where);
		return this;
	}
	
	public Tables whereOrPrev(String... wheres){

		if ( ! _queryData.contains("where") ||
				_queryData.str("where").length() <= 0){
			whereContinue(makeWhere(wheres));
			return this;
		}


		String where = _queryData.str("where") + " OR " + makeWhere(wheres) + "";
		_queryData.put("where", where);
		return this;
	}

	public Tables whereAndPrevAll(String ...wheres) {
		String where = "(" + _queryData.str("where") + ") AND " + makeWhere(wheres);
		_queryData.put("where", where);
		return this;
	}
	
	public Tables limit(int limit){
		return limit(0, limit);
	}
	
	public int getLimit(){
		String str = _queryData.str("limit");
		str = str.substring(str.lastIndexOf(" ") + 1);
		return Integer.parseInt(str);
	}
	
	public Tables limit(int start, int length) {
		_queryData.put("limit", String.format("%s, %s", start, length));
		return this;
	}
	
	public Tables order(String... str){
		_queryData.put("order", FuncStr.Join(str, ", "));
		return this;
	}
	
	public Tables order(DBField field){
		return order(field, false);
	}
	
	public Tables order(DBField field, boolean forward){
		return order(field, forward ? "ASC" : "DESC");
	}
	
	public Tables order(DBField field, String forward){
		_queryData.put("order", field + " " + forward + " ");
		return this;
	}
	
	public Tables join(String sql){
		_queryData.put("join", sql);
		return this;
	}

	public Tables join(DBField field1, DBField field2) {
		String tableName = field1.getDatabaseName();
		String currentTableName = dbp.getName() + ".";
		if (currentTableName.equals(field1.getDatabaseName())){
			tableName = field2.getDatabaseName();
		}

		String join = String.format("%s ON %s = %s", tableName.substring(0, tableName.length() - 1), field1.getColumnNameSQL(), field2.getColumnNameSQL());
		join(join);
		return this;
	}
	
	public Line result(){
		String sql = toSQL();
		return query(sql);
	}
	
	public Line get(){
		_queryData.putIfNotExist("limit", "0, 1");
		Line d = result().line(0);
		if (_queryData.str("select").contains(",")) return d;
		if (Line.isLine(_queryData.str("select"))){
			return new Line(_queryData.str("select"));
		}
		return d;
	}

	public Line getByPrimaryKey(int id){
		where(String.format("`%s` = %s", dbp.getPrimaryKey(), id));
		return get();
	}
	
	public String getField(){
		Line l = get();
		for (Entry<Object, Object> a: l.valueSet()){
			try{
				return a.getValue() + "";
			} catch (Exception ex){
				return null;
			}
		}
		return null;
		
	}
	
	public int count(){
		String select = "";
		if (_queryData.contains("select")){
			select = _queryData.str("select");
		}
		select("COUNT(*) as count");
		Line ret = get();
		if (select.length() == 0){
			_queryData.remove("select");
		} else {
			_queryData.put("select", select);
		}
		return ret.integer("count");
	}
	
	public void build(){
		String[] columnsName = dbp.getColumnsName();
		String[] columnsType = dbp.getColumnsType();
		String sql = "CREATE TABLE " + dbp.getName() + "(";
		for (int i=0; i<columnsName.length; i++){
			sql += "`" + columnsName[i] + "` " + columnsType[i];
			if (i<columnsName.length-1) sql += ",";
		}
		sql += ")";
		db.query(sql);
	}
	
	public Line backup(){
		Line l = result();
		return new Line().put(dbp.getName(), l);
	}
	
	public Tables drop(){
		db.query("DROP TABLE IF EXISTS " + dbp.getName());
		return this;
	}
	
	public Tables AddField(String field){
		if ( ! dbp.containColumn(field)) return this;
		AddField(field, dbp.getType(field));
		return this;
	}
	
	public Tables AddField(String field, String type){
		try{
			String sql = "ALTER TABLE " + dbp.getName() + " ADD " + field + " " + type.toUpperCase();
			db.fetch(sql);
		}catch(Exception ex){
			e(ex);
		}
		return this;
	}
	
	public long insert(Line data){
		try{
			
			return db.insert(dbp.getName(), dbp.getPrimaryKey(), dbp.filter(data));
		}catch(Exception ex){
			e(ex);
			return 0;
		}
	}
	
	public long update(Line line, int id){
		return update(line, "`" + dbp.getPrimaryKey() + "` = " + id);
	}

	public long update(Line data, String... wheres){
		
		try{
			where(wheres);
			
			ContentValues cv = dbp.filter(data);
			AFLog.d(this, "[update " + dbp.getName() + "]" + cv + "[where] " + _queryData.str("where"));
			return db.update(dbp.getName(), cv, _queryData.str("where"));
		}catch(Exception ex){
			e(ex);
			return -1;
		}
	}
	
	public Line query(String sql){
		Line line = db.fetch(sql);
		line.setTable(dbp);
		return line;
	}
	
	public void delete(int id){
		delete(dbp.getPrimaryKey() + " = '" + id + "'");
	}
	
	/**
	 * delete the cur data automatic(find the id field)
	 * @param line
	 */
	public void delete(Line line){
		if (line._field().length() <= 0) {
			AFLog.e(this, "delete cur failure! Cause by not appoint the table name or id field");
			return;
		}
		String where = line._field() + " = " + line.str(line._field());
		delete(where);
	}
	
	/**
	 * delete with where string 
	 * @param wheres
	 */
	public void delete(String... wheres){
		if (wheres[0].startsWith(" OR ")){
			wheres[0] = wheres[0].substring(4);
		} else if (wheres[0].startsWith(" AND ")){
			wheres[0] = wheres[0].substring(5);
		}
		String where = FuncStr.arraytoString(wheres, "");
		String sql = "DELETE FROM " + dbp.getName() + " WHERE " + where;
		db.query(sql);
		i("[delete " + dbp.getName() + "] " + where);
	}
	
	public final void empty(){
		delete("_id >0");
	}

	//---------------------------------------------------------------
	
	public final static void BuildAll(){
		Tables[] tabs = Tables.All(); 
		for (int i=0; i<tabs.length; i++){
			tabs[i].build();
		}
	}
	
	public final static void DropAll(){
		Tables[] tabs = Tables.All(); 
		for (int i=0; i<tabs.length; i++){
			tabs[i].drop();
		}
	}
	
	
	
	//-------------------------------------------------------------------------------------------------------------------------------static
	
	public final static Tables[] All(){
		Class<?>[] dbnames = dbParse.all();
        if (dbnames == null) return new Tables[0];
		Tables[] tables = new Tables[dbnames.length];
		for(int i=0; i<dbnames.length; i++){
			tables[i] = new Tables(dbnames[i]);
		}
		return tables;
	}
	
	public void e(Exception ex){
		AFLog.e(this, ex);
	}
	
	public void i(String str){
		AFLog.i(this, str);
	}
}
