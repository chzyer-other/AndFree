package org.chenye.andfree.query;

import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.query.QueryFetchQueue.NextStep;

public class QueueDirector extends BaseLog{
	public QueueDirector(){
		
	}
	
	private boolean _stop = false;
	public QueueDirector stop(){
		_stop = true;
		return this;
	}
	public boolean isStop(){
		return _stop;
	}
	
	private NextStep _ns;
	public QueueDirector jump(NextStep ns){
		_ns = ns;
		return this;
	}
	public boolean isJump(){
		return _ns != null;
	}
	public NextStep getJump(){
		return _ns;
	}
	
	boolean _end = false;
	public QueueDirector end(){
		_end = true;
		return this;
	}
	public boolean isEnd(){
		return _end;
	}
	boolean _next = false;
	public QueueDirector next(){
		_next = true;
		return this;
	}
	public boolean isNext(){
		return _next;
	}
	
	public static QueueDirector Stop(){
		return new QueueDirector().stop();
	}
	public static QueueDirector Jump(NextStep ns){
		return new QueueDirector().jump(ns);
	}
	public static QueueDirector End(){
		return new QueueDirector().end();
	}
	public static QueueDirector Next(){
		return new QueueDirector().next();
	}
}
