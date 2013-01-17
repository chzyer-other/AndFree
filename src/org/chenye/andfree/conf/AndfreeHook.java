package org.chenye.andfree.conf;

import java.util.ArrayList;

import org.chenye.andfree.obj.AFActivity;
import org.chenye.andfree.obj.AFLogActivity;

/**
 * <b>AndfreeHook:</b>
 * can add hook what can run some code in some step <br>
 * <h2>How to add Hook</h2>
 * <code>
 * AndfreeHook.AddHook(new HookItem(){<br>
 * 		public void onProgramStart(){<br>
 * 			//do some code when program start<br>
 * 		}<br>
 * });
 * </code>
 *
 */
public class AndfreeHook extends AFLogActivity{
	private static AndfreeHook _instance;
	public static AndfreeHook getInstance(){
		if (_instance == null){
			_instance = new AndfreeHook();
		}
		return _instance;
	}
	
	public static boolean isFirstProgramStarts = true;
	public static void RunHookProgramStart() {
		if ( ! isFirstProgramStarts) return;
		for (HookItem hi: getInstance().taskList) {
			hi.onProgramStart();
		}
		isFirstProgramStarts = false;
	}
	
	public static void RunHookActivityEnter(AFActivity bact) {
		for (HookItem hi: getInstance().taskList) {
			hi.onActivityEnter(bact);
		}
	}
	
	public static void RunHookActivityLeave(AFActivity bact) {
		for (HookItem hi: getInstance().taskList) {
			hi.onActivityLeave(bact);
		}
	}
	
	private AndfreeHook(){}
	
	private ArrayList<HookItem> taskList = new ArrayList<HookItem>();
	/**
	 * add HookItem to global hook<br>
	 * ignore if duplicate
	 * @param hi
	 */
	public void addHook(HookItem hi){
		if (taskList.contains(hi)) return;
		taskList.add(hi);
	}
	
	public static void AddHook(HookItem hi){
		getInstance().addHook(hi);
	}
	
	public interface HookItem {
		public void onProgramStart();
		public void onActivityEnter(AFActivity bact);
		public void onActivityLeave(AFActivity bact);
	}
	
}
