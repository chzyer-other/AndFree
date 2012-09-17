package org.chenye.andfree.obj;

import java.util.Hashtable;

import android.app.Activity;
import android.content.Intent;

public class ActivityResult {
	Hashtable<Integer, onActivityResult> activityRegisters = new Hashtable<Integer, onActivityResult>();
	Hashtable<String, Integer> activityRegistersKey = new Hashtable<String, Integer>();
	int auto_create = 0;
	public void register(String command, onActivityResult callback){
		if (activityRegistersKey.containsKey(command)){
			activityRegisters.remove(activityRegistersKey.remove(command));
		}
		activityRegistersKey.put(command, auto_create);
		activityRegisters.put(auto_create, callback);
		auto_create++;
	}
	
	public void unregister(String command){
		int index = activityRegistersKey.remove(command);
		activityRegisters.remove(index);
	}
	
	public int getCode(String key){
		if (activityRegistersKey.containsKey(key)){
			return activityRegistersKey.get(key);
		}else{
			return -1;
		}
	}
	
	public void call(int requestCode, int resultCode, Intent data){
		onActivityResult result = activityRegisters.remove(requestCode);
		if (result == null) return;
		result.call(resultCode == Activity.RESULT_OK, data);
	}
	
	public interface onActivityResult {
		public void call(boolean result_ok, Intent data);
	}
}
