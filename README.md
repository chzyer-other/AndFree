## AndFree  
The Android Framework provides some helper functions, some simple widget items, database orm and a layout framework.

## How to Use

In eclipse, import the whole Andfree project and set it as a library.
Create a new project, and go into its ``properties -> android``, add library "AndFree".

## Layout
It's possable to build a android project without any layout designed. It also provides some widget like combo, switcher, etc. that you can set up then by code easily.

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
	// Line is a multiple functions JSON data collection in Andfree


Or you can get the same results by writing follows:
	
	Line data = new dbcore.popup(){{
		select(data);
		where(type.v(1));
	}}.result();

``It means "SELECT data FROM popup WHERE type = 1"``
