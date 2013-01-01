package org.chenye.andfree.query;

import in.hitme.android._network.HitmeNioClient.ClientException;

import org.chenye.andfree.func.FuncStr;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.query.QueryFetchQueue.InextQueue;
import org.chenye.andfree.query.QueryFetchQueue.NextStep;

public abstract class IHttpRequest {
	boolean post = false;
	public IHttpRequest(boolean post){
		this.post = post;
	}
	public boolean isPost(){
		return post;
	}
	
	String _path;
	public String path(){
		return _path;
	}
	public String host(){
		return null;
	}
	public String referer(){
		return null;
	}
	public Line data(){
		return null;
	}
	
	/** 接收到数据 */
	public abstract QueueDirector onResponse(String rep);
	public abstract void onException(ClientException ex);
	public QueueDirector onRedirect(String path){
		return null;
	}
	
	
	public QueueDirector stop(){
		return QueueDirector.Stop();
	}
	public QueueDirector jump(IHttpRequest ihr){
		ihr.setOnCreateNewStep(oncreateNewStep);
		ihr.setCookieSource(_cookie);
		if (nextFetch != null && ! nextFetch.req.equals(ihr)){
			ihr.setNextFetch(nextFetch);
			ihr.setDataSource(data);
		}
		NextStep ns = oncreateNewStep.create(ihr);
		return QueueDirector.Jump(ns);
	}
	public QueueDirector end(){
		return QueueDirector.End();
	}
	public QueueDirector next(){
		return QueueDirector.Next();
	}
	
	public NextStep nextStep(){
		return new NextStep(nextFetch);
	}
	public NextStep nextStep(IHttpRequest hr){
		return oncreateNewStep.create(hr);
	}
	
	private InextQueue nextFetch;
	public void setNextFetch(InextQueue hr){
		nextFetch = hr;
	}
	
	IcreateNewStep oncreateNewStep;
	public void setOnCreateNewStep(IcreateNewStep ins){
		oncreateNewStep = ins;
	}
	public interface IcreateNewStep{
		public NextStep create(IHttpRequest hr);
	}
	
	Line _cookie;
	public void setCookieSource(Line cookie){
		this._cookie = cookie;
	}
	public String cookie(String field){
		return _cookie.str(field);
	}
	public Line getAllCookie(){
		return _cookie.clone();
	}
	
	public void setDataSource(Line data){
		this.data = data;
	}
	Line data;
	public void setData(String field, Object obj){
		data.put(field, obj);
	}
	public String getStr(String field){
		return data.str(field);
	}
	public int getInt(String field){
		return data.integer(field);
	}
	public Line getLine(String field){
		return data.line(field);
	}
	
	
	public String find(String regex, String source){
		 return find(regex, false, source);
	}
	public String find(String regex, boolean decode, String source){
		Line data = FuncStr.findall(regex, source);
		if (data.length() <= 0){
			return null;
		}
		String data_str = data.str(0);
		if (decode) {
			data_str = data_str.replace("&amp;", "&");
		}
		return data_str;
	}
	public String findField(String field, String source) {
		return findField(field, false, source);
	}
	public Line copyField(Line line, String source, String... fields){
		for (String field: fields){
			line.put(field, findField(field, source));
		}
		return line;
	}
	public String findField(String field, boolean decode, String source) {
		String data = find("name=\"" + field + "\"[^>]+value=\"(([^\"]+))\"", decode, source);
		return data;
	}
	public boolean findFieldExist(String field, String source){
		return findField(field, source).length() > 0;
	}
	public String findXMLHref(String source){
		return findXMLHref(0, source);
	}
	public String getFormAction(String source){
		return find("<form[^>]+action=\"(([^\"]+))\"", source);
	}
	public String findXMLHref(int index, String source){
		return FuncStr.findall("go\\shref=\"(([^\"]+))\"", source).str(index).replace("&amp;", "&");
	}
}
