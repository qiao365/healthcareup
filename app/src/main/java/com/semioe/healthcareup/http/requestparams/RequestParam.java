package com.semioe.healthcareup.http.requestparams;

import org.apache.http.NameValuePair;

import java.util.List;

/**
 * @description 请求准备上传数据
 */
public interface RequestParam {
	List<NameValuePair> getParams();

	void SetUrl(String url);

	String getUrl();

	void SetStringJsonData(String jsonData);

	String getStringJsonData();
}
