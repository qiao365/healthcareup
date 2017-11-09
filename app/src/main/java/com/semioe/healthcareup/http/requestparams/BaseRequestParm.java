package com.semioe.healthcareup.http.requestparams;

import org.apache.http.NameValuePair;

import java.util.HashMap;
import java.util.List;

public class BaseRequestParm implements RequestParam {
    private String url;
    private String jsonData;
    private String ContentType;// 文本类型
    private String Authorization;// 认证码 authtoken
    private String request;// 请求方式
    private List<String> imageUrlList;// 图片
    private HashMap<String, String> textMap;// 文本 map类型
    private boolean isEncryption = false;// 是否加密
    private byte[] jsonData2;
    private String filePath;// 附件
    private String clientVersion;
    private int clientVersionCode;
    private String networkType;
    private String osVersion;
    private String platform;

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public int getClientVersionCode() {
        return clientVersionCode;
    }

    public void setClientVersionCode(int clientVersionCode) {
        this.clientVersionCode = clientVersionCode;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getContentType() {
        return ContentType;
    }

    public void setContentType(String contentType) {
        ContentType = contentType;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BaseRequestParm(String url, HashMap<String, String> textMap, String Authorization, List<String> imageUrlList,
                           String filePath) {
        this.url = url;
        this.textMap = textMap;
        this.ContentType = ContentType;
        this.request = request;
        this.Authorization = Authorization;
        this.imageUrlList = imageUrlList;
        this.filePath = filePath;
        this.request = null;
    }

    public BaseRequestParm(String url, String jsonData, String ContentType, String request, String Authorization) {
        this.url = url;
        this.jsonData = jsonData;
        this.ContentType = ContentType;
        this.request = request;
        this.Authorization = Authorization;
    }

    public BaseRequestParm(String url, byte[] jsonData, String ContentType, String request, String Authorization) {
        this.url = url;
        this.jsonData2 = jsonData;
        this.jsonData = new String(jsonData);
        this.ContentType = ContentType;
        this.request = request;
        this.Authorization = Authorization;
    }

    public List<NameValuePair> getParams() {

        return null;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getUrl() {

        return url;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    public String getStringJsonData() {

        return jsonData;
    }

    public void SetUrl(String url) {
        this.url = url;
    }

    public void SetStringJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public HashMap<String, String> getTextMap() {
        return textMap;
    }

    public void setTextMap(HashMap<String, String> textMap) {
        this.textMap = textMap;
    }

    public boolean isEncryption() {
        return isEncryption;
    }

    public void setEncryption(boolean isEncryption) {
        this.isEncryption = isEncryption;
    }

    public byte[] getJsonData2() {
        return jsonData2;
    }

    public void setJsonData2(byte[] jsonData2) {
        this.jsonData2 = jsonData2;
    }

}
