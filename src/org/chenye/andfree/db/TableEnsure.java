package org.chenye.andfree.db;

import org.chenye.andfree.db.DB;
import org.chenye.andfree.obj.AFLogActivity;
import org.chenye.andfree.obj.Line;

public class TableEnsure extends AFLogActivity{
	public void test(){
		DB db = DB.getInstance();
		String sql = "SELECT * FROM sqlite_master WHERE type='table' AND name != 'android_metadata'";
		Line line = db.fetch(sql);
		Line exits_table = new Line();
		Line code_table = new Line();
		
		for (Class<?> t: dbParse.all()){
			code_table.put(t.getSimpleName());
		}
		
		for (Line l: line){
			exits_table.put(l.str("name"));
		}
		
		for (int i=0; i<code_table.length(); i++){
			String tablename = code_table.str(i);
			dbParse dbp = new dbParse(tablename);
			if ( ! exits_table.contains(tablename)){
				new Tables(dbp.getTableClass()).build();
			} else {
				String[] columsName = dbp.getColumnsName();
				Line l = db.fetch("PRAGMA table_info(" + dbp.getName() + ")");
				for (int j=0; j<columsName.length; j++){
					if (l.find("name", columsName[j]) == null){
						new Tables(dbp.getTableClass()).AddField(columsName[j]);
					}
				}
			}
		}
		
	}
	
}
