package org.chenye.andfree.func;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.chenye.andfree.obj.Line;

import android.os.Environment;

public class fileFunc {
	static boolean enable_gzip = true;
	static boolean encrypt = true;
	public static void save(String dir, String filename, Line line){
		save(dir, filename, line.toString());
	}
	
	public static boolean checkSDAvailable(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static void save(String dir, String filename, String str){
		save(dir, filename, enable_gzip ? gzipFunc.compressToByte(str) : str.getBytes());
	}
	
	public static void save(String dir, String filename, byte[] content){
		File file = getFile(dir, filename);
        try {
        	FileOutputStream outStream = new FileOutputStream(file);  
        	if (encrypt) outStream.write(new byte[] {1});
			outStream.write(content);
			outStream.close();
		} catch (IOException e) {
			e(e);
		}
	}
	
	public static Line readLine(String dir, String filename){
		return new Line(readString(dir, filename));
	}
	
	public static String readString(String dir, String filename){
		byte[] ret = read(dir, filename);
		if (ret == null) return "";
		return enable_gzip ? gzipFunc.uncompressToString(ret) : new String(ret);
	}
	
	public static byte[] read(String dir, String filename){
		File file = getFile(dir, filename);
		try{
        	FileInputStream inputString = new FileInputStream(file);
        	byte[] ret = new byte[(int) file.length()];
        	if (encrypt) inputString.read();
        	inputString.read(ret);
        	inputString.close();
        	return ret;
        } catch (IOException e) {
        	e(e);
        }
		return null;
	}
	
	public static File getFile(String dir, String filename){
		File file = new File(dir);
		if ( ! file.exists()) file.mkdirs();
		file = new File(dir + filename);
		return file;
	}
	
	public static void e(Exception ex){
		log.e(new fileFunc(), ex);
	}
	
	public static void i(Object o){
		log.i(new fileFunc(), o);
	}
}
