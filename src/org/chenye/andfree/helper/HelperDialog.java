package org.chenye.andfree.helper;

import java.util.ArrayList;

import org.chenye.andfree.conf.AndfreeLang;
import org.chenye.andfree.obj.AFLogObj;
import org.chenye.andfree.obj.Line;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;

public class HelperDialog extends AFLogObj{
	Context m;
	public static final int DIALOG = 0;
	public static final int PROCESS = 1;
	public static final int ALERT = 2;
	public static final int INPUT = 3;
	int style_type = DIALOG;
	AlertDialog.Builder dialog;
	ProgressDialog pDialog;
	Dialog d;
	
	static HelperDialog tmp_dialog;
	
	public static HelperDialog getInstance(){
		return tmp_dialog;
	}
	
	public static HelperDialog Input(Context mContext, Object... objs){
		if (tmp_dialog != null) tmp_dialog.dismiss();
		tmp_dialog = new HelperDialog(mContext, INPUT, objs).show();
		return tmp_dialog;
	}
	
	public static HelperDialog Alert(Context mContext, Object... objs){
		HelperDialog dialog = new HelperDialog(mContext, ALERT, objs);
		dialog.show();
		return dialog;
	}
	
	public static HelperDialog Process(Context mContext, Object... objs){
		if (tmp_dialog != null) tmp_dialog.dismiss();
		tmp_dialog = new HelperDialog(mContext, PROCESS, objs).show();
		return tmp_dialog;
	}
	
	public static void Dismiss(){
		if (tmp_dialog == null) return;
		tmp_dialog.dismiss();
		tmp_dialog = null;
	}
	
	public HelperDialog(Context mContext, int type, Object... objs){
		m = mContext;
		style_type = type;
		if (style_type == DIALOG){
			dialog = new AlertDialog.Builder(m);
		} else if (style_type == PROCESS) {
			pDialog = new ProgressDialog(m);
			init_process(objs);
		} else if (style_type == ALERT){
			dialog = new AlertDialog.Builder(m);
			init_alert(objs);
		} else if (style_type == INPUT){
			dialog = new AlertDialog.Builder(m);
		}
	}
	
	public interface clickForString{
		public String call(String str);
	}
	
	clickForString dialogClick;
	public HelperDialog setClick(clickForString click){
		dialogClick = click;
		return this;
	}
	
	public void init_alert(Object... objs){
		dialog.setMessage(objs[0] + "");
		DialogInterface.OnClickListener click = null;
		DialogInterface.OnClickListener cancel = null;
		String navName = "确定";
		for (int i=1; i<objs.length; i++){
			if (objs[i] instanceof DialogInterface.OnClickListener){
				if (click == null) click = (DialogInterface.OnClickListener) objs[i];
				else cancel = (DialogInterface.OnClickListener) objs[i];
			} else if (objs[i] instanceof DialogInterface.OnCancelListener){
			
			} else if (objs[i] instanceof String){
				navName = objs[i] + "";
			}
		}
		if (navName != null && click != null){
			dialog.setPositiveButton(navName, click);
		}
		dialog.setNegativeButton(AndfreeLang.cancel, cancel);
	}
	
	public HelperDialog(Context mContext){
		this(mContext, DIALOG);
	}
	
	ArrayList<String> items_label = new ArrayList<String>();
	ArrayList<Line> items_Line = new ArrayList<Line>();
	ArrayList<OnClickListener> items_click = new ArrayList<View.OnClickListener>();
	OnClickLine items_dialog_click;
	public HelperDialog addItem(String hint, View.OnClickListener click){
		items_label.add(hint);
		items_click.add(click);
		return this;
	}
	
	public HelperDialog addItem(String hint, Line data){
		items_label.add(hint);
		items_Line.add(data);
		return this;
	}
	
	public HelperDialog setLineClick(OnClickLine mClick){
		items_dialog_click = mClick;
		return this;
	}
	
	
	public HelperDialog show(){
		if (style_type == DIALOG) {
			show_item();
			d = dialog.create();
			d.show();
		} else if (style_type == PROCESS) {
			pDialog.show();
		} else if (style_type == ALERT){
			d = dialog.create();
			d.show();
		} else if (style_type == INPUT){
			d = dialog.create();
			d.show();
		}
		return this;
	}
	
	public HelperDialog setTitle(String title){
		if (dialog != null){
			dialog.setTitle(title);	
		}
		
		if (pDialog != null){
			boolean showing = pDialog.isShowing(); 
			if (showing){
				pDialog.hide();
			}
			pDialog.setMessage(title);
			if (showing){
				pDialog.show();
			}
		}
		return this;
	}
	
	public void dismiss(){
		if (style_type == DIALOG) {
			d.dismiss();
		} else if (style_type == PROCESS) {
			pDialog.dismiss();
		}
	}
	
	public void init_process(Object... objs){
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setIndeterminate(false);
		
		for (Object obj: objs){
			if (obj instanceof String){
				pDialog.setMessage((String) obj);
			} else if (obj instanceof Integer){
				setProcessMax((Integer) obj);
			}
		}
		
	}
	
	public static boolean ProcessUpdateAddBy(int count){
		if (tmp_dialog == null) return false;
		tmp_dialog.processAddBy(count);
		return true;
	}
	
	public static void ProcessUpdateLabel(String hint){
		if (tmp_dialog == null){
			return;
		}
		tmp_dialog.setTitle(hint.replace("%p", tmp_dialog.getProcessNow() + "").replace("%m", tmp_dialog.getProcessMax() + ""));
	}
	
	public int getProcessNow(){
		return pDialog.getProgress();
	}
	
	public int getProcessMax(){
		return pDialog.getMax();
	}
	
	public void processAddBy(int count){
		pDialog.setProgress(pDialog.getProgress() + count);
	}
	
	public HelperDialog setProcessMax(int max){
		if (style_type != PROCESS) {
			error("not support other withour Process!");
			return this;
		}
		
		pDialog.setMax(max);
		pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pDialog.setProgress(1);
		return this;
	}
	
	private void show_item(){
		if (items_label.size() <= 0) return;
		
		String[] items = new String[items_label.size()];
		int i = 0;
		while (items_label.size() > 0){
			items[i++] = items_label.get(0);
			items_label.remove(0);
		}
		
		dialog.setItems(items, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (items_dialog_click == null){
					items_click.get(which).onClick(null);
				} else {
					items_dialog_click.onClick(items_Line.get(which));
				}
			}
		});
	}
	
	public interface OnClickLine{
		public void onClick(Line data);
	}
	
	
}
