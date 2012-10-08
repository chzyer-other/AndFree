package org.chenye.andfree.obj;

import org.chenye.andfree.obj.ActivityResult.onActivityResult;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

public class baseMainResult extends BaseLog{
	String commandStr;
	Object tmp;
	BaseActivity m;
	
	public baseMainResult(String command) {
		commandStr = command;
	}

	protected static baseMainResult _f(Object... objs) {
		return new baseMainResult(objs[0].toString());
	}
	
	public static baseMainResult command(String command){
		return new baseMainResult(command);
	}

	public int CODE(BaseActivity m) {
		return m.getActivityResultCode(commandStr);
	}

	public baseMainResult TMP(Object mTmp){
		tmp = mTmp;
		return this;
	}
	
	public Object GET_TEMP(){
		return tmp;
	}
	
	public ViewGroup GET_VIEWGROUP(){
		return (ViewGroup) tmp;
	}
	
	public View GET_VIEW(){
		return (View) tmp;
	}
	
	private baseMainResult REGISTER(BaseActivity mContext, onActivityResult callback) {
		m = mContext;
		//i("register event " + commandStr + " => " + callback.hashCode());
		m.registerActivityResult(commandStr, callback);
		return this;
	}

	public void UNREGISTER() {
		m.unregisterActivityResult(commandStr);
	}
	
	public void SEND(BaseActivity mContext, onActivityResult callback, Class<?> cls, Line Line){
		m = mContext;
		Intent intent = new Intent(m, cls);
		intent.setData(Uri.parse(Line.toString()));
		SEND(mContext, callback, intent);
	}
	
	public void SEND(BaseActivity mContext, onActivityResult callback, Class<?> cls){
		m = mContext;
		SEND(mContext, callback, new Intent(m, cls));
	}
	
	public void SEND(BaseActivity mContext, onActivityResult callback, Intent intent){
		REGISTER(mContext, callback);
		m.startActivityForResult(intent, CODE(m));
	}
}

