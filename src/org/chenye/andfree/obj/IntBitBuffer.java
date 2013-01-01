package org.chenye.andfree.obj;

import java.nio.ByteBuffer;

public class IntBitBuffer extends BaseLog{
	private long header = -1;
	private static int HEADER_LENGTH = 32;
	private int _point = 0;
	
	/**
	 * IntBitBuffer(Big Endian)
	 * summary: 
	 */
	public IntBitBuffer(){
		
	}
	public IntBitBuffer(long header){
		this.header = header;
	}
	
	/**
	 * move the old number left of *length bit and plus value
	 * @param value
	 * @param length
	 */
	public void put(int value, int length){
		long v = value;
		v &= pow(2, length) - 1;
		if (header == -1){
			header = v;
			return;
		}
		
		header = header << length;
		header += v;
	}
	
	public void put(boolean value, int length){
		put(value ? 1 : 0, length);
	}
	
	public int pow(int a, int b){
		return (int) Math.pow(a, b);
	}

	public long value(){
		return header;
	}
	
	public byte[] struct(){
		ByteBuffer b = ByteBuffer.allocate(8);
		b.putLong(header);
		b.position(4);
		byte[] array = new byte[4];
		b.get(array);
		return array;
	}
	
	public int get(int length){
		int move_length = HEADER_LENGTH - length - _point;
		int data = (int) (header >> move_length);
		data &= pow(2, length) - 1;
		_point += length;
		return data;
	}
}
