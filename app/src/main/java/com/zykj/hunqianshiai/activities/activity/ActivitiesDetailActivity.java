package com.zykj.hunqianshiai.activities.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseView;
import com.zykj.hunqianshiai.bases.BasesActivity;
import com.zykj.hunqianshiai.net.UrlContent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 活动详情
 */
public class ActivitiesDetailActivity extends BasesActivity implements BaseView<String> {
    @Bind(R.id.web_view)
    BridgeWebView mWebView;
    @Bind(R.id.iv_right_share)
    ImageView mShare;

    private Bundle mBundle;
    private String mState;
    private String mTitle;
    private String mUrl;
    private String mActid;

    @Override
    protected int getContentViewX() {
        return R.layout.activity_activities_detail;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void initView() {
        mBundle = getIntent().getExtras();
        mTitle = mBundle.getString("title");
        mUrl = mBundle.getString("url");
        mActid = mBundle.getString("actid");
        mState = mBundle.getString("state");
        appBar(mTitle);
        //显示分享按钮
        mShare.setVisibility(View.VISIBLE);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(mUrl);

        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
//                toastShow(data);
            }
        });
    }

    @OnClick({R.id.tv_apply,R.id.iv_right_share})
    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_apply:
                if (mState.equals("1")) {
                    openActivity(ActivitiesApplyActivity.class, mBundle);
                } else {
                    toastShow("活动已经结束");
                }

                break;

            case R.id.iv_right_share:
                //Log.e("分享","点击");
                showShare(mTitle,"首家实名制认证严肃婚恋社交平台",mUrl,mActid);
                break;
        }
    }

    @Override
    public void success(String bean) {

    }

    @Override
    public void refresh(String bean) {

    }

    @Override
    public void loadMore(String bean) {

    }

    @Override
    public void failed(String content) {

    }
}
