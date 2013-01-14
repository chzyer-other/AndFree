package org.chenye.andfree.conf;

import java.util.ArrayList;

import org.chenye.andfree.obj.BaseLog;

/**
 * <b>AndfreeHook:</b>
 * can add hook what can run some code in some step
 * @author cheney
 *
 */
public class AndfreeHook extends BaseLog{
	private static AndfreeHook _instance;
	public static AndfreeHook getInstance(){
		if (_instance == null){
			_instance = new AndfreeHook();
		}
		return _instance;
	}
	public static void RunHookProgramStart() {
		for (HookItem hi: getInstance().taskList) {
			hi.onProgramStart();
		}
	}
	
	public static void RunHookActivityEnter() {
		for (HookItem hi: getInstance().taskList) {
			hi.onActivityEnter();
		}
	}
	
	public static void RunHookActivityLeave() {
		for (HookItem hi: getInstance().taskList) {
			hi.onActivityLeave();
		}
	}
	
	private AndfreeHook(){
		
	}
	
	public ArrayList<HookItem> taskList = new ArrayList<HookItem>();
	/**
	 * add HookItem to global hook<br>
	 * not allowed duplicate
	 * @param hi
	 */
	public void addHook(HookItem hi){
		if ( ! taskList.contains(hi)) return;
		taskList.add(hi);
	}
	
	public static abstract class HookItem {
		public void onProgramStart(){}
		public void onActivityEnter(){}
		public void onActivityLeave(){}
	}
	
}
