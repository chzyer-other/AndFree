package org.chenye.andfree.db;

import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.obj.Line;

import android.text.Editable;

public class baseConfig {
	public final static class first{
		static final String pack = "first";
		public static final configField LAST_INSTALL_DATE = _f(pack, "installDate", 0);
		public static final configField RATE_REMINDED = _f(pack, "rateReminded", false);
		public static final configField RUN = _f(pack, "run", true);
		public static final configField LAST_REMIND_VERSION = _f(pack, "lastRemindVersion", AndfreeConf.VERTION);
	}
	
	private static String getClsName(Object obj){
		String cls_name = obj.getClass().getSimpleName();
		if (cls_name.toLowerCase().equals("class")){
			cls_name = ((Class<?>) obj).getSimpleName();
		} else if (cls_name.toLowerCase().equals("string")){
			cls_name = obj + "";
		}
		return cls_name;
	}
	
	protected static configField _f(Object obj, String name){
		return new configField(getClsName(obj), name, "");
	}
	
	protected static configField _f(Object obj, String name, int def){
		return _f(getClsName(obj), name, def + "");
	}
	
	protected static configField _f(Object obj, String name, boolean def){
		return _f(getClsName(obj), name, def ? "true" : "false");
	}
	
	protected static configField _f(Object obj, String name, String def){
		return new configField(getClsName(obj), name, def);
	}
	
	public static class configField{
		String pack;
		String name;
		String def;
		public configField(String pack, String name, String def){
			this.pack = pack;
			this.name = name;
			this.def = def;
		}
		
		public String def(){
			return def;
		}
		
		public String toString(){
			return (pack + "_" + name).toUpperCase();
		}
		
		public String sql(){
			return "`key` = '" + toString() + "'";
		}
		
		public String str(){
			if (new AndfreeDBcore.config().where(sql()).count() == 0){
				set(def);
				return def;
			}
			return new AndfreeDBcore.config().select(AndfreeDBcore.config.value).where(sql()).getField();
		}
		
		public String bool(String true_string, String false_string){
			return bool() ? true_string : false_string;
		}
		public boolean bool(){
			return Boolean.parseBoolean(str());
		}
		
		public Line line(){
			return new Line(str());
		}
		
		public int Int(){
			return Integer.parseInt(str());
		}
		
		public long Long(){
			return Long.parseLong(str());
		}
		
		public void set(long data){
			set(data + "");
		}
		
		public void set(boolean data){
			set(data + "");
		}
		public void set(Editable data){
			set(data.toString());
		}
		
		public void set(String data){
			Line line = new Line(AndfreeDBcore.config.class);
			line.put(AndfreeDBcore.config.key, toString());
			line.put(AndfreeDBcore.config.value, data);
			line.save();
		}
		
		public boolean equals(Object key){
			return str().equals(key);
		}
		
		public int length(){
			return str().length();
		}
	}
}
