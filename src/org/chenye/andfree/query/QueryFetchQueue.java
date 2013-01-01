package org.chenye.andfree.query;

import in.hitme.android._andfree.Conf;
import in.hitme.android._network.HitmeNioClient.ClientException;

import java.util.ArrayList;
import java.util.Map.Entry;

import org.apache.http.Header;
import org.chenye.andfree.func.FuncThread;
import org.chenye.andfree.obj.BaseLog;
import org.chenye.andfree.obj.Line;
import org.chenye.andfree.query.IHttpRequest.IcreateNewStep;

import android.view.View;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class QueryFetchQueue extends BaseLog{
	Query q;
	QueryOption conf;
	Line globalData = new Line();
	ArrayList<InextQueue> reqs = new ArrayList<InextQueue>();
	public QueryFetchQueue(Query query, QueryOption option, IHttpRequest... requests){
		q = query;
		conf = option;
		for (IHttpRequest req: requests){
			req.setOnCreateNewStep(new IcreateNewStep() {
				public NextStep create(IHttpRequest hr) {
					return new NextStep(getInextQueue(q, hr));
				}
			});
			req.setCookieSource(conf.cookie);
			req.setDataSource(globalData);
			InextQueue iq = getInextQueue(q, req);
			if (reqs.size() > 0){
				reqs.get(reqs.size() - 1).setNextFetch(iq);
			}
			reqs.add(iq);
		}
		start(0);
	}
	
	public static InextQueue getInextQueue(final Query q, IHttpRequest req){
		final QueryOption conf = q.conf;
		return new InextQueue(req) {
			public void run() {
				
				String host = req.host();
				if (host != null){
					conf.host = host;
				}
				
				String path = req.path();
				if (path != null){
					conf.path = path;
				}
				
				String referer = req.referer();
				if (referer != null){
					conf.referer = referer;
				}
				
				String url = q.getPach();
				AsyncHttpClient c = new AsyncHttpClient();
				c.addHeader("Referer", conf.referer);
				c.addHeader("Cookie", q.getCookieString());
				conf.referer = url;

				final AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler(){
					boolean timeout = false;
					boolean success = false;
					@Override
					public void onSuccess(int statusCode, Header[] header,
							String content) {
						if (timeout) return;
						success = true;
						q.parseHeader(header);
						QueueDirector qd = null;
						if (content.length() <= 0 && conf.header.contains("Location")){
							content = conf.header.str("Location");
							qd = req.onRedirect(content);
						}
						if (qd == null){
							qd = req.onResponse(content);
						}
						self.onFinish(qd, req.nextStep());
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						if (content != null && content.contains("timeout")){
							if (success){
								return;
							}
							timeout = true;
							req.onException(ClientException.Timeout());
							return;
						}
						super.onFailure(error, content);
						req.onException(ClientException.From(new Exception(error)));
					}
					
				};
				
				if (req.isPost()){
					Line data = req.data();
					RequestParams rp = new RequestParams();
					for (Entry<Object, Object> d: data.valueSet()){
						String key = "";
						String value = "";
						if (d.getKey() != null){
							key = d.getKey().toString();
						}
						if (d.getValue() != null){
							value = d.getValue().toString();
						}
						
						rp.put(key, value);
					}
					c.post(url, rp, handler);
				} else {
					c.get(url, handler);
				}
				FuncThread.delay(Conf.CLIENT_TIMEOUT * 1000, new View.OnClickListener() {
					
					public void onClick(View arg0) {
						handler.onFailure(null, "timeout");
					}
				});
			}
			
			public void onFinish(QueueDirector qd, NextStep ns){
				if (qd == null || qd.isNext()){
					ns.run();
					return;
				}
				
				if (qd.isJump()){
					qd.getJump().run();
					return;
				}
				
				if (qd.isStop()){
					return;
				}
				
				if (qd.isEnd()){
					return;
				}
			}
		};
	}
	
	
	public static class NextStep{
		private InextQueue iq;
		public NextStep(InextQueue iq){
			this.iq = iq;
		}
		public void run(){
			if (iq == null) return;
			iq.run();
		}
	}
	public static abstract class InextQueue{
		IHttpRequest req;
		final InextQueue self = this;
		public InextQueue(IHttpRequest req){
			this.req = req;
		}
		public abstract void run();
		public void onFinish(QueueDirector qd, NextStep ns){}
		public IHttpRequest getReq(){
			return req;
		}
		public void setNextFetch(InextQueue iq){
			req.setNextFetch(iq);
		}
	}
	
	public void start(int index){
		InextQueue iq = reqs.get(index);
		if (reqs.size() > index + 1){
			iq.setNextFetch(reqs.get(index + 1));
		}
		iq.run();
	}
	
	
}
