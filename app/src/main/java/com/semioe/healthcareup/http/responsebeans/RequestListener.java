package com.semioe.healthcareup.http.responsebeans;

public abstract class RequestListener<T extends ResponseBean> {

	// TODO 请求数据完成处理业务
	public abstract void onComplete(T bean);

	// TODO 请求数据失败
	public abstract void onFailed();
}
