package org.chenye.andfree.widget;

import java.io.File;

import org.chenye.andfree.db.AFCore;
import org.chenye.andfree.func.FuncFile;
import org.chenye.andfree.func.FuncStr;
import org.chenye.andfree.func.FuncTime;
import org.chenye.andfree.obj.Line;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

public class AFImage extends IWidget<AFImage, ImageView>{
	public AFImage(Context m){
		super(new ImageView(m));
	}
	
	public AFImage(ImageView e) {
		super(e);
	}
	
	public AFImage(int id){
		super(id, ImageView.class);
	}

	@Override
	public AFImage init(Object... objs) {
		return this;
	}

	public AFImage setMinHeight(int h){
		_e.setMinimumHeight(h);
		return this;
	}
	
	public AFImage setImageResource(int resid){
		_e.setImageResource(resid);
		return this;
	}
	
	public AFImage setImageBitmap(Bitmap bm){
		_e.setImageBitmap(bm);
		return this;
	}
	public AFImage setImageBinary(byte[] binaryData){
		if (binaryData == null){
			return this;
		}
		Bitmap bm = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
		return setImageBitmap(bm);
	}
	
	public AFImage setImageFlexWidthScale(byte[] binaryData){
		Bitmap bm = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
		int widget = bm.getWidth() / bm.getHeight() * getHeight();
		setImageBitmap(bm).obj().setScaleType(ScaleType.FIT_XY);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) getLayoutParams();
		lp.width = widget;
		setLayoutParams(lp);
		return this;
	}
	
	int tryTime = 0;
	public AFImage setImageUrl(final String url){
		Line data = new AFCore.ImageCache().where(
			AFCore.ImageCache.path.equal(url)
		).get();
		if (data.valid()){
			Bitmap bm = FuncFile.readBitmap(
					new File(data.str(AFCore.ImageCache.file))
			);
			if (bm != null){
				setImageBitmap(bm);
				return this;
			}
		}
		
		new AsyncHttpClient().get(url, new BinaryHttpResponseHandler(){
			@Override
			public void onSuccess(byte[] binaryData) {
				File f = FuncFile.saveToSDCard(FuncStr.getRandomString(32), binaryData);
				Line data = new Line(AFCore.ImageCache.class);
				data.put(AFCore.ImageCache.path, url);
				data.put(AFCore.ImageCache.file, f.toString());
				data.put(AFCore.ImageCache.createTime, FuncTime.time());
				data.save(AFCore.ImageCache.path);
				tryTime = 0;
				setImageBinary(binaryData);
			}
			
			@Override
			public void onFailure(Throwable error) {
				if (tryTime++ > 3) return;
				setImageUrl(url);
			}
		});
		return this;
	}
	
	public AFImage setBorder(int drawable, int padding){
		setPadding(padding);
		setBackgroundResource(drawable);
		return this;
	}
}
