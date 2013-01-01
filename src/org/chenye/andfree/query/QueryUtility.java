package org.chenye.andfree.query;

import java.net.URLEncoder;
import java.util.Map.Entry;

import org.apache.http.HttpHost;
import org.chenye.andfree.db.DB;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class QueryUtility {
	public static Context mContext = DB.getInstance().getContext();
	public static String urlencode(Line data){
		if (data == null) return null;
		String params = "";
		for (Entry<Object, Object> param: data.valueSet()){
			String key = param.getKey() + "";
			String value = param.getValue() + "";
			if (value.equals("null")) value = "";
			params += "&" + key + "=" + URLEncoder.encode(value);
		}
		if (params.length() > 0) params = params.substring(1);
		return params;
	}
	
	public static HttpHost getAPN() {
		HttpHost proxy = null;
		Uri uri = Uri.parse("content://telephony/carriers/preferapn");
		Cursor mCursor = null;
		if (null != mContext) {
			mCursor = mContext.getContentResolver().query(uri, null, null, null, null);
		}
		if (mCursor != null && mCursor.moveToFirst()) {
			// 游标移至第一条记录，当然也只有一条
			String proxyStr = mCursor.getString(mCursor.getColumnIndex("proxy"));
			if (proxyStr != null && proxyStr.trim().length() > 0) {
				proxy = new HttpHost(proxyStr, 80);
			}
			mCursor.close();
		}
		return proxy;
	}
}
