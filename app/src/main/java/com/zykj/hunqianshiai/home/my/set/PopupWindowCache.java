package com.zykj.hunqianshiai.home.my.set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.zykj.hunqianshiai.R;
import com.zykj.hunqianshiai.bases.BasePopupWindow;

/**
 * 清除缓存
 * Created by ${xu} on 2017/10/31.
 */

public class PopupWindowCache extends BasePopupWindow {

    private SetActivity mActivity;
    private ProgressDialog mDialog;

    public PopupWindowCache(Activity activity) {
        super(activity);
        this.mActivity = (SetActivity) activity;
        dismissPop();
        TextView affirm = (TextView) view.findViewById(R.id.tv_affirm);
        affirm.setOnClickListener(this);
    }

    @Override
    public int getViewId() {
        return R.layout.popupwindow_cache;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_affirm:
                mDialog = ProgressDialog.show(mActivity, "", "清除中...");

                mHandler.sendEmptyMessageDelayed(0, 1500);
                break;
        }
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mActivity.toastShow("清除成功");
            mDialog.dismiss();
            dismiss();
        }
    };
}
