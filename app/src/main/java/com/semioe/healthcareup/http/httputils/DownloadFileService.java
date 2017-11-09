package com.semioe.healthcareup.http.httputils;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.semioe.healthcareup.Constants;
import com.semioe.healthcareup.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class DownloadFileService extends IntentService {

    private static final String TAG = DownloadFileService.class.getSimpleName();
    private NotificationManager notificationManager;
    private Notification notification;

    private RemoteViews rViews;
    private File file;
    public boolean flag = false;
    private long fileLength, downloadLength;

    public DownloadFileService() {
        super("com.ebon.pms.enn.sj");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        // 获得下载文件的url
        String downloadUrl = bundle.getString("url");
        String fileName = bundle.getString("fileName");
        // 设置文件下载后的保存路径，保存在SD卡根目录的Download文件夹
        File dirs = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
        // 检查文件夹是否存在，不存在则创建
        if (!dirs.exists()) {
            dirs.mkdir();
        }
        file = new File(dirs, fileName);
        // 设置Notification
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.mipmap.logos, getResources().getString(R.string.app_name), System.currentTimeMillis());

        // 指定父类,在单击屏蔽意图
        Intent intentNotifi = new Intent(this, Object.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intentNotifi,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置通知主题的意图,在单击时候处理
        notification.contentIntent = pendingIntent;
        // 该标志表示当用户点击 Clear 之后，能够清除该通知。
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        // 加载Notification的布局文件
        rViews = new RemoteViews(getPackageName(), R.layout.downloadfile_layout);
        // 设置下载进度条
        rViews.setProgressBar(R.id.downloadFile_pb, 100, 0, false);
        rViews.setTextViewText(R.id.downloadFileName, fileName);
        rViews.setTextViewText(R.id.downloadP, "0%");

        notification.contentView = rViews;

        notificationManager.notify(0, notification);
        // 开始下载
        downloadFile(downloadUrl, file);
        // 删除通知
        notificationManager.cancel(0);
        // 如果下载完成,发送安装广播
        if (flag) {
            downloadBr(file);
        }
        // 设置变量
        Constants.Downloading = false;
    }

    private void downloadBr(File file) {
        // 广播出去，由广播接收器来处理下载完成的文件
        Intent sendIntent = new Intent("com.ebon.downloadComplete");
        // 把下载好的文件的保存地址加进Intent
        sendIntent.putExtra("downloadFile", file.getPath());
        sendBroadcast(sendIntent);
    }

    private void downloadFile(String downloadUrl, File file) {

        // 因为IntentService运行在独立的线程中,不能正常显示Toast
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "找不到保存下载文件的目录");
            e.printStackTrace();
        }
        InputStream ips = null;
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            huc.setRequestMethod("GET");
            huc.setReadTimeout(10000);
            huc.setConnectTimeout(3000);
            //获取相应的文件长度
//			fileLength = Integer.valueOf(huc.getHeaderField("Content-Length"));
            fileLength = huc.getContentLength();
            ips = huc.getInputStream();
            // 拿到服务器返回的响应码
            int hand = huc.getResponseCode();
            if (hand == 200) {
                // 开始检查下载进度
                handler.post(run);
                // 建立一个byte数组作为缓冲区，等下把读取到的数据储存在这个数组
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = ips.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    downloadLength = downloadLength + len;
                    Log.e(TAG, "len" + len);
                }
            } else {
                Log.e(TAG, "服务器返回码" + hand);
            }
            showToastByRunnable(this, "下载完成", Toast.LENGTH_LONG);
            flag = true;
        } catch (ProtocolException e) {
            e.printStackTrace();
            flag = false;
            showToastByRunnable(this, "下载失败", Toast.LENGTH_LONG);
            // 设置变量，代表当前没有下载apk
            Constants.Downloading = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            flag = false;
            showToastByRunnable(this, "下载失败", Toast.LENGTH_LONG);
            // 设置变量，代表当前没有下载apk
            Constants.Downloading = false;
        } catch (IOException e) {
            e.printStackTrace();
            flag = false;
            showToastByRunnable(this, "下载失败", Toast.LENGTH_LONG);
            // 设置变量，代表当前没有下载apk
            Constants.Downloading = false;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (ips != null) {
                    ips.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                // 设置变量，代表当前没有下载apk
                Constants.Downloading = false;
            }
        }
    }

    private void showToastByRunnable(final IntentService context, final CharSequence text, final int duration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, duration).show();
            }
        });
    }

    public void onDestroy() {
        // 移除定時器任务
        handler.removeCallbacks(run);
        super.onDestroy();
    }

    // 定时器，每隔一段时间检查下载进度，然后更新Notification上的ProgressBar
    private Handler handler = new Handler();
    private Runnable run = new Runnable() {
        public void run() {
            rViews.setProgressBar(R.id.downloadFile_pb, 100, (int) (downloadLength * 100 / fileLength), false);
            rViews.setTextViewText(R.id.downloadP, downloadLength * 100 / fileLength + "%");
            notification.contentView = rViews;
            notificationManager.notify(0, notification);
            handler.postDelayed(run, 1000);
        }
    };

}
