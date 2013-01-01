package org.chenye.andfree.msgpack;

import java.nio.ByteBuffer;

public class MsgPackUnpack{
	ByteBuffer source;
	Object obj;
	ByteBuffer b = ByteBuffer.allocate(4);
	
	public MsgPackUnpack(byte[] b){
		source = ByteBuffer.wrap(b);
		obj = parseAll();
	}
	
	public Object parseInt(int type){
		if (type <= 0x7f || type >= 0xe0){
			if (type >= 0xe0) return type - 256;
			return type;
		}
		if ((type >= 0xd0 && type <= 0xd3) || (type >= 0xcc && type <= 0xcf)){
			if (type == 0xcf || type == 0xd3){
				return source.getLong();
			}
			if (type == 0xce || type == 0xd2){
				return source.getInt();
			}
			
			b.position(0);
			int skip_len = 2; //two default
			byte writebyte = 0x00;
			if (type == 0xcc){
				//one
				skip_len = 3;
			}
			if (type == 0xd0 || type == 0xd1){
				writebyte = (byte) 0xff;
			}
			for (int i=0; i<skip_len; i++){
				b.put(writebyte);
			}
			
			b.put(source.get());
			if (skip_len == 2){
				b.put(source.get());
			}
			b.position(0);
			return b.getInt();
		}
		return null;
	}
	
	public Object parseBool(int type){
		if (type == 0xc3 || type == 0xc2){
			return type == 0xc3 ? true : false;
		}
		return null;
	}
	
	public Object parseString(int type){
		int len = -1;
		if (type >= 0xa0 && type <= 0xbf){
			len = type - 0xa0;
		} else if (type == 0xda){
			len = getTwoByteInt();
		} else if (type == 0xdb){
			len = source.getInt();
		}
		
		if (len < 0) return null;
		
		byte[] data = new byte[len];
		source.get(data);
		return new String(data);
	}
	
	public Object parseList(int type){
		int len = -1;
		if (type >= 0x90 && type <= 0x9f){
			len = type - 0x90;
		} else if (type == 0xdc){
			len = getTwoByteInt();
		} else if (type == 0xdd){
			len = source.getInt();
		}
		
		if (len < 0) return null;
		
		IListPicker ilist = getlist.ret();
		for (int i=0; i<len; i++){
			ilist.put(parseAll());
		}
		return ilist;
	}
	
	public Object parseDict(int type){
		int len = -1;
		if (type >= 0x80 && type <= 0x8f){
			len = type - 0x80;
		} else if (type == 0xde){
			len = getTwoByteInt();
		} else if (type == 0xdf){
			len = source.getInt();
		}
		if (len < 0) return null;
		
		IDictPicker idict = getdict.ret();
		for (int i=0; i<len; i++){
			idict.put(parseAll(), parseAll());
		}
		return idict;
	}
	
	public int getTwoByteInt(){
		b.position(0);
		b.put(new byte[] {0, 0});
		b.put(source.get());
		b.put(source.get());
		b.position(0);
		return b.getInt();
	}
	
	public Object parseAll(){
		byte tmp_type = source.get();
		int type = tmp_type < 0 ? tmp_type + 256 : tmp_type;
		Object o;
		o = parseInt(type);
		if (o != null) return o;
		
		o = parseBool(type);
		if (o != null) return o;
		
		o = parseString(type);
		if (o != null) return o;
		
		o = parseList(type);
		if (o != null) return o;
		
		o = parseDict(type);
		if (o != null) return o;
		return null;
	}
	
	public Object toObject(){
		return obj;
	}
	
	public static Object parse(byte[] b){
		return new MsgPackUnpack(b).toObject();
	}
	
	
	public interface IGetListPicker{
		public IListPicker ret();
	}
	public interface IGetDictPicker{
		public IDictPicker ret();
	}
	private static IGetDictPicker getdict;
	private static IGetListPicker getlist;
	public static void setGetList(IGetListPicker g){
		getlist = g;
	}
	public static void setGetDict(IGetDictPicker g){
		getdict = g;
	}
}

