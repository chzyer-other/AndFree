package org.chenye.andfree.func;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.chenye.andfree.db.DB;
import org.chenye.andfree.helper.HelperContent;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

public class FuncNet {
	public FuncNet(String host){
		this.host = host;
		String apn = new HelperContent("content://telephony/carriers/preferapn").get().str("apn");
		cmwap = ( ! isWiFiActive(DB.getInstance().getContext())) && apn.equals("cmwap");
	}
	
	public String host = "";
	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	boolean cmwap = false;
    
    public interface onCallback{
    	public void call(Line ret);
    }
    
    
    
    public static class onCallbackRet {
    	public Line call(Line ret){return null;}
    	public Line ret(Line ret){
    		return ret;
    	}
    	public Line per(Line ret){return ret;}
    }
    
    public interface onCallbackString {
    	public String onString(String ret);
    }
    
    public interface onCallbackBitmap {
    	public String onBitmap(Bitmap ret);
    }
    
    public interface onCallbackBoolean {
    	public void onBoolean(boolean ret);
    }

    public void post(final String url, final Line data, final onCallback call){
    	class cls extends AsyncTask<Object, Line, Line>{

			@Override
			protected Line doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String ret = post(url, data);
				if (ret == null) return Line.def();
				return new Line(ret);
			}
    		
			@Override
			protected void onPostExecute(Line result) {
				// TODO Auto-generated method stub
				call.call(result);
			}
    	}
    	new cls().execute();
    }
    
    public void get(final String url, final onCallback call){
    	class cls extends AsyncTask<Object, Line, Line>{

			@Override
			protected Line doInBackground(Object... params) {
				// TODO Auto-generated method stub
				String ret = FuncNet.this.get(url);
				if (ret == null) return Line.def();
				return new Line(ret);
			}
    		
			@Override
			protected void onPostExecute(Line result) {
				// TODO Auto-generated method stub
				call.call(result);
			}
    	}
    	new cls().execute();
    }
    
    public String get(String url){
    	log.i(this, "http://" + host + url);
		HttpGet httpGet = new HttpGet("http://" + host + url);
		
		DefaultHttpClient httpclient = new DefaultHttpClient();
		
		if (cmwap){
			httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("10.0.0.172", 80, "http"));
		}
		
		try {
			HttpResponse httpResponse = httpclient.execute(new HttpHost(host, 80, "http"), httpGet);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				return result;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	
		return null;		
    }
    
    public String post(String url, Line data){    
    	
		HttpPost httpPost = new HttpPost("http://" + host + url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (Entry<Object, Object> item: data.valueSet()){
			params.add(new BasicNameValuePair(item.getKey() + "", data.encodeUnicode(item.getValue() + "")));
		}
		
		HttpResponse httpResponse;
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			DefaultHttpClient httpclient = new DefaultHttpClient();
			if (cmwap){
				httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("10.0.0.172", 80, "http"));
			}
			httpResponse = httpclient.execute(httpPost);
			
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(httpResponse.getEntity());
				log.i(this, result);
				return result;
			}
			i(httpResponse.getStatusLine().getStatusCode());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e(e);
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e(e);
			return null;
		}
		return null;
		
    }
    
    public void e(Exception ex){
    	log.e(this, ex);
    }

    public void i(Object obj){
    	log.i(this, obj);
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
    
	public static boolean checkNet(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
			.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info == null || !info.isAvailable()) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
    
}
