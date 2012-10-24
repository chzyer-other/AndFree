# AndFree  
# 中文说明
###前言
andfree是我历经1年的android开发, 最后整理出来的, 其前身未成形时曾用于两个项目, 可能由于LZ在接触Android前较为熟悉python, php, js, 所以可能andfree中会有一些上述语言的影子, 希望Andfree能够给各位开发者带来一些便利~

###使用
AndFree本身是一个Android项目的library, 可以download后在eclipse中导入, 最后在要用的项目中的`perproties->android`引用andfree, 就可以使用了.

###协议
采用最自由的MIT开源协议

###特性
   - 提供完整的数据库类封装
   - 提供常用的辅助函数封装
   - 基于Activity给其添加更多特性
   - Class-XML一对一模式提供控件封装
   - 统一全局, 唯一一个数据集类(Line), 支持JSON输入和解析
   - Activity默认提供Activity Result路由, 不用将全部代码集中于Activity而是可以分散到子类中
   - 系统控件封装, 全部使用统一的接口(WidgetHelper), 还有代码与XML中id的关系绑定
   - 支持单Activity带多个子页面并有层级关系(子级数量不限制, 并内置一个过渡效果), 类似IOS那种模式
   - 统一ContentProvider和数据库接口
   - 内置我写的一些常用控件(类似Preference界面那样), 如果想写设置界面的话基本可以秒杀.(绝对比Preference好写)
   - 内置动态配置, 主要是用于保存设置类的数据(实质是使用数据库内建一张config的表, 并做一些封装), 在我写的默认控件中也对动态配置信息进行支持, 比如给一个开关指定一个配置字段, 开关会根据用户的操作自动操作数据库 :), 自己使用的话, 语法类似于下面
   
   		Systems.showGuide.set(true);
   		if (Systems.showGuide.bool()){
   			// TODO show guide layout
   		}
   - 还有静态的配置类, 并可设置依赖关系, 比如说我有两个模块 A, B, 两个模块都有一个Log功能, 然后我可以通过静态配置来决定这两个log功能执不执行. Conf.LOG_A.和Conf.LOG_B
   	
   		//_f可以理解为一个生成DebugField的函数.(代码里面真的是_f, 哈哈贪方便)
   		public static DebugField LOG_A = _f(true);
   		public static DebugField LOG_B = _f(true);
   	然后我还想要一个全局的LOG开关, 一关闭这个的话, 全局的LOG都不显示, 这个时候就需要依赖关系
   	
   		public static DebugField LOG = _f(true);
   		//LOG_A和LOG_B要添加依赖关系
   		public static DebugField LOG_A = _f(true, LOG);
   		public static DebugField LOG_B = _f(true, LOG);
   	这样, 一旦依赖的配置是False, LOG_A和LOG_B将始终被解析为false, 当LOG为true是, 其他两个依赖他的debug才起作用
   		
   - 代码定义数据库结构, 好处是程序能根据代码自动更新数据库结构, 对, AndFree会自动更新数据库结构(前提是你要修改数据库的版本号AndFree才会检查~)
   - 特性大概就这么多, 目的只有一个, 让安卓的开发更加快速, 下面还会有详细的使用说明(code)

其他功能?
-----
有有有~ 我还有一个用于网络爬虫(特别是登录型的)的插件, 基于AndFree, 叫[AndFree_Query](https://github.com/chzyer/AndFree_Query), 可以看下

#English Documents
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


As an alternative, you can get the same results by writing follows:
	
	Line data = new dbcore.popup(){{
		select(data);
		where(type.v(1));
	}}.result();

``It means "SELECT data FROM popup WHERE type = 1"``
## Data collector - Line
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