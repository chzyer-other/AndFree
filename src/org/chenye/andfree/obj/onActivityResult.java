package org.chenye.andfree.obj;

import android.content.Intent;

public class onActivityResult {
	public void callback(boolean result_ok, Intent data){
		Line line = null;
		if (data != null && data.getDataString() != null) {
			line = new Line(data.getDataString());
		}
		callback(result_ok, line);
	}
	public void callback(boolean result_ok, Line data){}
}