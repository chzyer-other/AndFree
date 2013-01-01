package org.chenye.andfree.func;

import java.io.IOException;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;

import org.chenye.andfree.conf.AndfreeConf;

import android.util.Base64;

public class FuncSecurity {
	public static byte[] desEncrypt(byte[] plainText, String keys) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(keys.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.ENCRYPT_MODE, key, sr);
		byte data[] = plainText;
		byte encryptedData[] = cipher.doFinal(data);
		return encryptedData;
	}

	public static byte[] desDecrypt(byte[] encryptText, String keys) throws Exception {
		SecureRandom sr = new SecureRandom();
		DESKeySpec dks = new DESKeySpec(keys.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES");
		cipher.init(Cipher.DECRYPT_MODE, key, sr);
		byte encryptedData[] = encryptText;
		byte decryptedData[] = cipher.doFinal(encryptedData);
		return decryptedData;
	}

	public static String encrypt(String input) {
		if (input.length() > 28) return input;
		int times = AndfreeConf.SECURITYFUNC_KEY.getBytes().length / 8;		
		byte[] b = new byte[AndfreeConf.SECURITYFUNC_KEY.length()];
		try{
		for(int i=0; i<times; i++){
			int start = input.length() / times * i;
			int end = input.length() / times * (i + 1);
			if (i == times - 1) end = input.length();
			String tmp_input = input.substring(start, end);
			String tmp_key = AndfreeConf.SECURITYFUNC_KEY.substring(8*i, 8*(i+1));
			byte[] tmp = desEncrypt(tmp_input.getBytes(), tmp_key);
			for (int j=0; j<tmp.length; j++){
				b[i*tmp.length + j] = tmp[j];
			}
		}
		}catch(Exception e){
			e(e);
		}
		return base64Encode(b);
	}

	public static String decrypt(String input) throws Exception {
		byte[] result = base64Decode(input);
		return new String(desDecrypt(result, AndfreeConf.SECURITYFUNC_KEY));
	}

	public static String base64Encode(byte[] s) {
		return new String(Base64.encode(s, Base64.DEFAULT));
	}

	public static byte[] base64Decode(String s) throws IOException {
		return Base64.decode(s, Base64.DEFAULT);
	}
	
	private static void e(Exception ex){
		log.e(FuncSecurity.class, ex);
	}
}