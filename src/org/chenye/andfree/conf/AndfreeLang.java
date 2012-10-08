package org.chenye.andfree.conf;

public class AndfreeLang {
	public final static int CN = 0;
	public final static int EN = 1;
	public final static int HK = 2;
	public static int SELECT = CN;
	
	public final static String cancel = l("取消", "cancel");
	
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
