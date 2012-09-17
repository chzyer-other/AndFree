## AndFree  
The Android Framework provides some helper functions, some simple widget items, database orm and a layout framework.

## Layout
It's possable to build a android project without any layout designed. It also provides some widget like combo, switcher, etc. that you can set up then by code easily.

## Database
Andfree provides a database orm. You can define your database structure with code like follows:


	public final static class popup extends BaseDBcore{
		public final static dbField _id = dbField.primaryInt("_id");
		public final static dbField type = dbField.integer("type", 0); //0:contact; 1:keyword
		public final static dbField data = dbField.text("data");//contact_id || keyword
		public final static dbField music = dbField.text("music");//null: default; URI
	}

## How to Use

In eclipse, import the whole Andfree project and set it as a library.
Create a new project, and go into its ``properties -> android``, add library "AndFree".