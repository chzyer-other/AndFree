package org.chenye.andfree.obj;

import org.chenye.andfree.conf.AndfreeHook;
import org.chenye.andfree.conf.AndfreeHookSetup;
import org.chenye.andfree.func.FuncStr;
import org.chenye.andfree.obj.ActivityResult.onActivityResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AFActivity extends Activity{
	protected AFActivity m = this;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getContext() == null){
        	instance = m;
        }
        AndfreeHookSetup.setup(this);
        AndfreeHook.RunHookProgramStart();
        AndfreeHook.RunHookActivityEnter(this);
        init();
    }
    
    private void init(){
    	Intent intent = getIntent();
    	
    	if (intent != null){
    		Uri uri = intent.getData();
    		if (uri != null){
    			onIntent(new Line(uri.toString()));
    		}
    	}
    	onInit();
    }
    protected void onInit(){}
    protected void onIntent(Line data){}
    
    protected static Context instance;
    public static Context getContext(){
    	return instance;
    }
	
	@Override
	public void onDestroy(){
		super.onDestroy();
	}
	
	public void toast(Object str){
		if (str instanceof Exception){
			str = ((Exception) str).getMessage();
		}
		AFLog.toast(m, str.toString());
	}
	
	int width = 0;
	public int getWidthPix(){
		if (width > 0) return width;
		return getWindowManager().getDefaultDisplay().getWidth();
	}
	
	public int getWidth(){
    	return FuncStr.pixel2dp(m, getWidthPix());
    }
	public int getHeight(){
		return getWindowManager().getDefaultDisplay().getHeight();
	}
	
	public void startActivity(Class<?> cls){
		Intent i = new Intent(this, cls);
		startActivity(i);
	}
	
	public void startActivity(Class<?> cls, Line ret){
		Intent i = new Intent(this, cls);
		i.setData(Uri.parse(ret.toString()));
		startActivity(i);
	}
	
	public void switchActivity(Class<?> cls){
		startActivity(cls);
		finish();
	}
	
	public void setResultAndFinish(Intent data){
		setResult(true, data);
		finish();
	}
	
	public AFActivity setResult(Line line){
		setResult(true, line.toIntent());
		return this;
	}
	
	public void setResult(boolean result_ok, Intent data){
		setResult(result_ok ? Activity.RESULT_OK : Activity.RESULT_CANCELED, data);
	}
	
	protected void error(Object str){
		AFLog.e(this, str);
	}
	
	public void log(Object obj){
		AFLog.i(this, obj);
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
