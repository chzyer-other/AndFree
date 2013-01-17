package org.chenye.andfree.widget;

import org.chenye.andfree.func.FuncThread;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class AFEdit extends IWidget<AFEdit, EditText> implements IWidgetText<AFEdit>{

	public AFEdit(EditText e) {
		super(e);
	}
	
	public AFEdit(int id){
		super(id, EditText.class);
	}

	@Override
	public AFEdit init(Object... objs) {
		return null;
	}

	public boolean isTextEmpty() {
		return _e.getText().length() == 0;
	}

	public String getText() {
		return _e.getText().toString();
	}

	public boolean isTextEqual(String str) {
		return _e.getText().toString().equals(str);
	}

	public AFEdit setText(String str) {
		_e.setText(str);
		return self;
	}

	public AFEdit setEmptyText() {
		return setText("");
	}

	public AFEdit setTextSize(int dp) {
		_e.setTextSize(dp);
		return this;
	}

	public AFEdit setTextColor(int color) {
		_e.setTextColor(color);
		return this;
	}

	public AFEdit copy() {
		return new AFEdit(newChildInstance());
	}

	public AFEdit setTextColor(String color) {
		return setTextColor(Color.parseColor(color));
	}

	public AFEdit setMaxWidth(int dp){
		_e.setMaxWidth(px(dp));
		return this;
	}

	public AFEdit setHint(String hint) {
		_e.setHint(hint);
		return this;
	}

	public AFEdit bindOnEnterPress(final IWidget<?, ?> iw){
		_e.setImeOptions(EditorInfo.IME_ACTION_GO);
		_e.setOnKeyListener(new View.OnKeyListener() {
			
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP){
					hideKeybroad();
					FuncThread.delay(200, new View.OnClickListener() {
						
						public void onClick(View v) {
							iw.click();
						}
					});
					return true;
				}
				return false;
			}
		});
		return this;
	}
	
	public AFEdit hideKeybroad(){
		InputMethodManager im = (InputMethodManager) _m.getSystemService(Context.INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(_e.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		return this;
	}

	public interface OnFocusChange {
		public void onChange(boolean isOpenKeyboard);
	}
	
	OnFocusChange _ofc;
	/**
	 * Listener for Whether EditText got Focus; Must cooperate with a ExpandWidgetScroll
	 * @param layout ExpandWidgetScroll
	 * @param ofc OnFocusChange
	 */
	public void setOnFocusChange(ExpandWidgetScroll layout, OnFocusChange ofc) {
		layout.setAFEditFocusHandler(this);
		_ofc = ofc;
	}
	
	public void onFocus() {
		_ofc.onChange(true);
	}

	public void onLostFocus() {
		_ofc.onChange(false);
	}
}
