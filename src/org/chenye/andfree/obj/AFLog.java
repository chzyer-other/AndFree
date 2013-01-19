package org.chenye.andfree.obj;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.conf.AndfreeDebug;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public final class AFLog {
	private static String getObj(Object cls){
		if ( ! (cls instanceof String)) {
			cls = cls.getClass().getSimpleName();
		}
		return "[" + cls + "@" + cls.hashCode() + "] ";
	}
	
	
	public static void e(Object cls, Object obj){
		if ( ! AndfreeDebug.LOG.DO()) return;
		String str = "";
		if (obj instanceof Exception){
			str = getMsgFromException((Exception) obj);
		} else {
			str = obj.toString();
		}
		Log.e(AndfreeConf.LOG_TAG, String.format("%s %s", getObj(cls), str));
	}
	
	private static String getMsgFromException(Exception ex){
		String msg = ex.getMessage();
		if (msg == null) {
			msg = ex.toString();
		}
		StringWriter str = new StringWriter();
		ex.printStackTrace(new PrintWriter(str));
		msg += String.format("\n%s", str);
		return msg;
	}
	
	public static void i(Object obj, Object... object){
		if (object.length <= 0) {
			i(obj, "null");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(getObj(obj));
		for (Object o: object){
			if (o == null) continue;
			sb.append(o.toString());
			sb.append(",");
		}
		i(sb.toString());
	}
	
	public static void i(Object obj, long str){
		i(obj, str + "");
	}
	
	public static void i(Object obj, String func, String... str){
		StringBuilder sb = new StringBuilder();
		sb.append(getObj(obj));
		sb.append("[");
		sb.append(func);
		sb.append("]");
		for (String s: str){
			sb.append(s);
		}
		i(sb.toString());
	}
	
	public static void i(Object obj, String str){
		i(getObj(obj) + str);
	}
	
	public static void i(ContentValues data){
		i(data.toString());
	}
	
	public static void i(Line line){
		i(line.toString());
	}
	
	public static void i(AFCursor cur){
		i(cur.toString());
	}
	
	private static void i(String str){
		if ( ! AndfreeDebug.LOG.DO()) return;
		Log.i(AndfreeConf.LOG_TAG, str);
	}
	

	
	public static void d(Object obj, String str){
		d(getObj(obj) + str);
	}
	
	private static void d(String str){
		if ( ! AndfreeDebug.LOG.DO()) return;
		Log.d(AndfreeConf.LOG_TAG, str);
	}
	
	public static void w(Object obj, String str){
		w(getObj(obj) + str);
	}
	
	private static void w(String str){
		if ( ! AndfreeDebug.LOG.DO()) return;
		Log.w(AndfreeConf.LOG_TAG, str);
	}
	
	public static void v(Object obj, String str){
		v(getObj(obj) + str);
	}
	
	private static void v(String str){
		if ( ! AndfreeDebug.LOG.DO()) return;
		Log.v(AndfreeConf.LOG_TAG, str);
	}
	

	
	
	public static void toast(Context m,int str){
		toast(m, "" + str);
	}
	
	public static void toast(Context m, String str){
		Toast.makeText(m, str, Toast.LENGTH_LONG).show();
	}
	
	public static void toast(Context m, String str, int margin){
		Toast t = Toast.makeText(m, str, Toast.LENGTH_LONG);
		t.setDuration(500);
		t.setMargin(0, (float) 0.6);
		t.show();
	}
	
}
