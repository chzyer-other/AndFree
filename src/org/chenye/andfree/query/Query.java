package org.chenye.andfree.query;

import java.util.Map.Entry;

import org.apache.http.Header;
import org.chenye.andfree.func.FuncStr;
import org.chenye.andfree.obj.Line;

public class Query {
	/**
	 * 默认使用http协议
	 * @param host 域名
	 */
	public Query(String host){
		this("http", host);
	}
	
	/**
	 * @param scheme 协议(http/https)
	 * @param host 域名
	 */
	public Query(String scheme, String host){
		conf = new QueryOption();
		conf.scheme = scheme.toLowerCase();
		conf.host = host.toLowerCase();
		if (conf.scheme.equals("http")){
			conf.port = 80;
		} else if (conf.scheme.equals("https")){
			conf.port = 443;
		}
	}
	
	public QueryOption conf;
	
	public Query FetchQueue(IHttpRequest... requests){
		new QueryFetchQueue(this, conf, requests);
		return this;
	}
	
	public String getPach(){
		StringBuffer sb = new StringBuffer();
		sb.append(conf.scheme);
		sb.append("://");
		sb.append(conf.host);
		if ((conf.scheme.equals("http") && conf.port != 80) || (conf.scheme.equals("https") && conf.port != 443) ){
			sb.append(":");
			sb.append(conf.port);
		}
		if ( ! conf.path.startsWith("/")){
			sb.append("/");
		}
		sb.append(conf.path);
		return sb.toString();
	}
	
	public void parseHeader(Header[] headers){
		Line data = new Line();
		for (Header h: headers){
			String key = h.getName();
			String value = h.getValue();
			if (key.equalsIgnoreCase("set-cookie")){
				String f = FuncStr.findall("^(([^=]+))=", value).str(0);
				String v = FuncStr.findall("^[^=]+=((.*?));", value).str(0);
				conf.cookie.put(f, v);
				continue;
			}
			data.put(key, value);
		}
		conf.header = data;
	}
	
	public String getCookieString(){
		StringBuilder sb = new StringBuilder();
		for (Entry<Object, Object> q: conf.cookie.valueSet()){
			sb.append(q.getKey());
			sb.append("=");
			sb.append(q.getValue());
			sb.append(";");
		}
		return sb.toString();
	}
}
