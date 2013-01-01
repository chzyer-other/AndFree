package org.chenye.andfree.query;

public abstract class IHttpGet extends IHttpRequest{
	public IHttpGet() {
		super(false);
	}
	public IHttpGet(String path) {
		super(false);
		_path = path;
	}

}
