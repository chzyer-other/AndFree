package org.chenye.andfree.func;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import org.chenye.andfree.db.DB;
import org.chenye.andfree.helper.cpHelper;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class restFunc {
	private restFunc(){}
	
	public static void post(String url, String data, afterConnect after){
		String host = "andfree.sinaapp.com";
		send("POST", host, url, data, after);
	}

	public static void send(final String method, final String host, final String url, final String data, final afterConnect ac){
		class connect extends AsyncTask<Object, Object, Boolean>{
			String response = "";
			String html = "";
			@Override
			protected Boolean doInBackground(Object... params) {
				// TODO Auto-generated method stub
				try {
					Socket socket;
					if (isCmWap()){
						socket = new Socket(InetAddress.getByName("10.0.0.172"), 80);
					}else{
						socket = new Socket(host, 80);
					}
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					
					i(header(method, host, url, data));
					dos.writeBytes(header(method, host, url, data));
					
					boolean readHeader = true;
					boolean readLen = false;
					
					
					while(true){
						String line = dis.readLine();
						if (line == null) break;
						if (line.length() == 0){
							if (html.length() > 0) break;
							readLen = true;
							readHeader = false;
						} else if (readHeader) {
							response += line + "\r\n";
						} else if (readLen){
							int length = hex2int(line);
							if (length <= 0) break;
							char[] b = new char[length];
							for(int i=0; i<length; i++){						
								b[i] = (char) dis.read();
							}			
							
							html = new String(b);
							i(html);
						}
					}
					
					socket.close();
					return true;
					
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}
			
			@Override
			protected void onPostExecute(Boolean success) {
				if ( ! success) return;
				ac.finish(response, html);
			}
		}
		new connect().execute();
	}
	
	public interface afterConnect{
		public void finish(String response, String html);
	}
	
	public static int hex2int(String hex){
    	hex = hex.toUpperCase();
    	int total = 0;
    	for(int i=0; i<hex.length(); i++){
    		int d = hex.length() - i - 1;
    		int t = hex.charAt(i);
    		t -= 48;
    		if (t >= 16) t -= 7;
    		else if(t < 0) t = 0;
    		total += t * java.lang.Math.pow(16, d);
    	}
    	return total;
    }
	
	public static String header(String method, String host, String url, String data){
    	String header = method + " " + url + " HTTP/1.1\r\n"
    			+ "X-Online-Host: " + host + "\r\n"
				+ "Host: " + host + "\r\n";
    	
		header += "Content-Type: application/x-www-form-urlencoded\r\n"
				+ "Content-Length: " + data.length() + "\r\n"
				+ "Connection: close\r\n\r\n";
    	if (method.equals("POST")) {
			header += data;
		}
    	
    	return header;
	}
	
	public static void e(Exception ex){
		log.e(new restFunc(), ex);
	}
	public static void i(Object obj){
		log.i(new restFunc(), obj);
	}
	
	public static boolean isCmWap(){
		Line data = new cpHelper("content://telephony/carriers/preferapn").get();
		if (data.invalid()) return false;
		String apn = data.str("apn");
		return ( ! isWiFiActive(DB.getInstance().getContext())) && apn.equals("cmwap");
	}
	
	public static boolean isWiFiActive(Context inContext) {  
        Context context = inContext.getApplicationContext();  
        ConnectivityManager connectivity = (ConnectivityManager) context  
                .getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (connectivity != null) {  
            NetworkInfo[] info = connectivity.getAllNetworkInfo();  
            if (info != null) {  
                for (int i = 0; i < info.length; i++) {  
                    if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {  
                        return true;  
                    }  
                }  
            }  
        }  
        return false;  
    }
}
