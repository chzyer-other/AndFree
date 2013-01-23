package org.chenye.andfree.conf;

import java.lang.reflect.Field;

import org.chenye.andfree.conf.AndfreeHook.HookItem;
import org.chenye.andfree.db.DB;
import org.chenye.andfree.func.FuncClass;
import org.chenye.andfree.msgpack.IDictPicker;
import org.chenye.andfree.msgpack.IListPicker;
import org.chenye.andfree.msgpack.MsgPackUnpack;
import org.chenye.andfree.msgpack.MsgPackUnpack.IGetDictPicker;
import org.chenye.andfree.msgpack.MsgPackUnpack.IGetListPicker;
import org.chenye.andfree.obj.AFActivity;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.widget.IWidget;

public class AndfreeHookSetup implements HookItem{
	private AndfreeHookSetup() {}
	private static AFActivity ba;
	private static boolean setuped = false;
	public static void setup(AFActivity act){
		if (setuped) return;
		setuped = true;
		ba = act;
		AndfreeStaticConfigure.APPLICATION_PACKAGE = act.getPackageName();
		AndfreeHook.AddHook(new AndfreeHookSetup());
	}
	
	public void onProgramStart() {
		setupDatabaseCore();
		setupConf();
		setupDatabase(ba);
		setupMsgpack();
		setupIWidgetLayoutRange();
		setupWidth();
	}

	public void onActivityEnter(AFActivity bact) {
	}

	public void onActivityLeave(AFActivity bact) {
		
	}
	
	public void setupDatabase(AFActivity bact){
		DB.getInstance(bact);
	}
	
	public void setupWidth(){
		AndfreeConf.SCREEN_WIDTH = ba.getWidth(); 
	}
	
	private void setupDatabaseCore(){
		AndfreeStaticConfigure.DBCORE_PACKAGE = String.format("%s._andfree.dbcore", AndfreeStaticConfigure.APPLICATION_PACKAGE);
	}
	
	private void setupIWidgetLayoutRange(){
		Class<?> r = FuncClass.GetApplicationClass("R");
		if (r == null) return;
		
		Class<?> layout = FuncClass.FindClass(r, "layout");
		if (layout == null) return;
		
		long min = 0;
		long max = 0;
		for (Field f: layout.getFields()){
			try {
				long i = f.getLong(layout);
				if (i < min || min == 0){
					min = i;
				}
				if (i > max || max == 0){
					max = i;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		IWidget.UpdateLayoutRange(min, max);
	}
	
	private void setupConf(){
		Class<?> conf = FuncClass.GetApplicationClass("_andfree.Conf");
		if (conf == null) return;
		
		Field[] fs = conf.getFields();
		for (Field f: fs){
			try {
				Object obj = f.get(conf);
				AndfreeConf.class.getField(f.getName()).set(AndfreeConf.class, obj);
			} catch (Exception e) {
			}
		}
	}
	
	private void setupMsgpack(){
		MsgPackUnpack.setGetDict(new IGetDictPicker() {
			
			public IDictPicker ret() {
				return new Line();
			}
		});
		
		MsgPackUnpack.setGetList(new IGetListPicker() {
			
			public IListPicker ret() {
				return new Line();
			}
		});
	}

}
