package com.zykj.hunqianshiai.home.region;

import android.annotation.SuppressLint;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BaseFragment;
import com.zykj.hunqianshiai.net.UrlContent;
import com.zykj.hunqianshiai.tools.JsonUtils;

import butterknife.Bind;
import io.rong.imkit.RongIM;

/**
 * Created by xu on 2017/12/5.
 */

public class RegionFragment extends BaseFragment {

    @Bind(R.id.web_view)
    BridgeWebView mWebView;

    public RegionFragment() {
    }

    public static RegionFragment getInstance() {
        return Instance.mRegionFragment;
    }

    private static class Instance {
        static RegionFragment mRegionFragment = new RegionFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_region;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView() {
        TextView tv_title = mView.findViewById(R.id.tv_title);
        tv_title.setText("脸谱");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.loadUrl(UrlContent.FACE_ALIKE_URL + "&userid=" + UrlContent.USER_ID);


        mWebView.setDefaultHandler(new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                RegionBean regionBean = JsonUtils.GsonToBean(data, RegionBean.class);
                RongIM.getInstance().startPrivateChat(mContext, regionBean.userid, regionBean.username);
            }
        });
    }
}
