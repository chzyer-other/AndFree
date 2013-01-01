package org.chenye.andfree.msgpack;

import java.util.Iterator;

public interface IListPicker{
	public Object put(Object key);
	public int length();
	public Iterator<Object> values();
	public boolean isListValid();
}