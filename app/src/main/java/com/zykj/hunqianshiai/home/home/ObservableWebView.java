package com.zykj.hunqianshiai.home.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.lzyzsd.jsbridge.BridgeWebView;

/**
 * Created by xu on 2018/1/31.
 */

public class ObservableWebView extends BridgeWebView {
    private OnScrollChangedCallback mOnScrollChangedCallback;

    public ObservableWebView(final Context context) {
        super(context);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableWebView(final Context context, final AttributeSet attrs,
                             final int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(final int l, final int t, final int oldl,
                                   final int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (mOnScrollChangedCallback != null) {
//            mOnScrollChangedCallback.onScroll(l - oldl, t - oldt);
            mOnScrollChangedCallback.onScroll(l, t, oldl, oldt);
        }
    }

    public OnScrollChangedCallback getOnScrollChangedCallback() {
        return mOnScrollChangedCallback;
    }

    public void setOnScrollChangedCallback(
            final OnScrollChangedCallback onScrollChangedCallback) {
        mOnScrollChangedCallback = onScrollChangedCallback;
    }

    /**
     * Impliment in the activity/fragment/view that you want to listen to the webview
     */
    public static interface OnScrollChangedCallback {
        //        public void onScroll(int dx, int dy);
        public void onScroll(int sx, int sy, int ex, int ey);
    }

}
