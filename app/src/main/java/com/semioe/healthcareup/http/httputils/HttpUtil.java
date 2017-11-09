package com.semioe.healthcareup.http.httputils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.http.requestparams.BaseRequestParm;
import com.semioe.healthcareup.utils.InternetUtil;
import com.semioe.healthcareup.utils.SystemLog;
import com.semioe.healthcareup.utils.Utils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Administrator
 * @ClassName: HttpUtil
 * @date 2014-3-31 下午5:42:17
 */
public class HttpUtil {

    /**
     * 判断网络连接是否打开,包括移动数据连接
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                if (InternetUtil.getNetworkState(context).equals("None")) {
                    SystemLog.Log("网络未连接");
                } else {
                    SystemLog.Log("当前网络:" + InternetUtil.getNetworkState(context));
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

//    public static String uploads(String xml, String path, String authorization) {
//        try {
//            byte[] data = xml.getBytes("UTF-8");
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(10 * 1000);
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST"); // Post方式
//            conn.setRequestProperty("connection", "keep-alive"); // 客户端到服务器端的连接持续有效
//            conn.setRequestProperty("Authorization", authorization);
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//
//            OutputStream outStream = conn.getOutputStream();
//            outStream.write(data);
//            outStream.flush();
//            outStream.close();
//            StringBuffer buf = new StringBuffer();
//            String s = "";
//
//            if (conn.getResponseCode() == 200) {
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                while ((s = br.readLine()) != null) {
//                    buf.append(s);
//                }
//                br.close();
//                in.close();
//            }
//            return buf.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "-2";
//    }

//    public static String upload(String source, String path, String authorization, String ContentType) {
//        byte[] data;
//        StringBuffer result = new StringBuffer();
//        String s = "";
//        try {
//            data = source.getBytes("UTF-8");
//            Log.i("i", "上传token=" + authorization);
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(10 * 1000);
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST"); // Post方式
//            conn.setRequestProperty("connection", "keep-alive");// 客户端到服务器端的连接持续有效
//            conn.setRequestProperty("Authorization", authorization);
//            conn.setRequestProperty("Content-Type", ContentType);
//            // "application/x-www-form-urlencoded");
//            // "application/json; charset=UTF-8");
//            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//
//            OutputStream outStream = conn.getOutputStream();
//            outStream.write(data);
//            outStream.flush();
//            outStream.close();
//            Log.i("i", "http返回code:" + conn.getResponseCode());
//            if (conn.getResponseCode() == 200) {
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                while ((s = br.readLine()) != null) {
//                    result.append(s);
//                }
//                br.close();
//                in.close();
//                Log.i("i", "http返回=" + result.toString());
//                return result.toString();
//            } else {
//                Log.i("i", "http返回=null");
//                return "null";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "null";
//    }

//    public static String uploadPut(String source, String path, String authorization, String ContentType) {
//        byte[] data;
//        StringBuffer result = new StringBuffer();
//        String s = "";
//        try {
//            data = source.getBytes("UTF-8");
//            Log.i("i", "上传token=" + authorization);
//            URL url = new URL(path);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(10 * 1000);
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false);
//            conn.setRequestMethod("PUT"); // Post方式
//            conn.setRequestProperty("connection", "keep-alive");// 客户端到服务器端的连接持续有效
//            conn.setRequestProperty("Authorization", authorization);
//            conn.setRequestProperty("Content-Type", ContentType);
//            // "application/x-www-form-urlencoded");
//            // "application/json; charset=UTF-8");
//            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
//
//            OutputStream outStream = conn.getOutputStream();
//            outStream.write(data);
//            outStream.flush();
//            outStream.close();
//            Log.i("i", "http返回code:" + conn.getResponseCode());
//            if (conn.getResponseCode() == 200) {
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                while ((s = br.readLine()) != null) {
//                    result.append(s);
//                }
//                br.close();
//                in.close();
//                Log.i("i", "http返回=" + result.toString());
//                return result.toString();
//            } else {
//                Log.i("i", "http返回=null");
//                return "null";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "null";
//    }

    // 数据上传方法
    //        public static String uploadData(String path, String ContentType, String data, String request, String authorization,
//                                    String ebon_encrypt) {

    public static String uploadData(BaseRequestParm parm,
                                    String ebon_encrypt) {

        HttpURLConnection conn = null;
        try {
            URL url = new URL(parm.getUrl());
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);
            conn.setDoInput(true);// 允许输入
            conn.setUseCaches(false);
            if (parm.getAuthorization() != null) {
                conn.setRequestProperty("token", parm.getAuthorization());// 认证
            }
            conn.setRequestProperty("connection", "keep-alive"); // 客户端到服务器端的连接持续有效
                conn.setRequestProperty("Content-Type", parm.getContentType());
            Log.i("path========", parm.getUrl());
            Log.i("data========", parm.getStringJsonData());
            Log.i("authorization======>", parm.getAuthorization());
            Log.i("request============>", parm.getRequest());
            Log.i("ContentType========>", parm.getContentType());
            if (parm.getRequest().equals("GET")) {
                // GET方式
                // httpurlconnection.setDoOutput(true); 这一句不要
                // httpurlconnection.setRequestMethod("GET"); 这一句不要，缺省就是get
            } else if (parm.getRequest().equals("DELETE")) {
                conn.setRequestMethod(parm.getRequest());
            } else if (parm.getRequest().equals("POST") || parm.getRequest().equals("PUT")) {
                // Post方式
                conn.setRequestMethod(parm.getRequest()); // Post方式
                conn.setDoOutput(true);// 允许输出

                OutputStream outputStream = conn.getOutputStream();
                byte[] bytes;
                bytes = parm.getStringJsonData().getBytes();
                outputStream.write(bytes);
                outputStream.flush();
                outputStream.close();
            }
            // int code = conn.getResponseCode();
            SystemLog.Log("code = " + conn.getResponseCode());
            if (conn.getResponseCode() == 200) {
                InputStream in = conn.getInputStream();
                byte[] inputBbytes = InputStreamTOByte(in);
                String results;
//                if (ebon_encrypt.equals("1")) {
//                    // 解密
//                    results = JniUtils.decrypt(inputBbytes);
//                } else {
                results = new String(inputBbytes, "utf-8");
//                }
                return results;
            } else if (conn.getResponseCode() == 401) {
                // return "用户认证失败";
                return "401";
            } else if (conn.getResponseCode() == 403) {
                // return "用户授权失败";
                return null;
            } else if (conn.getResponseCode() == 404) {
                // return "请求地址错误";
                return null;
            } else if (conn.getResponseCode() == 405) {
                // return "请求方法错误";
                return null;
            } else if (conn.getResponseCode() == 400) {
                // return "请求数据格式错误";
                InputStream in = conn.getErrorStream();

                byte[] inputBbytes = InputStreamTOByte(in);
                String results;

                results = new String(inputBbytes, "utf-8");

                BufferedReader bf = new BufferedReader(new InputStreamReader(in));

                return "code400" + results;
            } else if (conn.getResponseCode() == 500) {
                // return "系统内部错误";
                return null;
            }
        } catch (Exception e) {
            System.out.println("---->超时了");
            SystemLog.Log(e.toString());
        }
        return null;// 返回"401"：设置重新登陆
    }

    /**
     * 将InputStream转换成byte数组
     *
     * @param in InputStream
     * @return byte[]
     * @throws IOException
     */
    public static byte[] InputStreamTOByte(InputStream in) throws IOException {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024 * 4];
        int count = -1;
        while ((count = in.read(data, 0, 1024 * 4)) != -1)
            outStream.write(data, 0, count);

        data = null;
        return outStream.toByteArray();
    }

    // 从网络获取版本号
//    public static String upDataCode(String path) {
//        StringBuffer result = new StringBuffer();
//        String s = "";
//        try {
//            // URL url = new URL(path);
//            URL url = new URL(path);
//            // Log.i("url",);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(10 * 1000);
//            conn.setDoInput(true);// 允许输入
//            conn.setDoOutput(true);// 允许输出
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST"); // Post方式
//            conn.setRequestProperty("connection", "keep-alive"); // 客户端到服务器端的连接持续有效
//            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//
//            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
//            // writer.write(data);
//            writer.flush();
//            writer.close();
//            if (conn.getResponseCode() == 200) {
//                InputStream in = conn.getInputStream();
//                BufferedReader br = new BufferedReader(new InputStreamReader(in));
//                while ((s = br.readLine()) != null) {
//                    result.append(s);
//                }
//                br.close();
//                in.close();
//                return result.toString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "null";
//    }

    /**
     * 检查软件是否有更新版本
     *
     * @return
     * @throws Exception
     */

    // 解析 xml
//    public static HashMap<String, String> parseXml(String path) throws Exception {
//
//        // URL url = new URL(path);
//        // HttpURLConnection con = (HttpURLConnection) url.openConnection();
//        // con.setConnectTimeout(10 * 1000);
//        // con.setReadTimeout(10 * 1000);
//        // InputStream stream = con.getInputStream();
//        DefaultHttpClient client = new DefaultHttpClient();
//        HttpUriRequest req = new HttpGet(path);
//        HttpResponse resp = client.execute(req);
//        HttpEntity con = resp.getEntity();
//
//        InputStream stream = con.getContent();
//        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "GBK"));
//
//        String xmlString = "";
//        for (String temp = br.readLine(); temp != null; xmlString += temp, temp = br.readLine())
//            ;
//        Log.i("cccc====", xmlString);
//        // 去除字符串中的换行符，制表符，回车符。
//        // xmlString = xmlString.replaceAll("/n|/t|/r", "");
//
//        InputStream stream2 = new ByteArrayInputStream(xmlString.getBytes("UTF-8"));
//
//        // 实例化一个文档构建器工厂
//        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//        // 通过文档构建器工厂获取一个文档构建器
//        DocumentBuilder builder = factory.newDocumentBuilder();
//        // 通过文档通过文档构建器构建一个文档实例
//
//        Document document = builder.parse(stream2);
//        // 获取XML文件根节点
//        Element root = document.getDocumentElement();
//        // 获得所有子节点
//        NodeList childNodes = root.getChildNodes();
//        HashMap<String, String> hashMap = new HashMap<String, String>();
//        for (int j = 0; j < childNodes.getLength(); j++) {
//            // 遍历子节点
//            Node childNode = childNodes.item(j);
//            if (childNode.getNodeType() == Node.ELEMENT_NODE) {
//                Element childElement = (Element) childNode;
//                // 版本号
//                if ("version".equals(childElement.getNodeName())) {
//                    hashMap.put("version", childElement.getFirstChild().getNodeValue());
//                }
//                // 软件名称
//                else if (("name".equals(childElement.getNodeName()))) {
//                    hashMap.put("name", childElement.getFirstChild().getNodeValue());
//                }
//                // 下载地址
//                else if (("url".equals(childElement.getNodeName()))) {
//                    hashMap.put("url", childElement.getFirstChild().getNodeValue());
//                }
//                // 更新信息
//                else if (("info".equals(childElement.getNodeName()))) {
//
//                    hashMap.put("info", childElement.getFirstChild().getNodeValue());
//                }
//            }
//        }
//        return hashMap;
//    }

    // 下载APK
//    public static void downFile(final String url, final Handler handler, final String name) {
//        new Thread() {
//            @Override
//            public void run() {
//                HttpClient client = new DefaultHttpClient();
//                HttpGet get = new HttpGet(url);
//                HttpResponse response;
//                try {
//                    response = client.execute(get);
//                    HttpEntity entity = response.getEntity();
//                    long length = entity.getContentLength();
//                    InputStream is = entity.getContent();
//                    FileOutputStream fileOutputStream = null;
//                    if (is != null) {
//                        File dir = Environment.getExternalStorageDirectory();
//                        File file = new File(dir, name);
//                        fileOutputStream = new FileOutputStream(file);
//                        byte[] buf = new byte[1024];
//                        int ch = -1;
//                        long length2 = 0;
//                        while ((ch = is.read(buf)) != -1) {
//                            fileOutputStream.write(buf, 0, ch);
//                            length2 = length2 + ch;
//                            // 下载进度消息
//                            String pd = (length2 * 100 / length) + "%";
//                            PushMsg msg = handler.obtainMessage();
//                            msg.what = Constants.CHECKINFO;
//                            msg.obj = pd;
//                            handler.sendMessage(msg);
//                        }
//                        fileOutputStream.flush();
//                    }
//                    // 关闭流
//                    if (fileOutputStream != null) {
//                        fileOutputStream.close();
//                    }
//                    // 发送下载成功消息
//                    PushMsg msg = handler.obtainMessage();
//                    msg.what = Constants.UPDATESUCSS;
//                    handler.sendMessage(msg);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    // 下载失败发送消息
//                    PushMsg msg = handler.obtainMessage();
//                    msg.what = Constants.UPDATEFAIL;
//                    handler.sendMessage(msg);
//                }
//            }
//        }.start();
//    }
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase(Locale.CHINESE));
        }

        if (type == null || type.equals("")) {
            return "application/octet-stream";
        }
        return type;
    }

    // 上传头像/ 附件
    public static String postMethod(String mUrl, String authorization, HashMap<String, String> textMap,
                                    List<String> imageUrlList, String filePath) {
        String type = getMimeType(filePath);
        System.out.println("type：" + type);
        try {
            // 链接超时，请求超时设置
            BasicHttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, 10 * 1000);
            HttpConnectionParams.setSoTimeout(httpParams, 10 * 1000);

            // 请求参数设置
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(mUrl);
            post.setHeader("authorization", authorization);
//            post.setHeader("EBON-ENCRYPT", "0");// 头信息，0不加密，1加密
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8"));// 编码格式 必须这里设定
            // 上传 文本， 转换编码为utf-8 其中"text" 为字段名，
            // 后边new StringBody(text,
            // Charset.forName(CHARSET))为参数值，其实就是正常的值转换成utf-8的编码格式
            if (textMap != null && !textMap.equals("")) {
                Iterator iter = textMap.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = entry.getKey().toString();
                    String val = entry.getValue().toString();
                    entity.addPart(key, new StringBody(val, Charset.forName("UTF-8")));
                }
                // 上传多个文本可以在此处添加上边代码，修改字段和值即可
            }
            if (filePath != null && !filePath.equals("")) {
                // 上传文件
                entity.addPart("file", new FileBody(new File(filePath), getMimeType(filePath), "UTF-8"));
            }
            if (imageUrlList != null) {
                String[] imagePath = new String[imageUrlList.size()];
                int size = imageUrlList.size();
                for (int i = 0; i < size; i++) {
                    imagePath[i] = imageUrlList.get(i);
                }
                if (imagePath != null && imagePath.length > 0) {
                    // 上传图片
                    for (String p : imagePath) {
                        entity.addPart("avatar", new FileBody(new File(p), getMimeType(p), "UTF-8"));
                    }
                }

            }

            post.setEntity(entity);
            HttpResponse resp = client.execute(post);
            // 获取回调值
            // System.out.println("Response:"
            // + EntityUtils.toString(resp.getEntity()));
            // System.out.println("StatusCode:"
            // + resp.getStatusLine().getStatusCode());
            // String Response = EntityUtils.toString(resp.getEntity());
            int code = resp.getStatusLine().getStatusCode();
            if (code == 200) {
                InputStream dd = resp.getEntity().getContent();
                String str = Utils.InputStreamTOString(dd);
                return str;
            } else if (code == 401) {
                return "401";// 登录失效
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e.toString());
            return null;
        }
    }

    public static void start(Context context, String fileUrl, String name) {
        Intent downloadIntent = new Intent(context, DownloadFileService.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", fileUrl);
        bundle.putString("fileName", name);
        downloadIntent.putExtras(bundle);
        context.startService(downloadIntent);
        Constants.Downloading = true;
    }
}