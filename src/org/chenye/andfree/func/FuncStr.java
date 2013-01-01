package org.chenye.andfree.func;

import java.io.UnsupportedEncodingException;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.obj.Line;


import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Base64;

public class FuncStr extends BaseLog{
	public static String arraytoString(String[] arrays, String split){
		String str = "";
		for (String array: arrays){
			if (array.startsWith(split)){
				str += array;
				continue;
			}
			str += split + array;
		}
		if (str.startsWith(split)) str = str.substring(split.length());
		return str;
	}
	
	public static String md5(Object... objs){
		Line l = new Line();
		for (Object o:objs){
			l.put(o + "");
		}
		return md5(l.toString());
	}
	
	public static String base64encode(String str){
		return Base64.encodeToString(str.getBytes(), Base64.DEFAULT);
	}
	
	public static String base64decode(String str){
		return new String(Base64.decode(str, Base64.DEFAULT));
	}
	
	public static String stringEncode(String str){
		if (str == null) return "";
		try {
			byte[] b = str.getBytes("UTF-8"); 
			return new String(b, "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String stringDecode(String str){
		if (str == null) {
			error(new FuncStr(), "base64decode->get null source");
			return null;
		}
		//return new String(Base64.decode(str, Base64.DEFAULT));
		try {
			byte[] b = str.getBytes("ISO-8859-1");
			return new String(b, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
	
	public static String md5(String s){return md5(s.getBytes());}
	public static String md5(byte[] source) {
		String s = null;
		char hexDigits[] = {
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' 
		};
		
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			md.update(source);
			byte tmp[] = md.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
		
	public static int getStringCount(String str, String chars){
		int count = 0;
		int start = 0;
		while(str.indexOf(chars, start) != -1) {
			start = str.indexOf(chars, start) + 1;
			count++;
		}
		return count;
	}
	
	public static int dp2pix(Context m, int dp){
	    final float scale = m.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
	
	public static int pixel2dp(Context m, int pix){
		final float scale = m.getResources().getDisplayMetrics().density;
		return (int) ((int) pix / scale) ;
	}
	
	public static String getProtocol(){
		return "(protocol <> -5 or (protocol is null and type = 2))";
	}
	
	public static String get_all_phone_template(){
		return "'%s','+86%s','12520%s'";
	}
	
	public static String get_all_phone_template(String phone){
		return String.format(get_all_phone_template(), phone , phone, phone);
	}
	
	public static String get_all_phone(String phone){
		phone = FuncStr.Sub2Phone(phone);
		if ( ! check_phone(phone)) return "'" + phone + "'";
		if (FuncStr.check_phone(phone)){
			phone = String.format(get_all_phone_template(), phone , phone, phone);
		}
		return phone;
	}
	
	public static boolean preg(String regEx,String sourse){
		if (sourse == null) return false;
		return preg_match(regEx, sourse).find();
	}
	
	public static Matcher preg_match(String regEx,String sourse){
		Pattern p=Pattern.compile(regEx);
		return p.matcher(sourse);
	}
	
	public static boolean CheckNumber(String num){
		num = SubCN(num);
		return preg("^\\d+$", num);
	}
	
	public static boolean check_mobile_phone(String phone){
		String regex = "^13[4-9]\\d{8}$|147\\d{8}$|15[0-27-9]\\d{8}$|18[2378]\\d{8}$";		
		return preg(regex, phone);		
	}
	
	public static boolean check_phone(String phone){
		if (phone.startsWith("+86")) phone = phone.substring(3);
		boolean result = phone.length() == 11 && phone.substring(0,1).equals("1");
		
		String two = phone.substring(1, 2);
		if (result && (two.equals("3") || two.equals("4") || two.equals("5") || two.equals("8"))){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	public static String get_zip(String key, String[] index, String[] data){
		for (int i=0; i<index.length; i++){
			if (key.equals(index[i])){
				return data[i];
			}
		}
		return null;
	}
	
	public static String getRandomString(int length) {  
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < length; i++) {  
            int number = random.nextInt(base.length());  
            sb.append(base.charAt(number));  
        }  
        return sb.toString();  
	}
	
	public static String get_music_name(Context m, Object uri){
		if (uri.equals("") || uri.equals("0")){
			return "����";
		}
		Ringtone ring = get_ringtone(m, uri);
		if (ring == null) return "����";
		return ring.getTitle(m);
	}
	
	public static Ringtone get_ringtone(Context m, Object uri){
		
		return RingtoneManager.getRingtone(m, Uri.parse(uri + ""));
	}
	
	public static boolean CheckFetion(String phone){
		if (phone == null) return false;
		return phone.length() == 16 && phone.substring(0,6).equals("125201");
	}
	
	public static String SubFetion(String phone){
		if (CheckFetion(phone)){
			return phone.substring(5, phone.length());
		}
		return phone;
	}
	
	public static String Sub2Phone(String phone){
		if (phone == null) return null;
		phone = SubFetion(phone);		
		if (phone.startsWith("+86")){
			phone = phone.substring(3);
		}
		return phone;
	}
	
	public static String check_password(String body){
		if (body == null){
			error(new FuncStr(), "check_password-> null body");
			return "";
		}
		body = body.replaceAll("忘密码|忘记密码", "");
		String keyword = "(?:密码|验证码|邀请码|校验码|申请码|使用码|口令)";
		String result = "";
		Matcher m = FuncStr.preg_match(keyword + ".*?(\\d+)", body);
		if ( ! m.find()){
			return "";
		}
		if (m.group(1).length() >= 4){
			return m.group(1);
		}
		
		m = FuncStr.preg_match("(\\d+).*?" + keyword, body);
		if ( ! m.find() || m.group(1).length() < 4){
			return "";
		}
		result = m.group(1);
		m = FuncStr.preg_match(result + "\\.(cn|com)", body);
		if (m.find()) return "";
		return result;
	}
	
	public static String check_url(String body){
		String regex = "([a-zA-Z0-9:\\&\\?\\-\\.\\/]+\\.[a-zA-Z]{2,3}(?:[a-zA-Z0-9\\&\\?\\-\\.\\/=])*)";
		Matcher m = preg_match(regex, body);
		if ( ! m.find() || m.group(1).length()<= 4){
			return "";
		}
		String result = m.group(1); 
		if ( ! result.startsWith("http")){
			result = "http://" + result;
		}
		return result;
		
	}
	
	public static String SubCN(String phone){
		if (phone == null) return null;
		if (phone.length() == 14 && phone.startsWith("+86")){
			return phone.substring(3);
		}
		return phone;
	}
	
	public static Line findall(String regex, String source) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(source);
		Line datas = new Line();
		while (m.find()){
			Line d = new Line();
			for (int i=1; i<m.groupCount(); i++){
				d.put(m.group(i));
			}
			if (d.invalid()) continue;
			datas.put(d.length() == 1 ? d.str(0) : d);
		}
		
		return datas;
	}
}
