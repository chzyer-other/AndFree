package org.chenye.andfree.widget;

import android.graphics.Color;
import android.widget.Button;

public class AFButton extends IWidget<AFButton, Button> implements IWidgetText<AFButton>{
	public AFButton(Button e) {
		super(e);
	}
	public AFButton(){
		super(Button.class);
	}
	
	public AFButton(int id){
		super(id, Button.class);
	}

	@Override
	public AFButton init(Object... objs) {
		return null;
	}
	
	public AFButton copy() {
		return new AFButton();
	}
	
	public boolean isTextEmpty() {
		return false;
	}
	
	public String getText() {
		return _e.getText().toString();
	}
	
	public boolean isTextEqual(String str) {
		return getText().equals(str);
	}
	
	public AFButton setText(String str) {
		_e.setText(str);
		return this;
	}
	
	public AFButton setEmptyText() {
		setText("");
		return this;
	}
	
	public AFButton setTextSize(int dp) {
		_e.setTextSize(dp);
		return this;
	}
	
	public AFButton setTextColor(int color) {
		_e.setTextColor(color);
		return this;
	}
	
	public AFButton setTextColor(String color) {
		return setTextColor(Color.parseColor(color));
	}
	
	public AFButton setHint(String hint) {
		_e.setHint(hint);
		return this;
	}
	
	

}
