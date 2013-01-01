package org.chenye.andfree.widget;

public interface IWidgetText<T> {
	public boolean isTextEmpty();
	public String getText();
	public boolean isTextEqual(String str);
	public T setText(String str);
	public T emptyText();
	public T setTextSize(int dp);
	public T setTextColor(int color);
	public T setTextColor(String color);
	
	public T setHint(String hint);
}
