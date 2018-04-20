package com.zykj.hunqianshiai.home.my.set;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.tools.JsonUtils;

import butterknife.Bind;

public class FanKuiActivity extends BasesActivity {

    @Bind(R.id.wv_web)
    BridgeWebView mWebView;
    @Override
    protected int getContentViewX() {
        return R.layout.activity_fan_kui;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String url = bundle.getString("url");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                toastShow(data);
                FanKuiBean fanKuiBean = JsonUtils.GsonToBean(data, FanKuiBean.class);
                switch (fanKuiBean.flg) {
                    case 2:
                        openActivity(FeedbackActivity.class);
                        break;
                    case 3:
                        finish();
                        break;
                }
            }
        });
    }
}
