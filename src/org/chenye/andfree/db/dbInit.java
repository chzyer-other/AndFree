package org.chenye.andfree.db;

import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.func.FuncTime;
import org.chenye.andfree.func.log;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class dbInit extends SQLiteOpenHelper{
	public dbInit(Context context){
		super(context, AndfreeConf.DB_FILENAME, null, AndfreeConf.VERTION);
	}
	
	/**
	 * call when db create 
	 */
	@Override
	public void onCreate(SQLiteDatabase data) {
		DB db = new DB(data);
		Tables.BuildAll();
		onChange(db);
	}
	
	/**
	 * call when db update
	 */
	@Override
	public void onUpgrade(SQLiteDatabase data, int oldVersion, int newVersion) {
		DB db = new DB(data);
		log.w(this, "old db version: " + oldVersion + ", new Version : " + newVersion);
		UpdateTable(db);
		UpdateField(db);
		onChange(db);
	}
	
	public void onChange(DB db){
		BaseConfig.first.LAST_INSTALL_DATE.set(FuncTime.time());
		BaseConfig.first.RATE_REMINDED.set(false);
		BaseConfig.first.RUN.set(true);
	}
	
	public static void UpdateTable(DB db){
		Class<?>[] dbnames = dbParse.all();
		for (Class<?> dbname:dbnames){
			String sql = "select count(*) as count from sqlite_master WHERE type='table' and name='" + dbname.getSimpleName() + "'";
			Line line = db.fetch(sql);
			if (line.length() >= 1) line = line.line(0);
			if (line.integer("count") == 0){
				new Tables(dbname).build();
			}
		}
	}
	
	
	public static void UpdateField(DB db){
		Class<?>[] dbnames = dbParse.all();
		for (int i=0; i<dbnames.length; i++){
			dbParse dbp = new dbParse(dbnames[i]);
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