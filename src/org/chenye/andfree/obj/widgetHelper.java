package org.chenye.andfree.obj;

import org.chenye.andfree.R;
import org.chenye.andfree.func.ClsFunc;
import org.chenye.andfree.func.log;
import org.chenye.andfree.func.ClsFunc.clsFace;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class widgetHelper {
	private String type;
	private int id;
	private int wrapId;
	private View w;
	private View wrap_w;
	private Context m;
	private ViewGroup container;
	public widgetHelper(String mType, int mId, int mWrap){
		wrapId = mWrap;
		type = mType;
		id = mId;
	}
	
	@Override
	public boolean equals(Object obj){
		return obj.equals(w);
	}
	
	public void setContainer(ViewGroup vg){
		container = vg;
	}
	
	private View getView(int id){
		if (container != null){
			return container.findViewById(id);
		}else{
			return ((Activity) m).findViewById(id);
		}
	}
	
	public widgetHelper(String mType, int mId){
		this(mType, mId, -1);
	}
	
	public widgetHelper init(Context mContext, widgetHelper wh, Object... objs){
		init(mContext, wh.viewgroup(), objs);
		return this;
	}
	
	public widgetHelper init(Context mContext, ViewGroup vg, Object... objs){
		setContainer(vg);
		init(mContext, objs);
		setTags(R.id.parent, vg);
		return this;
	}
	
	public widgetHelper select(ViewGroup v){
		widgetHelper mWidget = new widgetHelper(type, id, wrapId);
		mWidget.init(m, v);
		return mWidget;
	}
	
	public void hide(Context mContext, ViewGroup vg){
		setContainer(vg);
		hide(mContext);
	}
	
	public void hide(Context mContext){
		m = mContext;
		w = getView(id);
		w.setVisibility(View.GONE);
	}
	
	public widgetHelper show(){
		w.setVisibility(View.VISIBLE);
		return this;
	}
	
	private clsFace init_common_face = new clsFace(){
		public void click(android.view.View.OnClickListener obj) {
			if (wrapId == -1){
				w.setOnClickListener((View.OnClickListener) obj);
			}else{
				wrap_w.setOnClickListener((View.OnClickListener) obj);
			}
			tmp_click = (View.OnClickListener) obj;
		};
		
		public void onKey(android.view.View.OnKeyListener obj) {
			w.setOnKeyListener(obj);
		};
		
		public void onFocusChange(OnFocusChangeListener obj) {
			w.setOnFocusChangeListener(obj);
		};
		
	};
	
	public widgetHelper init(Context mContext, Object... objs){
		m = mContext;
		w = getView(id);
		if (wrapId != -1){
			wrap_w = getView(wrapId);
			wrap_w.setTag(R.id.wrap_child, w);
		}
		
		for (Object obj:objs){
			ClsFunc.getClassChoise(obj, init_common_face);
		}
		
		if (type.equals("edt")){
			init_edt(objs);
		}else if (type.equals("txt")){
			init_txt(objs);
		}else if (type.equals("img")){
			init_img(objs);
		}else if (type.equals("seek")){
			init_seek(objs);
		}
		return this;
	}
	
	public widgetHelper setBackgroundColor(String color){
		w.setBackgroundColor(Color.parseColor(color));
		return this;
	}
	
	private void init_seek(Object... objs){
		final SeekBar sbr = (SeekBar) w;
		for (Object obj:objs){
			ClsFunc.getClassChoise(obj, new clsFace(){
				@Override
				public void integer(int obj) {
					// TODO Auto-generated method stub
					sbr.setMax(obj);
				}
				
				@Override
				public void widget(final widgetHelper obj) {
					// TODO Auto-generated method stub
					sbr.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
						
						public void onStopTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							
						}
						
						public void onStartTrackingTouch(SeekBar seekBar) {
							// TODO Auto-generated method stub
							
						}
						
						public void onProgressChanged(SeekBar seekBar, int progress,
								boolean fromUser) {
							// TODO Auto-generated method stub
							obj.setText(progress + "");
						}
					});
				}
				
				@Override
				public void seek(OnSeekBarChangeListener obj) {
					// TODO Auto-generated method stub
					sbr.setOnSeekBarChangeListener(obj);
				}
			});
		}
	}
	
	private void init_edt(Object... objs){
		final EditText edt = (EditText) w;
		for (Object obj:objs){
			ClsFunc.getClassChoise(obj, new clsFace(){
				public void string(String obj) {					
					edt.setText(obj);
				};
				
				@Override
				public void TextWatcher(android.text.TextWatcher obj) {
					// TODO Auto-generated method stub
					edt.addTextChangedListener(obj);
				}
			});
		}
	}
	
	private void init_txt(Object... objs){
		final TextView w_t = (TextView) w;
		for (Object obj:objs){
			ClsFunc.getClassChoise(obj, new clsFace(){
				@Override
				public void integer(int obj) {
					// TODO Auto-generated method stub
					w_t.setText(obj + "");
				}
				
				@Override
				public void string(String obj) {
					// TODO Auto-generated method stub
					w_t.setText(obj);
				}
			});
		}
	}
	
	private void init_img(Object... objs){
		final ImageView iv = (ImageView) w;
		for (Object obj:objs){
			ClsFunc.getClassChoise(obj, new clsFace(){
				@Override
				public void integer(int obj) {
					// TODO Auto-generated method stub
					iv.setImageResource(obj);
				}
				
				@Override
				public void bitmap(Bitmap obj) {
					// TODO Auto-generated method stub
					iv.setImageBitmap(obj);
				}
				
				@Override
				public void uri(Uri obj) {
					// TODO Auto-generated method stub
					iv.setImageURI(obj);
				}
			});
		}
	}
	
	private View.OnClickListener tmp_click;
	
	private String getText_edt(){
		return ((EditText) w).getText().toString();
	}
	
	public void setTags(Object... objs){
		for (int i=0; i<objs.length; i+=2){
			w.setTag(Integer.parseInt(objs[i].toString()), objs[i + 1]);
		}
	}
	
	public ImageView wrapImg(){
		return (ImageView) wrap_w;
	}
	
	public ImageView img(){
		return (ImageView) w;
	}
	
	public void setTextColor(String color){
		if (type.equals("txt")){
			txt().setTextColor(Color.parseColor(color));
		} else if (type.equals("edt")){
			edt().setTextColor(Color.parseColor(color));
		} else {
			e("unsupport type[" + type + "] for setTextColor");
		}
	}
	
	public widgetHelper setText(String text){
		if (type.equals("txt")){
			((TextView) w).setText(text);
		} else if (type.equals("edt")){
			((EditText) w).setText(text);
		}
		return this;
	}
	
	public String getText(){
		if (type.equals("edt")) {
			return getText_edt();
		} else if (type.equals("txt")){
			return txt().getText().toString();
		}
		return "";
	}
	
	public void setImage(Bitmap bm){
		img().setImageBitmap(bm);
	}
	
	public void setImageResource(int res){
		if (type.equals("img")){
			img().setImageResource(res);
		}
	}
	
	public void setImageUri(Uri uri){
		if (type.equals("img")){
			img().setImageURI(uri);
		}
	}
	
	public widgetHelper setHover(final String color){
		final int b = w.getPaddingBottom();
		final int t = w.getPaddingTop();
		final int l = w.getPaddingLeft();
		final int r = w.getPaddingRight();
		w.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL){
					v.setBackgroundDrawable((Drawable) v.getTag(R.id.drawable));
					v.setPadding(l, t, r, b);
				} else {
					v.setBackgroundColor(Color.parseColor(color));
				}
				return false;
			}
		});
		
		w.setTag(R.id.drawable, w.getBackground());
		return this;
	}
	
	public View obj(){
		return w;
	}
	
	public View objWrap(){
		return wrap_w;
	}
	
	public ScrollView scv(){
		return (ScrollView) w;
	}
	
	public LinearLayout lly(){
		return (LinearLayout) w;
	}
	
	public ViewGroup viewgroup(){
		return (ViewGroup) w;
	}
	
	public Button btn(){
		return (Button) w;
	}
	
	public String getTagString(int field){
		return (String) w.getTag(field);
	}
	
	public int getTagInt(int field){
		return (Integer) w.getTag(field);
	}
	
	public int checkbox(int[] icon){
		int newtag = getTagInt(R.id.tag);
		newtag = newtag == 0 ? 1 : 0;
		setTags(R.id.tag, newtag);
		img().setImageResource(icon[newtag]);
		return newtag;
	}
	
	public TextView txt(){
		return (TextView) w;
	}
	
	public EditText edt(){
		return (EditText) w;
	}
	
	public SeekBar seek(){
		return (SeekBar) w;
	}
	
	public widgetHelper setSize(int width, int height){
		w.getLayoutParams().height = height;
		w.getLayoutParams().width = width;
		return this;
	}
	
	public void setInt(Line line, String str, int id){
		line.put(str, getTagInt(id));
	}
	
	public void setDouble(Line line, String str){
		line.put(str, Double.parseDouble(getText()));
	}
	
	public void set(Line line, String str){
		line.put(str, getText());
	}
	
	public void setEdtHover(final edtHover h){
		final EditText edt = edt();
		setTags(R.id.tag, 0);
		/*
		edt.setOnKeyPreImeListener(new EditText.OnKeyPreImeListener() {
			
			@Override
			public boolean onKeyPreIme(View view, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				if (keyCode != 4) return false;
				h.change(false);
				setTags(R.id.tag, 0);
				return false;
			}
		});
		*/
		edt.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (getTagInt(R.id.tag) == 1) return false;
				setTags(R.id.tag, 1);
				h.change(true);
				return false;
			}
		});
		
	}
	
	public interface edtHover{
		public void change(boolean focus);
	}
	
	public void removeView(){
		viewgroup().removeAllViews();
	}
	
	public void addView(View v, Integer... args){
		if (args.length > 0){
			((ViewGroup) w).addView(v, args[0], args[1]);
		} else {
			((ViewGroup) w).addView(v);
		}
	}
	
	protected void i(Object obj){
		log.i(this, obj);
	}
	
	protected void e(String str){
		log.e(this, str);
	}
	
	public boolean checkZeroAndHint(String str){
		boolean empty = getText().equals("0");
		if (empty) log.toast(m, str);
		return empty;
	}
	
	public boolean checkEmptyAndHint(String str){
		boolean empty = isEmpty();
		if (empty) log.toast(m, str);
		return empty;
	}
	
	public boolean isEmpty(){
		return getText().equals("");
	}
	
	@Override
	public String toString(){
		return getText();
	}
	
	public widgetHelper click(){
		if (tmp_click != null) tmp_click.onClick(w);
		return this;
	}
}
