package org.chenye.andfree.func;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class gzipFunc {
    private static String encode = "utf-8";//"ISO-8859-1"  
      
    public String getEncode() {  
        return encode;  
    }  
  
    public void setEncode(String encode) {  
    	gzipFunc.encode = encode;  
    }  
  
    public static byte[] compressToByte(String str){  
        if (str == null || str.length() == 0) {  
            return null;  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        GZIPOutputStream gzip;  
        try {  
            gzip = new GZIPOutputStream(out);  
            gzip.write(str.getBytes(encode));  
            gzip.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }
        return out.toByteArray();  
    }  
  
    public static byte[] compressToByte(String str,String encoding){  
        if (str == null || str.length() == 0) {  
            return null;  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        GZIPOutputStream gzip;  
        try {  
            gzip = new GZIPOutputStream(out);  
            gzip.write(str.getBytes(encoding));  
            gzip.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return out.toByteArray();  
    }  
  
    /* 
     * �ֽ������ѹ���󷵻��ַ� 
     */  
    public static String uncompressToString(byte[] b) {  
        if (b == null || b.length == 0) {  
            return null;  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ByteArrayInputStream in = new ByteArrayInputStream(b);  
  
        try {  
            GZIPInputStream gunzip = new GZIPInputStream(in);  
            byte[] buffer = new byte[256];  
            int n;  
            while ((n = gunzip.read(buffer)) >= 0) {  
                out.write(buffer, 0, n);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return out.toString();  
    }  
  
    /* 
     * �ֽ������ѹ���󷵻��ַ� 
     */  
    public static String uncompressToString(byte[] b, String encoding) {  
        if (b == null || b.length == 0) {  
            return null;  
        }  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        ByteArrayInputStream in = new ByteArrayInputStream(b);  
  
        try {  
            GZIPInputStream gunzip = new GZIPInputStream(in);  
            byte[] buffer = new byte[256];  
            int n;  
            while ((n = gunzip.read(buffer)) >= 0) {  
                out.write(buffer, 0, n);  
            }  
            return out.toString(encoding);  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
	
}
