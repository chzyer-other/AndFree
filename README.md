## AndFree  
The Android Framework provides some helper functions, some simple widget items, database orm and a layout framework.

## How to Use

1. In eclipse, import the whole Andfree project and set it as a library.
2. Create a new project, and go into its ``properties -> android``, add library "AndFree".

## Layout
It's possable to build a android project without any layout designed. It also provides some widget like combo, switch button, etc. that you can set up then by code easily.

## Database
Andfree provides a database orm. You can define your database structure with code like follows:

	public class dbcore extends baseCore{
		public static class popup extends BaseDBcore{
			public final static dbField _id = dbField.primaryInt("_id");
			public final static dbField type = dbField.integer("type", 0); //0:contact; 1:keyword
			public final static dbField data = dbField.text("data");//contact_id || keyword
			public final static dbField music = dbField.text("music");//null: default; URI
		}
	}

For get the result :

	Line data = new dbcore.popup()
		.select(dbcore.pupop.data)
		.where(dbcore.popup.type.v(1))
		.result();
	// Line is a multiple functions JSON data collector in Andfree


Or you can get the same results by writing follows:
	
	Line data = new dbcore.popup(){{
		select(data);
		where(type.v(1));
	}}.result();

``It means "SELECT data FROM popup WHERE type = 1"``
## Data collector - 'Line'
In android, there is many array type to choise. In andfree, there is only one, "Line".

1. Line support json and merge "JSONObject" and "JSONArray" and then no need to write try{}catch(){} 

		//object
		Line data = new Line();
		data.put("key1", "value1");
		data.put("key2", "value2");
		
		//array
		Line data = new Line();
		data.put(new Line().put("key1", "value1"));
		data.put(new Line().put("key2", "value2"));
		
2. Line also can received the database data. It's easy way to foreach then.

		//only support for array(list), object(dict) is not supported
		Line datas = new dbcore.popup().result();
		for (Line item: datas){
			Log.i('test', item.toString());
		}
3. Line support insert/update data to database using `save()`
	
		//insert
		Line data = new Line(dbcore.popup.class){{
			new dbcore.popup(){{
				put(type, 1);
				put(data, "hello");
				put(music, "media://xxx");
			}}
			save();
		}};
		
		//update
		Line data = new dbcore.popup().where(dbcore.popup._id.v(1)).get()
		data.put(dbcore.popup.type, 2);
		data.save();

> Line is a json Library written by myself, so it may a little low performance than the "JSONObject" and "JSONArray". But it's dosen't matter if the count of data is small than 500.At usually, the count may in the range of 10-20.