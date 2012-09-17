package org.chenye.andfree.conf;

public class lang {
	public final static int CN = 0;
	public final static int EN = 1;
	public final static int HK = 2;
	public static int SELECT = CN;
	
	public final static String cancel = l("取消", "Cancel");
	public final static String edit = l("编辑", "Edit");
	public final static String andfree = l("灵敏短信", "andfree", "靈敏短訊");
	public final static String route_info = l("规则详情", "Route Info", "規則詳情");
	public final static String route_desc = l("规则描述", "Description", "規則描述");
	public final static String system = l("系统", "System", "系統");
	public final static String action = l("操作", "Action", "操作");
	
	public final static String intercept_setting = l("信息拦截设置", "SMS Intercept", "訊息攔截設置");
	public static class encrypt{
		public final static String hide = l("隐藏短信锁");
		public final static String hide_summary = l("打开菜单后按3下菜单键呼出");
		public final static String def_body = l("内容已加密");
	}
	public final static String encrypt_setting = l("信息锁设置", "SMS Lock", "信息鎖設置");
	
	public final static String cloud = l("灵敏云", "Cloud", "靈敏雲");
	
	public final static String location_info = l("本地资料", "Info in Location", "本地資料");
	
	public final static String intercepts = l("信息拦截", "Msg Intercepts", "訊息攔截");
	public final static class intercept {
		public final static String weight = l("权重", "Weight", "權重");
		public final static String contain = l("包含..", "contains ..");
		public final static String start_with = l("以..开头", "start with ..", "以..開頭");
		public final static String end_with = l("以..结尾", "end with ..", "以..結尾");
		public final static String equals_with = l("完全符合..", "equals with ..");
		public final static String body = l("短信內容", "Msg", "短訊內容");
		public final static String address = l("号码", "Phone", "號碼");
		
		public final static String enable_smartly = l("低能拦截", "Smarty Interpept", "開啟智能攔截");
		public final static String enable_smartly_hint = l("利用云端可拦截大部分垃圾短信", "Interpept msg with cloud", "智能根据云端拦截信息");
	}
	
	public final static class dustbin {
		public final static String list = l("查看清理箱", "Dustbin Lists", "查看清理箱");
		public final static String search = l("搜索", "Search", "搜尋");
		public final static String search_hint = l("输入关键词", "Enter Keyword", "輸入關鍵字");
		public final static String detail = l("查看信息", "Detail", "查看訊息");
	}
	
	public final static String popup = l("信息弹出", "Popping", "訊息彈出");
	public final static String popup_open = l("open信息弹出", "Popping", "訊息彈出");
	
	public final static String custom_route = l("自定义规则", "Custom Routes", "自定義規則");
	public final static String details = l("详情", "Details", "詳情");
	public final static String views = l("查看", "Views", "查看");
	public final static String create = l("添加", "Creates", "添加");
	public final static String news = l("新增", "New", "新增");
	public final static String delete = l("删除", "Delete", "刪除");
	public final static String motify = l("修改", "Motify");
	public final static String white_list = l("白名单", "Write Lists", "白名單");
	public final static String black_list = l("黑名单", "Black Lists", "黑名單");
	

	
	public static class baseLanguage{
		String[] str;
		public baseLanguage(String... str){
			this.str = str;
		}
		
		@Override
		public String toString(){
			if (SELECT < str.length){
				return str[SELECT];
			} else {
				return str[0];
			}
		}
		
		public String v(){
			return toString();
		}
	}
	
	static String l(String... str){
		return new baseLanguage(str).v();
	}
}
