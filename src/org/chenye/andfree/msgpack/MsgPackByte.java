package org.chenye.andfree.msgpack;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map.Entry;

public class MsgPackByte extends DynamicByte{

	private static double N63 = Double.parseDouble("-9223372036854775808");
	private static long N31_1 = Long.parseLong("-2147483649");
	private static long N31 = N31_1 + 1;
	private static int N15_1 = -32769;
	private static int N15 = N15_1 + 1;
	private static int N7_1 = -129;
	private static int N7 = -128;
	private static int N5_1 = -33;
	private static int N5 = N5_1 + 1;
	private static int P4_1 = 15;
	private static int P7_1 = -N7 - 1;
	private static int P7 = P7_1 + 1;
	private static int P8_1 = 255;
	private static int P8 = 256;
	private static int P16_1 = 65535;
	private static int P16 = P16_1 + 1;
	private static long P32_1 = Long.parseLong("4294967295");
	private static long P32 = P32_1 + 1;
	private static double P64_1 = Double.parseDouble("18446744073709551615");
	
	public int putByte(Object a) {
		byte o = 0;
		if (a instanceof Integer){
			o = ((Integer) a).byteValue();
		} else if (a instanceof Long){
			o = ((Long) a).byteValue();
		} else if (a instanceof Byte){
			o = (Byte) a;
		} else {
			return 0;
		}
		
		return super.put(o);
	}
	
	public int putLong(Long a){
		if (a >= -32 && a <= 127){
			return putByte(a);
		}
		
		long int_header = 0;
		int len = 0;
		if (N63 <= a && a <= N31_1){
			int_header = 0xd3;
			len = 8;
		} else if (N31 <= a && a <= N15_1){
			int_header = 0xd2;
			len = 4;
		} else if (N15 <= a && a <= N7_1){
			int_header = 0xd1;
			len = 2;
		} else if (N7 <= a && a <= N5_1){
			int_header = 0xd0;
			len = 1;
		} else if (N5 <= a && a <= P7_1){
			int_header = a;
			len = 0;
		} else if (P7 <= a && a <= P8_1){
			int_header = 0xcc;
			len = 1;
		} else if (P8 <= a && a <= P16_1){
			int_header = 0xcd;
			len = 2;
		} else if (P16 <= a && a <= P32_1){
			int_header = 0xce;
			len = 4;
		} else if (P32 <= a && a <= P64_1){
			int_header = 0xcf;
			len = 8;
		} else {
			return 0;
		}
		
		putByte(int_header);
		
		if (len == 0){
			return 1;
		}
		
		ByteBuffer bb = ByteBuffer.allocate(len == 8 ? 8 : 4);
		if (len == 8){
			bb.putLong(a);
			return super.put(bb) + 1;
		}
		
		if (len == 4){
			return super.put(a, 0);
		}
		
		if (len == 2){
			return super.put(a, 2);
		}
		
		return super.put(a, 3);
	}
	
	public int putInt(Object tmp_a){
		Long a = null;
		if (tmp_a instanceof Integer){
			a = ((Integer) tmp_a).longValue();
		} else if (tmp_a instanceof Long){
			a = (Long) tmp_a;
		}
		if (a >= -32 && a <= 127){
			return putByte(tmp_a);
		}
		int int_header = 0;
		int len = 0;
		if (N63 <= a && a <= N31_1){
			int_header = 0xd3;
			len = 8;
		} else if (N31 <= a && a <= N15_1){
			int_header = 0xd2;
			len = 4;
		} else if (N15 <= a && a <= N7_1){
			int_header = 0xd1;
			len = 2;
		} else if (N7 <= a && a <= N5_1){
			int_header = 0xd0;
			len = 1;
		} else if (N5 <= a && a <= P7_1){
			int_header = (Integer) tmp_a;
			len = 0;
		} else if (P7 <= a && a <= P8_1){
			int_header = 0xcc;
			len = 1;
		} else if (P8 <= a && a <= P16_1){
			int_header = 0xcd;
			len = 2;
		} else if (P16 <= a && a <= P32_1){
			int_header = 0xce;
			len = 4;
		} else if (P32 <= a && a <= P64_1){
			int_header = 0xcf;
			len = 8;
		} else {
			return 0;
		}
		
		putByte(int_header);
		
		if (len == 0){
			return 1;
		}
		
		ByteBuffer bb = ByteBuffer.allocate(len == 8 ? 8 : 4);
		if (len == 8){
			bb.putLong(a);
			return super.put(bb) + 1;
		}
		
		int aa = (Integer) tmp_a;
		if (len == 4){
			return super.put(aa, 0);
		}
		
		if (len == 2){
			return super.put(aa, 2);
		}
		
		return super.put(aa, 3);
	}
	
	public int putBool(boolean bool){
		return putByte(bool ? 0xc3 : 0xc2);
	}
	
	public int putString(String str){
		int a = str.length();
		int header = 0;
		int pos;
		if (a <= P4_1){
			header = 0xa0 + a;
			pos = 4;
		} else if (a <= P16_1){
			header = 0xda;
			pos = 2;
		} else if (a <= P32_1){
			header = 0xdb;
			pos = 0;
		} else {
			return 0;
		}
		
		putByte(header);
		if (pos == 4){
			return super.put(str) + 1;
		}
		
		super.put(a, pos);
		return super.put(str) + 1 + (4-pos);
	}
	
	public int putList(IListPicker data){
		int writed = 1;
		int len = data.length();
		
		int header = 0x90;
		int pos = 0;
		if (len <= P4_1){
			header += len;
			pos = 4;
		} else if (len <= P16_1){
			header = 0xdc;
			pos = 2;
		} else if (len <= P32_1){
			header = 0xdd;
			pos = 0;
		} else {
			return 0;
		}
		
		putByte(header);
		
		if (pos < 4){
			writed += put(len, pos);
		}

		if (data.length() <= 0){
			return writed;
		}
		Iterator<Object> i = data.values();
		while (i.hasNext()){
			Object o = i.next();
			writed += put(o);
		}
		return writed;
	}
	
	public int putDict(IDictPicker data){
		int writed = 1;
		int len = data.length();
		int header = 0x80;
		int pos = 0;
		if (len <= P4_1){
			header += len;
			pos = 4;
		} else if (len <= P16_1){
			header = 0xde;
			pos = 2;
		} else if (len <= P32_1){
			header = 0xdf;
			pos = 0;
		} else {
			return 0;
		}
		
		putByte(header);
		if (pos < 4){
			writed += put(len, pos);
		}
		
		if (data.length() <= 0){
			return writed;
		}
		for (Entry<Object, Object> a: data.valueSet()){
			writed += put(a.getKey());
			writed += put(a.getValue());
		}
		return writed;
	}
	
	@Override
	public int put(Object o) {
		if (o instanceof Long){
			return putLong((Long) o);
		}
		
		if (o instanceof Integer){
			return putInt(o);
		}
		
		if (o instanceof Boolean){
			return putBool((Boolean) o);
		}
		
		if (o instanceof String){
			return putString(((String) o));
		}
		if (o instanceof IListPicker && ((IListPicker) o).isListValid()){
			return putList((IListPicker) o);
		}
		if (o instanceof IDictPicker && ((IDictPicker) o).isDictValid()){
			return putDict((IDictPicker) o);
		}
		return 0;
	}
}
