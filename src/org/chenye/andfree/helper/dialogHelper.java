package org.chenye.andfree.helper;

import java.util.ArrayList;

import org.chenye.andfree.R;
import org.chenye.andfree.conf.lang;
import org.chenye.andfree.func.ClsFunc;
import org.chenye.andfree.func.ClsFunc.clsFace;
import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.obj.Line;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class dialogHelper extends BaseLog{
	Context m;
	public static final int DIALOG = 0;
	public static final int PROCESS = 1;
	public static final int ALERT = 2;
	public static final int INPUT = 3;
	int style_type = DIALOG;
	AlertDialog.Builder dialog;
	ProgressDialog pDialog;
	Dialog d;
	
	static dialogHelper tmp_dialog;
	
	public static dialogHelper getInstance(){
		return tmp_dialog;
	}
	
	public static dialogHelper Input(Context mContext, Object... objs){
		if (tmp_dialog != null) tmp_dialog.dismiss();
		tmp_dialog = new dialogHelper(mContext, INPUT, objs).show();
		return tmp_dialog;
	}
	
	public static dialogHelper Alert(Context mContext, Object... objs){
		dialogHelper dialog = new dialogHelper(mContext, ALERT, objs);
		dialog.show();
		return dialog;
	}
	
	public static dialogHelper Process(Context mContext, Object... objs){
		if (tmp_dialog != null) tmp_dialog.dismiss();
		tmp_dialog = new dialogHelper(mContext, PROCESS, objs).show();
		return tmp_dialog;
	}
	
	public static void Dismiss(){
		if (tmp_dialog == null) return;
		tmp_dialog.dismiss();
		tmp_dialog = null;
	}
	
	public dialogHelper(Context mContext, int type, Object... objs){
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
			init_input(objs);
		}
	}
	
	public interface clickForString{
		public String call(String str);
	}
	
	clickForString dialogClick;
	public dialogHelper setClick(clickForString click){
		dialogClick = click;
		return this;
	}
	
	
	public void init_input(Object... objs){
		LayoutInflater inflater = LayoutInflater.from(m);
		ViewGroup container = (ViewGroup) inflater.inflate(R.layout.andfree_view_edit, null);
		final EditText et = (EditText) container.findViewById(R.id.andfree_editText1);
		dialog.setView(container);
		for (Object o:objs){
			ClsFunc.getClassChoise(o, new clsFace(){
				@Override
				public void string(String obj) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (dialogClick == null) return;
				dialogClick.call(et.getText().toString());
				
			}
		});
		dialog.setNegativeButton("取消", null);
	}
	
	public void init_alert(Object... objs){
		dialog.setMessage(objs[0] + "");
		DialogInterface.OnClickListener click = null;
		DialogInterface.OnClickListener cancel = null;
		String navName = "确定";
		for (int i=1; i<objs.length; i++){
			String type = ClsFunc.getClassName(objs[i]);
			if (type.equals("DialogInterface.OnClickListener")) {
				if (click == null) click = (DialogInterface.OnClickListener) objs[i];
				else cancel = (DialogInterface.OnClickListener) objs[i];
			} else if (type.equals("DialogInterface.OnCancelListener")){
			
			} else if (type.equals("String")){
				navName = objs[i] + "";
			}
		}
		if (navName != null && click != null){
			dialog.setPositiveButton(navName, click);
		}
		dialog.setNegativeButton(lang.cancel, cancel);
	}
	
	public dialogHelper(Context mContext){
		this(mContext, DIALOG);
	}
	
	ArrayList<String> items_label = new ArrayList<String>();
	ArrayList<Line> items_Line = new ArrayList<Line>();
	ArrayList<OnClickListener> items_click = new ArrayList<View.OnClickListener>();
	dataClick items_dialog_click;
	public dialogHelper addItem(String hint, View.OnClickListener click){
		items_label.add(hint);
		items_click.add(click);
		return this;
	}
	
	public dialogHelper addItem(String hint, Line data){
		items_label.add(hint);
		items_Line.add(data);
		return this;
	}
	
	public dialogHelper setDialogClick(dataClick mClick){
		items_dialog_click = mClick;
		return this;
	}
	
	
	public dialogHelper show(){
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
	
	public dialogHelper setTitle(String title){
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
			ClsFunc.getClassChoise(obj, new clsFace(){
				@Override
				public void string(String obj) {
					// TODO Auto-generated method stub
					pDialog.setMessage(obj);
				}
				
				@Override
				public void integer(int obj) {
					// TODO Auto-generated method stub
					setProcessMax(obj);
				}
			});
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
	
	public dialogHelper setProcessMax(int max){
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
	
	public interface dataClick{
		public void onClick(Line data);
	}
	
	
}
