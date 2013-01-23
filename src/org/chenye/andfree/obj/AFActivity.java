package org.chenye.andfree.obj;

import org.chenye.andfree.conf.AndfreeHook;
import org.chenye.andfree.conf.AndfreeHookSetup;
import org.chenye.andfree.func.FuncStr;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class AFActivity extends Activity{
	protected AFActivity m = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
    			onLine(new Line(uri.toString()));
    		}
    	}
    	onInit();
    }
    protected void onInit(){}
    protected void onLine(Line data){}
    
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
	
	onActivityResult _activity_result;
	public void startActivityForResult(Class<?> cls, onActivityResult result){
		Intent intent = new Intent(this, cls);
		_activity_result = result;
		startActivityForResult(intent, 0);
	}
	
	public void startActivityForResult(Class<?> cls, Line data, onActivityResult result) {
		_activity_result = result;
		startActivityForResult(data.toIntent(this, cls), 0);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && _activity_result != null) {
			_activity_result.callback(resultCode != 0, data);
			_activity_result = null;
		}
	}

	
}
