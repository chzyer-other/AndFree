package org.chenye.andfree.widget;

import android.widget.CheckBox;

public class AFCheck extends IWidget<AFCheck, CheckBox>{
	public AFCheck(){
		super(CheckBox.class);
	}
	
	public AFCheck(CheckBox e){
		super(e);
	}
	
	public AFCheck(int id){
		super(id, CheckBox.class);
	}

	public AFCheck setCheck(Boolean checked) {
		_e.setChecked(checked);
		return this;
	}
	
	public AFCheck check(){
		return setCheck(true);
	}
	
	public AFCheck uncheck(){
		return setCheck(false);
	}
	
	public boolean isChecked(){
		return _e.isChecked();
	}

}
