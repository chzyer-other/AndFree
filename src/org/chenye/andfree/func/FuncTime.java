package org.chenye.andfree.func;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.chenye.andfree.obj.AFLog;

public class FuncTime {
	public static String[] weekDays = {"Sun", "Mon", "Tues", "Wed", "Thurs", "Fri", "Sat"};
	public static String[] weekDays_cn = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
	public static long time(){
		return System.currentTimeMillis();
	}
	public static String getWeek_cn(){
		return weekDays_cn[getWeekOfDate()];
	}
	
	public interface onTimeSpend{
		public Object call(Object obj);
	}
	
	public static void getTimeSpend(onTimeSpend... calls){
		if (calls.length > 0 && calls[0] == null) return;
		FuncTime t = new FuncTime();
		t.setflag();
		int i=0;
		Object tmp = null;
		for (onTimeSpend call: calls){
			i++;
			FuncTime t2 = new FuncTime();
			t2.setflag();
			tmp = call.call(tmp);
			t2.getflag("step " + i);
		}
		t.getflag("all spend");
	}
	
	public static int getWeekOfDate(){
		Date d = new Date();
		d.setTime(time());
		
		return getWeekOfDate(d);
	}
	
	public static long get_date_long(){
		long time_long = FuncTime.time();
		String[] times = FuncTime.getDate(time_long, "H:m:s").split(":");
		return time_long - (Integer.parseInt(times[0]) * 3600 + Integer.parseInt(times[1]) * 60 + Integer.parseInt(times[2]))*1000 + time_long % 1000;
	}
	
	public static long parse_time(String time){
		String[] t = time.split(":");
		long timestamp = Integer.parseInt(t[0]) * 60 * 60 + Integer.parseInt(t[1]) * 60 ;
		timestamp = timestamp * 1000 + get_date_long();
		return timestamp;
	}
	
	public static String getTime(){
		return getTime(time());
	}
	public static String getTime(long time){
		Date d = new Date();
		d.setTime(time);
		String date = /*(d.getHours() < 10 ? "0" : "") + */d.getHours() + ":" + (d.getMinutes()<10 ? "0" : "") + d.getMinutes(); 
		return date;
	}
	
	public static String formatTime(){
		return formatTime(time());
	}
	
	public static String formatTime(long time){
		String date = "";
		if (getDate(time()).compareTo(getDate(time)) == 0){
			date = getTime(time);
		}else{
			date = getDate(time);
		}
		
		return date;
	}
	public static String formatTime(String time){
		return formatTime(Long.parseLong(time));
	}
	public static String formatTime2(long time){
		String date = "";
		if (getDate(time()).compareTo(getDate(time)) == 0){
			date = getTime(time);
		}else{
			date = getDate(time) + " " + getTime(time);
		}
		
		return date;
	}
	
	public static String getDate(){
		return getDate(time());
	}
	
	public static String getDate(long time){
		return getDate(time, "M月d日");
	}
	
	
	
	public static String getDate(long time, String format){
		SimpleDateFormat formatter = new SimpleDateFormat (format);
		Date curDate = new Date(time);//��ȡ��ǰʱ��     
		String str = formatter.format(curDate);     
		return str;
	}
	public static int getWeekOfDate(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return w;
    }
	
	private long tmp_time = 0;
	
	public FuncTime setflag(){
		tmp_time = time();
		return this;
	}
	
	public void getflag(){
		AFLog.d(this, "spend time: " + (time() - tmp_time));
		tmp_time = 0;
	}
	
	public void getflag(String m){
		AFLog.d(this, m + " spend time: " + (time() - tmp_time));
		tmp_time = 0;
		setflag();
	}
	
	public void getflag(Object m){
		AFLog.d(this, m.getClass().getName() + " spend time: " + (time() - tmp_time));
		tmp_time = 0;
		setflag();
	}
	public void check(String a){
	}
	
}
