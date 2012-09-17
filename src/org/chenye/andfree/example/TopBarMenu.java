package org.chenye.andfree.example;

import org.chenye.andfree.obj.Line;
import org.chenye.andfree.widget.ItemIntent;
import org.chenye.andfree.widget.GroupCategory;

import android.os.Bundle;
import android.view.View;

public class TopBarMenu extends Menu{
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	};
	
	void init(){
		GroupCategory cate = getContainer();
		cate.addItem(new ItemIntent(m, "关于", new View.OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setResult(new Line().put("action", "about")).finish();
			}
		}));
		cate.inMenu().make();
		
	}
}
