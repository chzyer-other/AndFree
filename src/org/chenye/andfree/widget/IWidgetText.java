package org.chenye.andfree.widget;

public interface IWidgetText<T> {
	public boolean isTextEmpty();
	public String getText();
	public boolean isTextEqual(String str);
	public T setText(String str);
	/**
	 * set Text to ""
	 * @return
	 */
	public T setEmptyText();
	public T setTextSize(int dp);
	public T setTextColor(int color);
	public T setTextColor(String color);
	
	public T setHint(String hint);
	public T setGravity(int gravity);
}
