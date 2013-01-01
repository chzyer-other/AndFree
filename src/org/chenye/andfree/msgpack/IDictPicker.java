package org.chenye.andfree.msgpack;

import java.util.Set;
import java.util.Map.Entry;

public interface IDictPicker {
	public Object put(Object key, Object value);
	public int length();
	public Set<Entry<Object, Object>> valueSet();
	public boolean isDictValid();
}
