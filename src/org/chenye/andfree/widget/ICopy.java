package org.chenye.andfree.widget;

/**
 * if want to use "newInstance" in WidgetList, PLEASE IMPLEMENTS THIS FIRST!
 * @param <T>
 */
public interface ICopy<T> {
	public T copy();
}
