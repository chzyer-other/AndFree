package org.chenye.andfree.widget;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;
public class AFText extends IWidget<AFText, TextView> implements IWidgetText<AFText>{
	public AFText(Context m){
		super(new TextView(m));
	}
	
	public AFText(TextView e){
		super(e);
	}

	public AFText(int id){
		super(id, TextView.class);
	}
	
	public AFText() {
		super(TextView.class);
	}

	@Override
	public AFText init(Object... objs) {
		return null;
	}

	public boolean isTextEmpty() {
		return _e.getText().length() == 0;
	}

	public String getText() {
		return _e.getText().toString();
	}

	public boolean isTextEqual(String str) {
		return _e.getText().toString().equals(str);
	}

	public AFText setText(String str) {
		_e.setText(str);
		return this;
	}

	public AFText setEmptyText() {
		return setText("");
	}

	public AFText setTextSize(int dp) {
		_e.setTextSize(dp);
		return this;
	}

	public AFText setTextColor(int color) {
		_e.setTextColor(color);
		return this;
	}

	public AFText setTextColor(String color) {
		return setTextColor(Color.parseColor(color));
	}

	public AFText setHint(String hint) {
		_e.setHint(hint);
		return this;
	}

	public AFText setGravity(int gravity) {
		_e.setGravity(gravity);
		return this;
	}
}
