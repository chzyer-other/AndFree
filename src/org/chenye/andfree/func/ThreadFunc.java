package org.chenye.andfree.func;

import org.chenye.andfree.func.networkfunc.onCallbackRet;
import org.chenye.andfree.obj.Line;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class ThreadFunc {
	static class delay extends AsyncTask<Object, Object, View.OnClickListener>{
		
		@Override
		protected View.OnClickListener doInBackground(Object... params) {
			try {
				Thread.sleep((Integer) params[0]);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			// TODO Auto-generated method stub
			return (View.OnClickListener) params[1];
		}
		
		@Override
		protected void onPostExecute(View.OnClickListener obj){
			obj.onClick(null);
		}
	}
	public static void delay(int delay, View.OnClickListener click){
		new delay().execute(delay, click);
	}
	
	public static void async(int delay, final onCallbackRet... calls){
		class async extends AsyncTask<Object, Line, Line>{
			int index;
			@Override
			protected Line doInBackground(Object... indexs) {
				// TODO Auto-generated method stub
				index = (Integer) indexs[0];
				Line data = indexs.length > 1 ? (Line) indexs[1] : null;
				return calls[index].call(data);
			}
		
			@Override
			protected void onPostExecute(Line result) {
				// TODO Auto-generated method stub
				result = calls[index].ret(result);
				if (index >= calls.length - 1) return;
				new async().execute(index + 1, result);
			}
			
			@Override
			protected void onProgressUpdate(Line... values) {
				// TODO Auto-generated method stub
				
				super.onProgressUpdate(values);
			}
		}
		
		delay(delay, new View.OnClickListener() {
			
			public void onClick(View arg0) {
				// TODO Auto-generated method stub	
				new async().execute(0);
			}
		});
	}
	
	public static void hide_keybroad(Activity m){
		InputMethodManager imm = (InputMethodManager)m.getSystemService(Activity.INPUT_METHOD_SERVICE);
		if (m.getCurrentFocus() == null) return;
		IBinder windowtoken = m.getCurrentFocus().getWindowToken();
		imm.hideSoftInputFromWindow(windowtoken, InputMethodManager.HIDE_NOT_ALWAYS);
	}
	
		
	public static void getConversation(Context m, int thread){
		m.startActivity(getConversationIntent(thread));
	}
	
	public static Intent getConversationIntent(int thread){
		return new Intent()
		.setClassName("com.android.mms", "com.android.mms.ui.ComposeMessageActivity")
		.setData(Uri.parse("content://mms-sms/conversations/" + thread));
	}
	
	public static boolean isFlyme(){
		int version = Integer.parseInt(android.os.Build.VERSION.SDK);
		return version >= 15;
	}
}
