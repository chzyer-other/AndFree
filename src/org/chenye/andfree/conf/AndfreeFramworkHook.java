package org.chenye.andfree.conf;

import org.chenye.andfree.conf.AndfreeHook.HookItem;

public class AndfreeFramworkHook extends HookItem{
	public AndfreeFramworkHook(){
		AndfreeHook.getInstance().addHook(this);
	}

	@Override
	public void onProgramStart() {
		
	}

	@Override
	public void onActivityEnter() {
		
	}

	@Override
	public void onActivityLeave() {
		
	}
}
