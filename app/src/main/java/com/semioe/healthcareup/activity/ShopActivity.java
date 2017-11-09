package com.semioe.healthcareup.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.semioe.healthcareup.R;

public class ShopActivity extends BaseActivity implements View.OnClickListener {

    private WebView webview;
    private ImageView iv_back;

    @Override
    public int bindLayout() {
        return R.layout.activity_shop;
    }

    @Override
    public void initView(View view) {
        webview = (WebView) findViewById(R.id.webview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    @Override
    public void doBusiness(Context mContext) {
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("file:///android_asset/Equmall/list.html");
        webview.setWebViewClient(new HelloWebViewClient());

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class HelloWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    protected String[] getNeedPermissions() {
        return new String[0];
    }

    @Override
    protected void permissionGrantedSuccess() {

    }

    @Override
    protected void permissionGrantedFail() {

    }

}
