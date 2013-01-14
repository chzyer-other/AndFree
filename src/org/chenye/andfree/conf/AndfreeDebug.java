package org.chenye.andfree.conf;

public class AndfreeDebug {
	public static boolean ALL_DENIED = false; // denie all debug
	public static DebugField LOG = _f(true);
	
	protected static DebugField _f(boolean now, DebugField... depenys) {
		return new DebugField(now, depenys);
	}
}
