package org.chenye.andfree.func;

import org.chenye.andfree.func.FuncNet.onCallbackRet;
import org.chenye.andfree.obj.Line;

import android.os.AsyncTask;
import android.view.View;

public class FuncThread {
	static class delay extends AsyncTask<Object, Object, View.OnClickListener>{
		
		@Override
		protected View.OnClickListener doInBackground(Object... params) {
			try {
				Thread.sleep((Integer) params[0]);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
			return (View.OnClickListener) params[1];
		}
		
		@Override
		protected void onPostExecute(View.OnClickListener obj){
			obj.onClick(null);
		}
	}
	
	/**
	 * delay some time and execute some callback
	 * @param delay
	 * @param click
	 */
	public static void delay(int delay, View.OnClickListener click){
		if (delay <= 0) {
			click.onClick(null);
			return;
		}
		new delay().execute(delay, click);
	}
	
	/**
	 * execute some function in async
	 * @param calls
	 */
	public static void async(onCallbackRet... calls){
		async(0, calls);
	}
	
	/**
	 * execute some function in async and delay
	 * @param delay
	 * @param calls
	 */
	public static void async(int delay, final onCallbackRet... calls){
		class async extends AsyncTask<Object, Line, Line>{
			int index;
			@Override
			protected Line doInBackground(Object... indexs) {
				index = (Integer) indexs[0];
				Line data = indexs.length > 1 ? (Line) indexs[1] : null;
				return calls[index].call(data);
			}
		
			@Override
			protected void onPostExecute(Line result) {
				result = calls[index].ret(result);
				if (index >= calls.length - 1) return;
				new async().execute(index + 1, result);
			}
			
			@Override
			protected void onProgressUpdate(Line... values) {
				super.onProgressUpdate(values);
			}
		}
		
		delay(delay, new View.OnClickListener() {
			
			public void onClick(View arg0) {
				new async().execute(0);
			}
		});
	}
}
