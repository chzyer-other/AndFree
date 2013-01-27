前言
---
andfree是我历经1年的android开发, 最后整理出来的, 其前身未成形时曾用于两个项目, 可能由于LZ在接触Android前较为熟悉python, php, js, 所以可能andfree中会有一些上述语言的影子, 希望Andfree能够给各位开发者带来一些便利~

使用
---
AndFree是一套代码, 只要把AndFree放到项目目录里面并且设置为源文件夹即可使用

协议
---
采用最自由的MIT开源协议

特性
---
   - 提供完整的数据库类封装
   - 提供常用的辅助函数封装
   - 基于Activity给其添加更多特性
   - Class-XML一对一模式提供控件封装
   - 统一全局, 唯一一个数据集类(Line), 支持JSON/[MsgPack](http://msgpack.org)的输入和输出, 数据库输出的格式也是Line (内置[msgpack-for-android](https://github.com/chzyer/msgpack-for-android))
   - Activity默认提供Activity Result路由, 不用将全部代码集中于Activity而是可以分散到子类中
   - 系统控件封装, 继承于类IWidget, 针对一些常用的控件比如button, imageview, textview, edittext, linearlayout, relativelayout进行一些封装
   - 统一ContentProvider和数据库接口
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
   		
   - 代码定义数据库结构, 好处是程序能根据代码自动更新数据库结构, 对, AndFree会自动更新数据库结构(前提是你要修改数据库的版本号AndFree才会检查)
   - 特性大概就这么多, 目的只有一个, 让安卓的开发更加快速, 下面还会有详细的使用说明

其他功能?
-----
点击[AndFree](http://andfree.chenye.org), 查看`AndFree`的详细文档吧.
[使用AndFree入门安卓](https://gist.github.com/4564639)
