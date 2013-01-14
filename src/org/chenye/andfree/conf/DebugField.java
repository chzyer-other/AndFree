package org.chenye.andfree.conf;


public class DebugField {
	private boolean now = true;
	private int w = 0;
	private DebugField[] depanys;

	public DebugField(boolean now, DebugField... depany) {
		this.now = now;
		depanys = depany;
	}

	public boolean NOTDO() {
		return !DO();
	}

	public boolean DO() {
		if (AndfreeDebug.ALL_DENIED)
			return false;
		for (int i = 0; i < depanys.length; i++) {
			if (depanys[i].inWhen()) {
				return depanys[i].DO();
			}
		}
		return now;
	}

	public DebugField when(boolean mW) {
		w = mW ? 1 : 2;
		return this;
	}

	public boolean inWhen() {
		if (w == 0)
			return false;
		boolean ret = w == 1 ? true : false;
		return DO() == ret;
	}
}
