package com.zykj.hunqianshiai.web;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import butterknife.Bind;

public class WebViewActivity extends BasesActivity {

    @Bind(R.id.web_view)
    BridgeWebView mWebView;
    @Override
    protected int getContentViewX() {
        return R.layout.activity_web_view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String url = bundle.getString("url");
        appBar(title);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                toastShow(data);
            }
        });
    }
}
