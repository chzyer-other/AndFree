package org.chenye.andfree.obj;

import org.chenye.andfree.conf.AndfreeConf;
import org.chenye.andfree.db.DB;
import org.chenye.andfree.func.StrFunc;
import org.chenye.andfree.func.log;
import org.chenye.andfree.obj.ActivityResult.onActivityResult;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

public class dbActivity extends Activity{
	protected DB db;
	protected dbActivity m = this;
	public ProgressDialog mpDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndfreeConf.update(this);
        db = DB.getInstance(m);
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void toast(String str){
		log.toast(m, str);
	}
	
	int width = 0;
	public int getWidthPix(){
		if (width > 0) return width;
		return getWindowManager().getDefaultDisplay().getWidth();
	}
	
	public int getWidth(){
    	return StrFunc.pixel2dp(m, getWidthPix());
    }
	public int getHeight(){
		return getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public void setResultAndFinish(Intent data){
		setResult(true, data);
		finish();
	}
	
	public dbActivity setResult(Line line){
		setResult(true, line.toIntent());
		return this;
	}
	
	public void setResult(boolean result_ok, Intent data){
		setResult(result_ok ? Activity.RESULT_OK : Activity.RESULT_CANCELED, data);
	}
	
	protected void e(Object str){
		log.e(this, str);
	}
	
	protected void i(Object obj){
		log.i(this, obj);
	}

	ActivityResult activityResult = new ActivityResult();
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		activityResult.call(requestCode, resultCode, data);
	}
	
	public void registerActivityResult(String command, onActivityResult callback){
		activityResult.register(command, callback);
	}
	
	public void unregisterActivityResult(String command){
		activityResult.unregister(command);
	}
	
	public int getActivityResultCode(String key){
		return activityResult.getCode(key);
	}
	
}
