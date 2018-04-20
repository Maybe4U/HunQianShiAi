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

public class ShareActivity extends BasesActivity {
    @Bind(R.id.web_view)
    BridgeWebView mWebView;
    @Override
    protected int getContentViewX() {
        return R.layout.activity_share;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("title");
        String url = bundle.getString("url");
        appBar("邀请好友");
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(url);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                FanKuiBean fanKuiBean = JsonUtils.GsonToBean(data, FanKuiBean.class);
                if (fanKuiBean.flg == 6) {
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 创建普通字符型ClipData
                    ClipData mClipData = ClipData.newPlainText("Label", fanKuiBean.url);
                    // 将ClipData内容放到系统剪贴板里。
                    cm.setPrimaryClip(mClipData);
                    // 将文本内容放到系统剪贴板里。
//                    cm.setText(fanKuiBean.url);
                    toastShow("复制成功");
                } else if (fanKuiBean.flg == 7) {
                    showShare(UrlContent.USER_PIC, UrlContent.USER_NAME + "邀请你加入婚前试爱");
                }
            }
        });
    }
}
