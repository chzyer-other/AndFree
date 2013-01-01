package org.chenye.andfree.msgpack;

import java.nio.ByteBuffer;

public class DynamicByte {
	ByteBuffer bg;
	
	public void ensureBuffer(){ensureBuffer(-1);}
	public void ensureBuffer(int len){
		int capacity = 1024;
		if (bg == null){
			bg = ByteBuffer.allocate(capacity);
		}
		
		if (len < 0){
			len = capacity;
		} else {
			len -= bg.capacity() - bg.position();
		}
		
		if (len > 0){
			ByteBuffer tmp = ByteBuffer.allocate(bg.capacity() + len);
			tmp.put(bg.array());
			tmp.position(bg.position());
			bg = tmp;
		}
	}
	
	public void postion(int pos){
		bg.position(pos);
	}
	
	public int putAuto(Object a){return put(a, -1);}
	public int put(Object a){return put(a, 0);}
	public int put(Object a, int pos){
		return put(format(a, pos));
	}
	
	private int put(ByteBuffer byt){
		int readlen = byt.capacity() - byt.position();
		ensureBuffer(readlen);
		bg.put(byt);
		return readlen;
	}
	
	public ByteBuffer formatAuto(Object a){return format(a, -1);}
	public ByteBuffer format(Object a){return format(a, 0);}
	public ByteBuffer format(Object a, int pos){
		ByteBuffer b = null;
		if (a instanceof Integer){
			b = ByteBuffer.allocate(4);
			b.putInt((Integer) a);
			
			if (pos < 0){
				byte eq = (byte) (((Integer)a) < 0 ? 0xff : 0); 
				b.position(0);
				while (b.get() == eq && b.position() < b.capacity()){}
				pos = b.position() - 1;
			}
		} else if (a instanceof Byte){
			b = ByteBuffer.allocate(1);
			b.put((Byte) a);
		} else if (a instanceof ByteBuffer){
			b = (ByteBuffer) a;
		} else if (a instanceof byte[]){
			b = ByteBuffer.wrap((byte[]) a);
		} else if (a instanceof Boolean){
			b = ByteBuffer.wrap(new byte[] {(byte) ((Boolean) a ? 1 : 0)});
		} else if (a instanceof String){
			b = ByteBuffer.wrap(((String) a).getBytes());
		}
		if (b == null){
			return ByteBuffer.allocate(0);
		}
		
		b.position(pos);
		return b;
	}
	
	public ByteBuffer buffer(){
		ByteBuffer b = ByteBuffer.allocate(bg.position());
		bg.position(0);
		bg.get(b.array());
		b.position(0);
		return b;
	}
	
	public void log(Object len){
		System.out.println(len);
	}
	
}
