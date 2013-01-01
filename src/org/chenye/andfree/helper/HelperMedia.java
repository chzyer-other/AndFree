package org.chenye.andfree.helper;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class HelperMedia {
	public static String get_music_name(Context m, Object uri){
		if (uri == null || uri.equals("") || uri.equals("0")){
			return "静音";
		}
		if (uri.equals("default")) return "默认铃声";
		Ringtone ring = get_ringtone(m, uri);
		if (ring == null) return "静音";
		return ring.getTitle(m);
	}
	
	public static Ringtone get_ringtone(Context m, Object uri){
		
		return RingtoneManager.getRingtone(m, Uri.parse(uri + ""));
	}
}
