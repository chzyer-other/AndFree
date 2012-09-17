package org.chenye.andfree.obj;

import org.chenye.andfree.R;

import android.view.View;
import android.widget.ImageView;

public class baseWidget{
	public void setImg(String label, int id){
		
	}
	
	protected static widgetHelper edt(int id){
		return new widgetHelper("edt", id);
	}
	
	protected static widgetHelper img(int id, int wrap_id){
		return new widgetHelper("img", id, wrap_id);
	}
	
	protected static widgetHelper img(int id){
		return new widgetHelper("img", id);
	}
	
	protected static widgetHelper txt(int id){
		return new widgetHelper("txt", id);
	}
	
	protected static widgetHelper btn(int id){
		return new widgetHelper("btn", id);
	}
	
	protected static widgetHelper lly(int id){
		return new widgetHelper("lly", id);
	}
	
	protected static widgetHelper scv(int id){
		return new widgetHelper("scv", id);
	}
	
	public static widgetHelper rly(int id){
		return new widgetHelper("rly", id);
	}
	
	public static widgetHelper seek(int id){
		return new widgetHelper("seek", id);
	}
	
	public static widgetHelper inp(int id){
		return new widgetHelper("logininput", id);
	}
	
	public static widgetHelper pgb(int id){
		return new widgetHelper("processbar", id);
	}
	
	public static widgetHelper hide(dbActivity m, int id){
		widgetHelper w = new widgetHelper("View", id);
		w.obj().setVisibility(View.GONE);
		return null;
	}
	
	public static ImageView selectWrapImage(View v, widgetHelper w1, widgetHelper w2){
		return v.equals(w1.objWrap()) ? w1.img() : w2.img(); 
	}
	
	public static ImageView selectWrapOtherImage(View v, widgetHelper w1, widgetHelper w2){
		return v.equals(w1.objWrap()) ? w2.img() : w1.img();
	}
	
	public static int switchButton(View v, widgetHelper w1, widgetHelper w2, int[] icon){
		ImageView me = selectWrapImage(v, w1, w2);
		ImageView other = selectWrapOtherImage(v, w1, w2);
		if (me.getTag(R.id.tag).equals(0)){
			me.setImageResource(icon[1]);
			other.setImageResource(icon[0]);
			me.setTag(R.id.tag, 1);
			other.setTag(R.id.tag, 0);
		}
		return (Integer) me.getTag(R.id.id);
	}
	
	public static boolean equals(View v, widgetHelper... widgets){
		for (widgetHelper widget:widgets){
			if (widget.equals(v)) return true;
		}
		return false;
	}
	
}