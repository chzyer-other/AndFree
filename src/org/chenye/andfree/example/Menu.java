package org.chenye.andfree.example;

import org.chenye.andfree.R;
import org.chenye.andfree.func.StrFunc;
import org.chenye.andfree.func.timefunc;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.obj.dbActivity;
import org.chenye.andfree.widget.ItemIntent;
import org.chenye.andfree.widget.ItemText;
import org.chenye.andfree.widget.GroupCategory;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;

public class Menu extends dbActivity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);
		overridePendingTransition(R.anim.submenu_enter, android.R.anim.fade_out);
		super.onCreate(savedInstanceState);
		
		Line l = new Line(getIntent());
		setContentView(R.layout.andfree_example_menu);
		findViewById(R.id.parent).setOnClickListener(finish);
		View v = findViewById(R.id.scrollView1);
		AbsoluteLayout.LayoutParams lp = (AbsoluteLayout.LayoutParams) v.getLayoutParams();
		if (l.str("x").equals("right")){
			l.put("x", getWidthPix());
		}
		
		lp.x = l.integer("x") - StrFunc.dp2pix(this, 200);
		lp.y = l.integer("y");
		v.setLayoutParams(lp);
		
		if (l.contains("press")){
			pressCount = l.integer("press");
		}
	}
	
	int pressCount = -1;
	
	public GroupCategory getContainer(){
		return new GroupCategory(m, (ViewGroup) findViewById(R.id.linearLayout1));
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		
		super.onDestroy();
	}
	
	View.OnClickListener finish = new View.OnClickListener() {
		
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
			overridePendingTransition(android.R.anim.fade_in, R.anim.submenu_exit);
		}
	};
	
	boolean enable_down = false;
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		enable_down = true;
		return super.onKeyUp(keyCode, event);
	};
	
	int count = 0;
	long presstime = 0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == 4) {
			finish.onClick(null);
			return true;
		}
		
		if ( ! enable_down) return false;
		if (keyCode == 82){
			finish.onClick(null);
			return true;
		}
		
		
		if (keyCode == 24){
			i(timefunc.time() - presstime);
			if (timefunc.time() - presstime < 500 && presstime > 0 && timefunc.time() - presstime > 100){
				count++;
				if (count >= pressCount){
					setResult(new Line().put("action", "press_" + pressCount));
					finish();
				} else {
					presstime = timefunc.time(); 
				}
			} else {
				count = 1;
				presstime = timefunc.time(); 
			}
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
