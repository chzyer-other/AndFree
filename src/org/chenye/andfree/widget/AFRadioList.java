package org.chenye.andfree.widget;

import org.chenye.andfree.conf.S;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class AFRadioList extends IWidget<AFRadioList, AFRadio>{
	AFRadio[] radios;
	int hover = 0;
	public AFRadioList(AFRadio... objs){
		super(objs[0]._id, AFRadio.class);
		radios = objs;
	}
	
	public boolean isAllUncheck(){
		for (AFRadio f: radios){
			if (f.isChecked()){
				return false;
			}
		}
		return true;
	}

	@Override
	public AFRadioList init(Object... objs) {
		return null;
	}
	
	@Override
	public void inflate(Activity a) {
		super.inflate(a);
		for (AFRadio r: radios){
			if ( ! r.isInflated()){
				r.inflate(a);
			}
		}
		bindListener();
	}
	
	@Override
	public void inflate(ViewGroup vg) {
		super.inflate(vg);
		for (AFRadio r: radios){
			if ( ! r.isInflated()){
				r.inflate(vg);
			}
		}
		bindListener();
	}
	
	void ensureInflate(Activity m){
		for (AFRadio r: radios){
			if ( ! r.isInflated()){
				r.inflate(m);
			}
		}
	}
	
	void bindListener(){
		for (int i=0; i<radios.length; i++){
			final AFRadio r = radios[i];
			if (i == 0){
				r.check();
			} else {
				r.uncheck();
			}
			final int index = i;
			r.setClick(new View.OnClickListener() {
				
				public void onClick(View arg0) {
					if (hover == index){
						return;
					}
					radios[hover].uncheck();
					hover = index;
					r.check();
				}
			});
			r.setTags(S.index, i);
		}
	}
}
