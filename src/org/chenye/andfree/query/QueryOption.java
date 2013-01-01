package org.chenye.andfree.query;

import java.net.URI;

import org.chenye.andfree.obj.Line;

public class QueryOption {
	/** cookie */
	public Line cookie = new Line();
	public Line header = new Line();
	
	/** URI */ public URI uri;
	/** 协议 */ public String scheme;
	/** 域名 */ public String host;
	/** 端口 */ public int port;
	/** 路径 */ public String path;
	
	/** referer */ public String referer;
}
