package org.chenye.andfree.obj;

import java.util.ArrayList;

public class ArrayListRange {
	private ArrayList<Long> list = new ArrayList<Long>();
	public final static int ORDER_DESC = 0;
	public final static int ORDER_ASC = 1;

	private int _order;

	private ArrayListRange(int order) {
		_order = order;
	}

	public static ArrayListRange DESC(){
		return new ArrayListRange(ORDER_DESC);
	}

	public static ArrayListRange ASC(){
		return new ArrayListRange(ORDER_ASC);
	}

	public int put(long obj) {
		int index = list.size();
		for (int i = 0; i < list.size(); i++) {
			if (isContinue(obj, list.get(i))) continue;
			index = list.indexOf(list.get(i));
			break;
		}

		list.add(index, obj);
		return index;
	}

	public long get(int index) {
		return list.get(index);
	}

	private boolean isContinue(Long target, Long inList) {
		if (isASC()) {
			return target > inList;
		}

		return target < inList;
	}

	private boolean isDESC(){
		return _order == ORDER_DESC;
	}

	private boolean isASC(){
		return _order == ORDER_ASC;
	}

	public long getMin() {
		if (list.size() == 0) return 0;
		if (isDESC()) {
			return list.get(list.size() - 1);
		}
		return list.get(0);
	}

	public long getMax() {
		if (isDESC()) {
			return list.get(0);
		}
		return list.get(list.size() - 1);
	}

	public static boolean Test() {
		ArrayListRange alr = ArrayListRange.DESC();
		alr.put(2);
		alr.put(1);
		alr.put(5);
		alr.put(4);
		assert alr.get(0) == 5 && alr.get(1) == 4 && alr.get(2) == 2 && alr.get(3) == 1;
		alr = ArrayListRange.ASC();
		alr.put(2);
		alr.put(1);
		alr.put(5);
		alr.put(4);
		assert alr.get(3) == 5 && alr.get(2) == 4 && alr.get(1) == 2 && alr.get(0) == 1;
		return true;
	}

	public void clear() {
		list.clear();
	}
}
