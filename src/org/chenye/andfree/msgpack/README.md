MessagePack for Android
----

[MessagePack Homepage](http://msgpack.org)   
test successed with python.   

Feature
---

1. supports int, long, String, Boolean, array, hashtable
2. able to custom array and hashtable(implements IDictPicker or IListPicker)


How to use
---
   1. Modify the package name(default: org.chenye.andfree.msgpack)
   2. Implements data structures(list, dict)
   3. Configure MsgPackUnpack
```java
      	//structure
      	class Line implements IDictPicker, IListPicker{
      	
      	}
      	
      	//configure
      	MsgPackUnpack.setGetDict(new IGetDictPicker() {
			
			@Override
			public IDictPicker ret() {
				// TODO Auto-generated method stub
				return new Line();
			}
		});
		MsgPackUnpack.setGetList(new IGetListPicker() {
			
			@Override
			public IListPicker ret() {
				// TODO Auto-generated method stub
				return new Line();
			}
		});
```
   4. pack or unpack
```java
	      ByteBuffer bb = MsgPack.pack(Object obj);
    	  Object obj = MsgPack.unpack(byte[] bytes);
```