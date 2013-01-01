package org.chenye.andfree.msgpack;

import java.nio.ByteBuffer;

public class MsgPack {
	public ByteBuffer pack(Object obj){
		MsgPackByte b = new MsgPackByte();
		b.put(obj);
		return b.buffer();
	}
	
	public static Object unpack(byte[] byt){
		return MsgPackUnpack.parse(byt);
	}
}
