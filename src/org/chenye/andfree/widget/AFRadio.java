package org.chenye.andfree.widget;

import android.widget.RadioButton;

public class AFRadio extends IWidget<AFRadio, RadioButton>{
	
	public AFRadio(RadioButton c){
		super(c);
	}

	public AFRadio(int id){
		super(id, RadioButton.class);
	}
	

	public boolean isChecked(){
		return _e.isChecked();
	}
	
	public AFRadio check(){
		_e.setChecked(true);
		return this;
	}
	
	public AFRadio uncheck(){
		_e.setChecked(false);
		return this;
	}

	public AFRadio copy() {
		return new AFRadio(newChildInstance());
	}

}
