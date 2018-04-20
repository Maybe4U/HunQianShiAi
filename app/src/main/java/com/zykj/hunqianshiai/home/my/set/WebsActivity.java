package com.zykj.hunqianshiai.home.my.set;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebSettings;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import butterknife.Bind;

public class WebsActivity extends BasesActivity {
    @Bind(R.id.web_view)
    BridgeWebView mWebView;
    @Override
    protected int getContentViewX() {
        return R.layout.activity_webs;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");
        String title = bundle.getString("title");
        appBar(title);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                FanKuiBean fanKuiBean = JsonUtils.GsonToBean(data, FanKuiBean.class);
//               toastShow(data);
                finish();
            }
        });
    }
}
